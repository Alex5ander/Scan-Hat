package br.com.alexsander.leitor.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import br.com.alexsander.leitor.compose.CameraPreview
import br.com.alexsander.leitor.compose.ClipBoardModal
import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.viewmodel.CodeViewModel
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer

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
    val managedActivityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}
    val cameraController = remember { LifecycleCameraController(context) }
    var code by remember { mutableStateOf<Code?>(null) }
    val torchEnabled = remember { mutableStateOf(false) }

    fun decodeBarcode(bitmap: Bitmap): String? {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        val source = RGBLuminanceSource(bitmap.width, bitmap.height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

        return try {
            MultiFormatReader().decode(binaryBitmap).text
        } catch (e: Exception) {
            null
        }
    }

    LaunchedEffect(Unit) {
        managedActivityResultLauncher.launch(android.Manifest.permission.CAMERA)

        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context)
        ) { imageProxy ->
            try {
                val bitmap = imageProxy.toBitmap()
                val decodedText =
                    decodeBarcode(bitmap)
                if (decodedText != null && code == null) {
                    code = Code(value = decodedText)
                    onRead(code!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                imageProxy.close()
            }
        }
    }

    Box {
        CameraPreview(cameraController, Modifier.fillMaxSize())
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