package br.com.alexsander.leitor.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FlashlightOff
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import br.com.alexsander.leitor.compose.ClipBoardModal
import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.viewmodel.CodeViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning

const val HOME_ROUTE = "Escâner"

fun NavHostController.navigateToHome() {
    navigate(HOME_ROUTE, navOptions {
        popUpTo(CODES_ROUTE) {
            inclusive = true
        }
    })
}

fun NavGraphBuilder.homeScreen(viewModel: CodeViewModel, copy: (String) -> Unit = {}) {
    composable(HOME_ROUTE)
    {
        HomeScreen(viewModel::insert, copy)
    }
}

@Composable
fun HomeScreen(onRead: (Code) -> Unit = { }, copy: (String) -> Unit = { }) {
    val context = LocalContext.current
    val life = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val managedActivityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}
    val previewView = PreviewView(context)
    val cameraController = remember { LifecycleCameraController(context) }
    var code by remember { mutableStateOf<Code?>(null) }
    val torchEnabled = remember { mutableStateOf(false) }
    val barcodeScanner = BarcodeScanning.getClient()
    LaunchedEffect(Unit) {
        managedActivityResultLauncher.launch(android.Manifest.permission.CAMERA)
        cameraController.unbind()

        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                COORDINATE_SYSTEM_VIEW_REFERENCED,
                ContextCompat.getMainExecutor(context)
            ) { result: MlKitAnalyzer.Result? ->
                result?.getValue(barcodeScanner)?.forEach { r ->
                    if (code == null) {
                        code = Code(value = r.rawValue.toString())
                        onRead(code!!)
                    }
                }
            }
        )

        cameraController.bindToLifecycle(life)
        previewView.controller = cameraController
    }

    Box {
        AndroidView({ previewView }, Modifier.fillMaxSize())
        IconButton(
            onClick = {
                torchEnabled.value = !torchEnabled.value
                cameraController.enableTorch(torchEnabled.value)
            },
            Modifier.align(Alignment.Center),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = Color(0.5f, 0.5f, 0.5f, 0.5f)
            )
        ) {
            Icon(
                imageVector = if (torchEnabled.value) Icons.Rounded.FlashlightOff else Icons.Rounded.FlashlightOn,
                contentDescription = ""
            )
        }
        if (code != null) {
            ClipBoardModal(code, {
                copy(it)
                code = null
            }) {
                code = null
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}