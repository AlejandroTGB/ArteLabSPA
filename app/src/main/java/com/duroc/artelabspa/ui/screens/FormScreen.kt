package com.duroc.artelabspa.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.duroc.artelabspa.viewmodel.FormViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    viewModel: FormViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var showImageSourceDialog by remember { mutableStateOf(false) }
    var expandedCategoria by remember { mutableStateOf(false) }

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    var tempCameraFile by remember { mutableStateOf<File?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val rutaArchivoCopiado = copiarImagenAlApp(context, uri)
                if (rutaArchivoCopiado != null) {
                    viewModel.onPhotoChange(rutaArchivoCopiado)
                } else {
                    Toast.makeText(context, "Error al procesar imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && tempCameraFile != null) {
                viewModel.onPhotoChange(tempCameraFile!!.absolutePath)
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val (file, uri) = crearArchivoTemporal(context)
                tempCameraFile = file
                tempCameraUri = uri
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Se requiere permiso de cámara", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(key1 = true) {
        viewModel.validationEvent.collect { event ->
            when (event) {
                is FormViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, "¡Producto publicado!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
                is FormViewModel.ValidationEvent.Failure -> {
                    Toast.makeText(context, event.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onSave() }) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { showImageSourceDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (state.fotoUri.isNotEmpty()) {
                    AsyncImage(
                        model = File(state.fotoUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(48.dp))
                        Text("Toca para agregar foto")
                    }
                }
            }

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = if (state.precio == 0) "" else state.precio.toString(),
                onValueChange = { viewModel.onPriceChange(it) },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = !expandedCategoria }
            ) {
                OutlinedTextField(
                    value = state.categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }
                ) {
                    listOf("Arte", "Pintura", "Escultura", "Dibujo", "Fotografía", "Material", "Cuadro", "Kit", "Otro").forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria) },
                            onClick = {
                                viewModel.onCategoryChange(categoria)
                                expandedCategoria = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Button(
                onClick = { viewModel.onSave() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar Venta")
            }
        }

        if (showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = { showImageSourceDialog = false },
                title = { Text("Seleccionar imagen") },
                text = { Text("¿Cámara o Galería?") },
                confirmButton = {
                    TextButton(onClick = {
                        showImageSourceDialog = false
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) { Text("Galería") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showImageSourceDialog = false
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }) { Text("Cámara") }
                }
            )
        }
    }
}

fun crearArchivoTemporal(context: Context): Pair<File, Uri> {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(null) // Usamos carpeta privada de la app
    val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)

    val uri = FileProvider.getUriForFile(
        context,
        "com.duroc.artelabspa.provider",
        file
    )
    return Pair(file, uri)
}

fun copiarImagenAlApp(context: Context, uriOriginal: Uri): String? {
    return try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileDestino = File(context.getExternalFilesDir(null), "IMG_$timeStamp.jpg")

        val inputStream: InputStream? = context.contentResolver.openInputStream(uriOriginal)
        val outputStream = FileOutputStream(fileDestino)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        fileDestino.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}