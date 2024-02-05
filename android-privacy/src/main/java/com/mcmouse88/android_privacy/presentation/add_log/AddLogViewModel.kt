package com.mcmouse88.android_privacy.presentation.add_log

import android.Manifest
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcmouse88.android_privacy.data.local.entity.LogEntry
import com.mcmouse88.android_privacy.data.local.entity.MAX_LOG_PHOTOS_LIMIT
import com.mcmouse88.android_privacy.domain.repository.MediaRepository
import com.mcmouse88.android_privacy.domain.repository.PhotoSaverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddLogViewModel @Inject constructor(
    private val photoSaver: PhotoSaverRepository,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    private val _commands = MutableSharedFlow<Commands>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val commands = _commands.asSharedFlow()

    fun onPermissionChange(permission: String, isGranted: Boolean) {
        when (permission) {
            Manifest.permission.ACCESS_COARSE_LOCATION -> {
                _uiState.update { it.copy(hasLocationAccess = isGranted) }
            }

            Manifest.permission.ACCESS_FINE_LOCATION -> {
                _uiState.update { it.copy(hasLocationAccess = isGranted) }
            }

            Manifest.permission.CAMERA -> {
                _uiState.update { it.copy(hasCameraAccess = isGranted) }
            }
        }
    }

    fun onDateChange(dateInMillis: Long) {
        _uiState.update { it.copy(date = dateInMillis) }
    }

    fun loadLocalPickerPictures() {
        if (canAddPhoto()) {
            viewModelScope.launch {
                val localPickerPhotos = mediaRepository.fetchImages().map { it.uri }.toList()
                _uiState.update { it.copy(localPickerPhoto = localPickerPhotos) }
            }
        } else {
            _commands.tryEmit(Commands.ShowBottomMessage)
        }
    }

    fun onLocalPhotoPickerSelect(photo: Uri) {
        viewModelScope.launch {
            photoSaver.cacheFromUri(photo)
            refreshSavedPhotos()
        }
    }

    fun onPhotoPickerSelect(photos: List<Uri>) {
        viewModelScope.launch {
            photoSaver.cacheFromUris(photos)
            refreshSavedPhotos()
        }
    }

    fun onPhotoRemoved(photo: File) {
        viewModelScope.launch {
            photoSaver.removeFile(photo)
            refreshSavedPhotos()
        }
    }

    fun refreshSavedPhotos() {
        _uiState.update { it.copy(savedPhotos = photoSaver.getPhotos()) }
    }

    fun createLog() {
        val isValid = (uiState.value.place != null
                && uiState.value.savedPhotos.isNotEmpty())
                && !uiState.value.isSaving

        if (isValid.not()) {
            _commands.tryEmit(Commands.ShowBottomMessage)
            return
        }

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val photos = photoSaver.savePhotos()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = uiState.value.date
            val log = LogEntry(
                date = getIsoDate(uiState.value.date),
                place = uiState.value.place.orEmpty(),
                photo1 = photos[0].name,
                photo2 = photos.getOrNull(1)?.name,
                photo3 = photos.getOrNull(2)?.name
            )

            mediaRepository.insert(log)
            _commands.tryEmit(Commands.NavigateToHomeScreen)
        }
    }

    fun onAddPhotoClick() {
        if (canAddPhoto()) {
            _commands.tryEmit(Commands.NavigateToCameraScreen)
        } else {
            _commands.tryEmit(Commands.ShowBottomMessage)
        }
    }

    fun onPlaceDetected(place: String?) {
        _uiState.update { it.copy(place = place) }
    }

    private fun getTodayDateInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    private fun getIsoDate(timeInMillis: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(timeInMillis)
    }

    private fun canAddPhoto(): Boolean {
        return _uiState.value.savedPhotos.size < MAX_LOG_PHOTOS_LIMIT
    }

    data class UiState(
        val hasLocationAccess: Boolean = false,
        val hasCameraAccess: Boolean = false,
        val isSaving: Boolean = false,
        val date: Long = System.currentTimeMillis(),
        val place: String? = null,
        val savedPhotos: List<File> = emptyList(),
        val localPickerPhoto: List<Uri> = emptyList()
    )

    sealed interface Commands {
        data object ShowBottomMessage : Commands
        data object NavigateToHomeScreen : Commands
        data object NavigateToCameraScreen : Commands
    }
}