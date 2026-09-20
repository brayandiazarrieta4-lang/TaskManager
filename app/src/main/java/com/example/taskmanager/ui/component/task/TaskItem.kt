package com.example.taskmanager.ui.component.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.domain.model.Task

@Composable
fun TaskItem(
    task: Task,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Column(
        modifier = Modifier.padding(8.dp)
    ) {

        Text(
            text = task.title
        )

        Text(
            text = task.description
        )

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
        }
    }
}