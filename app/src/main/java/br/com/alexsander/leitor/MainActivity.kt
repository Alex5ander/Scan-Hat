package br.com.alexsander.leitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alexsander.leitor.data.AppDatabase
import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.screens.CODES_ROUTE
import br.com.alexsander.leitor.screens.HOME_ROUTE
import br.com.alexsander.leitor.screens.codesScreen
import br.com.alexsander.leitor.screens.homeScreen
import br.com.alexsander.leitor.screens.navigateToCodes
import br.com.alexsander.leitor.screens.navigateToHome
import br.com.alexsander.leitor.ui.theme.LeitorTheme
import br.com.alexsander.leitor.viewmodel.CodeViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        installSplashScreen()
        val db = AppDatabase.getInstance(this)
        val codeDAO = db.codeDAO()
        val viewModel by viewModels<CodeViewModel> { CodeViewModel.provideFactory(codeDAO) }
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val clipboardManager = LocalClipboardManager.current
            val scope = rememberCoroutineScope()
            val snackBarHostState = remember { SnackbarHostState() }
            fun showSnackBar(text: String) {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        text, withDismissAction = true
                    )
                }
            }

            fun copy(text: String) {
                clipboardManager.setText(AnnotatedString(text))
                showSnackBar("copiado para área de transferência")
            }

            fun delete(code: Code) {
                viewModel.delete(code)
                showSnackBar("Código deletado com sucesso!")
            }

            LeitorTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                    topBar = {
                        CenterAlignedTopAppBar(
                            { Text(currentBackStackEntry?.destination?.route.toString()) },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    },
                    bottomBar = {
                        BottomAppBar(
                            actions = {
                                IconButton(
                                    navController::navigateToHome,
                                    Modifier.weight(1f),
                                    enabled = currentBackStackEntry?.destination?.route == CODES_ROUTE,
                                ) {
                                    Icon(Icons.Filled.QrCodeScanner, "")
                                }
                                IconButton(
                                    navController::navigateToCodes,
                                    Modifier.weight(1f),
                                    enabled = currentBackStackEntry?.destination?.route == HOME_ROUTE,
                                ) {
                                    Icon(Icons.Filled.QrCode, "")
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = HOME_ROUTE,
                            Modifier.weight(1f)
                        ) {
                            homeScreen(viewModel) { copy(it) }
                            codesScreen(viewModel, navController, { copy(it) }, { delete(it) })
                        }
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
                }
            }
        }
    }
}


