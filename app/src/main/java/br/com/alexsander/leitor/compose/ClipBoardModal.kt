package br.com.alexsander.leitor.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import br.com.alexsander.leitor.data.Code

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ClipBoardModal(
    code: Code?,
    copy: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        CodeItem(code = code!!, { copy(it) })
    }
}