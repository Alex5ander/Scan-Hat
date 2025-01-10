package br.com.alexsander.leitor.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AD() {
    AndroidView(
        {
            AdView(it).apply {
                setAdSize(AdSize.BANNER)
                //                                    ca-app-pub-3940256099942544/9214589741
                adUnitId = "ca-app-pub-3940256099942544/9214589741"
                loadAd(AdRequest.Builder().build())
            }
        },
        Modifier
            .fillMaxWidth()
            .height(AdSize.BANNER.height.dp)
    )
}