package br.com.alexsander.leitor.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        CodeItem(code = code!!, { copy(it) })
    }
}


@Preview
@Composable
fun ClipBoardModalPreview() {
    ClipBoardModal(code = Code(value = "text"), copy = { }) { }
}