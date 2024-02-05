package com.mcmouse88.android_privacy.presentation.camera

import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModel
import com.mcmouse88.android_privacy.domain.repository.PhotoSaverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val photoSaver: PhotoSaverRepository
) : ViewModel() {

    private val _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    fun takePhoto(photo: File) {
        _cameraState.update { it.copy(isTakingPicture = true, imageFile = photo) }
        photoSaver.cacheCapturedPhoto(photo)
    }

    data class CameraState(
        val isTakingPicture: Boolean = false,
        val imageFile: File? = null,
        val captureError: ImageCaptureException? = null
    )
}