package br.com.alexsander.leitor.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import br.com.alexsander.leitor.R
import br.com.alexsander.leitor.data.Code

@Composable
fun CodeItem(
    code: Code,
    copy: (String) -> Unit = { },
    delete: ((Code) -> Unit?)? = null
) {
    var openDialog by remember { mutableStateOf(false) }
    ListItem(headlineContent = {
        Text(
            text = code.value,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    },
        trailingContent = {
            Row {
                IconButton({ copy(code.value) }) {
                    Icon(Icons.Rounded.ContentCopy, "")
                }
                if (delete != null) {
                    IconButton(
                        { openDialog = true },
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)
                    ) {
                        Icon(Icons.Rounded.Clear, "")
                    }
                }
            }
        }
    )
    HorizontalDivider()
    if (openDialog && delete != null) {
        AlertDialog(
            icon = { Icon(Icons.Rounded.Warning, "") },
            title = {
                Text(stringResource(R.string.delete_confirm_title))
            },
            text = {
                Text(
                    "${stringResource(R.string.delete_confirm_message)} ${code.value}?",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            },
            onDismissRequest = { openDialog = false },
            confirmButton = {
                TextButton({
                    delete(code)
                    openDialog = false
                }) {
                    Text(stringResource(R.string.yes))
                }
            }, dismissButton = {
                TextButton(onClick = { openDialog = false }) {
                    Text(stringResource(R.string.no))
                }
            })
    }
}

@Preview
@Composable
fun CodeItemPreview() {
    CodeItem(Code(value = "123"))
}