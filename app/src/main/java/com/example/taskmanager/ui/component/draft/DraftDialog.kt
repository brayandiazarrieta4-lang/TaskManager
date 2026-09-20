package com.example.taskmanager.ui.component.draft

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.taskmanager.domain.model.TaskDraftModel

@Composable
fun DraftDialog(
    draft: TaskDraftModel? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {

    var title by remember(draft) {
        mutableStateOf(
            draft?.title ?: ""
        )
    }

    var description by remember(draft) {
        mutableStateOf(
            draft?.description ?: ""
        )
    }

    val isEditing = draft != null

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text(
                if (isEditing) {
                    "Editar borrador"
                } else {
                    "Nuevo borrador"
                }
            )
        },

        text = {

            Column {

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("Título")
                    }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("Descripción")
                    }
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {
                    onConfirm(
                        title,
                        description
                    )
                }
            ) {

                Text(
                    if (isEditing) {
                        "Guardar"
                    } else {
                        "Guardar borrador"
                    }
                )
            }
        },

        dismissButton = {

            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}