package com.mcmouse88.android_privacy.presentation.add_log

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.mcmouse88.android_privacy.data.local.entity.MAX_LOG_PHOTOS_LIMIT
import com.mcmouse88.android_privacy.presentation.Screens
import com.mcmouse88.android_privacy.presentation.component.PhotoGrid
import com.mcmouse88.android_privacy.utils.ObserveAsEvent
import com.mcmouse88.android_privacy.utils.rememberSnackbarHostState
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLogScreen(
    navController: NavHostController,
    viewModel: AddLogViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = rememberSnackbarHostState()
    val internalPhotoPickerState = rememberModalBottomSheetState()

    ObserveAsEvent(flow = viewModel.commands) { command ->
        when (command) {
            AddLogViewModel.Commands.ShowBottomMessage -> {
                snackbarHostState.showSnackbar(
                    "You can't add more than $MAX_LOG_PHOTOS_LIMIT photos"
                )
            }

            AddLogViewModel.Commands.NavigateToHomeScreen -> {
                navController.navigate(Screens.Home.route) {
                    popUpTo(Screens.Home.route) {
                        inclusive = false
                    }
                }
            }

            AddLogViewModel.Commands.NavigateToCameraScreen -> {
                navController.navigate(Screens.Camera.route)
            }
        }
    }

    // TODO: Step 1. Register ActivityResult to request Camera permission

    // TODO: Step 3. Add explanation dialog for Camera permission

    // TODO: Step 5. Register ActivityResult to request Location permissions
    // TODO: Step 8. Change activity result to only request Coarse Location

    // TODO: Step 6. Add explanation dialog for Location permissions

    // TODO: Step 11. Register ActivityResult to launch the Photo Picker

    LaunchedEffect(key1 = Unit) {
        viewModel.refreshSavedPhotos()
    }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(MAX_LOG_PHOTOS_LIMIT),
        onResult = viewModel::onPhotoPickerSelect
    )

    ModalBottomSheet(
        onDismissRequest = { },
        sheetState = internalPhotoPickerState,
        ) {
        PhotoPicker(
            entries = state.localPickerPhoto,
            modifier = Modifier.fillMaxSize(),
            onSelect = { uri ->
                coroutineScope.launch {
                    internalPhotoPickerState.hide()
                    viewModel.onLocalPhotoPickerSelect(uri)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Log", fontFamily = FontFamily.Serif) },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = navController::navigateUp) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Save Log") },
                icon = {
                    if (state.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                    }
                },
                onClick = viewModel::createLog
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            ListItem(
                headlineContent = { Text(text = "Date") },
                trailingContent = {
                    // TODO: Step Over. Realize Composable DatePicker
                   DatePicker(timeInMillis = state.date, onChange = viewModel::onDateChange)
                }
            )
            Divider()

            ListItem(
                headlineContent = { Text(text = "Location") },
                trailingContent = {
                    // TODO: Step 7. Check, request, and explain Location permission
                    LocationPicker(address = state.place) {
                        fetchLocation(
                            context = context,
                            onPlaceDetected = viewModel::onPlaceDetected
                        )
                    }
                    // TODO: Step 9. Change location request to only request COARSE location
                }
            )
            Divider()

            ListItem(
                headlineContent = { Text(text = "Photos") },
                trailingContent = {
                    Row {
                        TextButton(
                            onClick = {
                                viewModel.loadLocalPickerPictures()
                                coroutineScope.launch {
                                    // TODO: Step 12. Replace the line below showing our internal
                                    //  photo picking UI and launch the Android Photo Picker instead
                                    internalPhotoPickerState.show()
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.PhotoLibrary, contentDescription = null)
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = "Add photo")
                        }

                        IconButton(
                            onClick = {
                                // TODO: Step 2. Check & request for camera permissions before
                                //  navigating to the camera screen
                                viewModel.onAddPhotoClick()
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.AddAPhoto, contentDescription = null)
                        }
                    }
                }
            )

            PhotoGrid(
                photos = state.savedPhotos,
                modifier = Modifier.padding(16.dp),
                onRemove = viewModel::onPhotoRemoved
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(timeInMillis: Long, onChange: (time: Long) -> Unit) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = timeInMillis
    )
    DatePicker(state = datePickerState)
}

@Composable
fun LocationPicker(
    address: String?,
    fetchLocation: () -> Unit
) {
    TextButton(onClick = fetchLocation) {
        Icon(imageVector = Icons.Filled.Explore, contentDescription = null)
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = address ?: "Get location")
    }
}

@Composable
fun PhotoPicker(
    entries: List<Uri>,
    modifier: Modifier = Modifier,
    onSelect: (uri: Uri) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = modifier) {
        items(entries) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable { onSelect.invoke(uri) }
            )
        }
    }
}

@Composable
fun CameraExplanationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Camera access") },
        text = { Text(text = "PhotoLog would like access to the camera to be able take picture when creating a log") },
        icon = {
            Icon(
                imageVector = Icons.Filled.Camera,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Continue")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Dismiss")
            }
        }
    )
}

@Composable
fun LocationExplanationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Location access") },
        text = { Text(text = "Photolog would like access to your location to save it when creating a log") },
        icon = {
            Icon(
                imageVector = Icons.Filled.Explore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Continue")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Dismiss")
            }
        }
    )
}

@SuppressLint("MissingPermission")
private inline fun fetchLocation(context: Context, crossinline onPlaceDetected: (String?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location ?: return@addOnSuccessListener

        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                val address = addresses.firstOrNull()
                val place = address?.locality ?: address?.subAdminArea ?: address?.adminArea
                ?: address?.countryName
                onPlaceDetected.invoke(place)
            }
        } else {
            @Suppress("DEPRECATION")
            val address = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1)?.firstOrNull() ?: return@addOnSuccessListener

            val place = address.locality ?: address.subAdminArea ?: address.adminArea ?: address.countryName
            ?: return@addOnSuccessListener
            onPlaceDetected.invoke(place)
        }
    }
}