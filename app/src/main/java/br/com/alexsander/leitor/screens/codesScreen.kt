package br.com.alexsander.leitor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import br.com.alexsander.leitor.compose.CodeItem
import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.viewmodel.CodeViewModel

const val CODES_ROUTE = "Códigos"

fun NavHostController.navigateToCodes() {
    navigate(CODES_ROUTE, navOptions {
        popUpTo(HOME_ROUTE) {
            inclusive = true
        }
    })
}

fun NavGraphBuilder.codesScreen(
    viewModel: CodeViewModel,
    navController: NavHostController,
    copy: (String) -> Unit = { },
    delete: (Code) -> Unit = { }
) {
    composable(CODES_ROUTE)
    {
        val codes by viewModel.codes.collectAsState(emptyList())
        CodeScreen(codes, navController::navigateToHome, copy, delete)
    }
}

@Composable
fun CodeScreen(
    codes: List<Code>,
    goToHome: () -> Unit = {},
    copy: (String) -> Unit = {},
    delete: (Code) -> Unit = {}
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (codes.isEmpty()) {
            item {
                Text(
                    text = "Você ainda não escaneou nenhum código de barras. Toque no botão abaixo para começar a escanear.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
                Icon(
                    Icons.Rounded.Search, null, Modifier.size(48.dp),
                    tint = Color.Black
                )
                Button(goToHome, Modifier.height(48.dp)) {
                    Text("clique aqui para escanear")
                    Icon(Icons.Rounded.CameraAlt, null, Modifier.size(48.dp))
                }
            }
        }
        items(codes)
        { code ->
            CodeItem(code, copy, delete)
        }
    }
}

@Preview
@Composable
fun CodeScreenPreview() {
    CodeScreen(
        codes = listOf(
//            Code(value = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
//            Code(value = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        )
    )
}