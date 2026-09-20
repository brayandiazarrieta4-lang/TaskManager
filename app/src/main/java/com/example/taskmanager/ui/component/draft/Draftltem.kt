package com.example.taskmanager.ui.component.draft

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.domain.model.TaskDraftModel

@Composable
fun DraftItem(
    draft: TaskDraftModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPublish: () -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {

        Text(text = draft.title)

        Text(text = draft.description)

        Row {

            Button(
                onClick = onEdit
            ) {
                Text("Editar")
            }

            Button(
                onClick = onDelete
            ) {
                Text("Eliminar")
            }

            Button(
                onClick = onPublish
            ) {
                Text("Publicar")
            }
        }
    }
}