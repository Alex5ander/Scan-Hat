package br.com.alexsander.leitor.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import br.com.alexsander.leitor.R
import br.com.alexsander.leitor.ROUTE
import br.com.alexsander.leitor.screens.navigateToCodes
import br.com.alexsander.leitor.screens.navigateToGenerate
import br.com.alexsander.leitor.screens.navigateToHome

@Composable
fun BottomBar(
    navController: NavHostController,
    currentBackStackEntry: NavBackStackEntry?
) {
    BottomAppBar(
        actions = {
            IconButton(
                navController::navigateToHome,
                Modifier.weight(1f),
                enabled = currentBackStackEntry?.destination?.route != ROUTE.FIRST.name,
            ) {
                Icon(Icons.Filled.QrCodeScanner, stringResource(R.string.first_bottom_button))
            }
            IconButton(
                navController::navigateToCodes,
                Modifier.weight(1f),
                enabled = currentBackStackEntry?.destination?.route != ROUTE.SECOND.name,
            ) {
                Icon(Icons.Filled.History, stringResource(R.string.second_bottom_button))
            }
            IconButton(
                navController::navigateToGenerate,
                Modifier.weight(1f),
                enabled = currentBackStackEntry?.destination?.route != ROUTE.THIRD.name,
            ) {
                Icon(Icons.Filled.QrCode, stringResource(R.string.third_bottom_button))
            }
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    )
}