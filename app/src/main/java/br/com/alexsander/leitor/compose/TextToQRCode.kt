package br.com.alexsander.leitor.compose

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun ColumnScope.TextToQRCode(text: String, onGenerated: (Bitmap) -> Unit) {
    val density = LocalDensity.current.density
    val size = (300.dp * density).value.toInt()
    val qrCodeBitmap = remember(text) {
        val matrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)

        val bitmap =
            Bitmap.createBitmap(matrix.width, matrix.height, Bitmap.Config.ARGB_8888).apply {
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        val pixelColor = if (matrix[x, y]) Color.BLACK else Color.WHITE
                        setPixel(x, y, pixelColor)
                    }
                }
            }
        onGenerated(bitmap)
        bitmap.asImageBitmap()
    }
    Image(modifier = Modifier.weight(1f), bitmap = qrCodeBitmap, contentDescription = "QR Code")
}