package br.com.alexsander.leitor.screens

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import br.com.alexsander.leitor.R
import br.com.alexsander.leitor.compose.TextToQRCode
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val GENERATE_ROUTE = "Gerar QRCode"

fun NavHostController.navigateToGenerate() {
    navigate(GENERATE_ROUTE, navOptions {
        popUpTo(HOME_ROUTE) {
            inclusive = true
        }
    })
}

fun NavGraphBuilder.generateScreen() {
    composable(GENERATE_ROUTE)
    {
        GenerateScreen()
    }
}

@Composable
fun GenerateScreen() {
    var textFieldValue by remember { mutableStateOf("") }
    var showGeneratedQRCode by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val canGenerate = textFieldValue.isNotEmpty() && !showGeneratedQRCode

    fun onValueChange(it: String) {
        textFieldValue = it
        showGeneratedQRCode = false
    }

    fun onClickGenrate() {
        showGeneratedQRCode = textFieldValue.isNotEmpty()
    }

    val context = LocalContext.current
    fun onClickDownload() {
        val filename = "qr_code${System.currentTimeMillis()}.png"
        val file = File(context.cacheDir, filename)
        try {
            val outputStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            val bitmapUri =
                FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, bitmapUri)
            }
            context.startActivity(Intent.createChooser(intent, "Share"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            placeholder = { Text(stringResource(R.string.type_here)) },
            onValueChange = ::onValueChange
        )
        Button(
            ::onClickGenrate,
            Modifier.height(48.dp),
            enabled = canGenerate
        ) {
            Text(stringResource(R.string.generate_qrcode))
            Icon(
                Icons.Filled.Image,
                ""
            )
        }
        if (showGeneratedQRCode && textFieldValue.isNotEmpty()) {
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextToQRCode(textFieldValue) { bitmap = it }
                Button(
                    ::onClickDownload,
                    Modifier.height(48.dp)
                ) {
                    Text(stringResource(R.string.download))
                    Icon(
                        Icons.Filled.Download,
                        ""
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GenerateScreenPreview() {
    GenerateScreen()
}