package com.example.taskmanager.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.ui.component.task.TaskDialog
import com.example.taskmanager.ui.component.task.TaskItem
import com.example.taskmanager.ui.state.TaskUiState
import com.example.taskmanager.ui.viewmodel.TaskViewModel

@Composable
fun TaskScreen(
    viewModel: TaskViewModel,
    onLogout: () -> Unit,
    onOpenDrafts: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    var selectedTask by remember {
        mutableStateOf<Task?>(null)
    }

    var taskToDelete by remember {
        mutableStateOf<Task?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.getTasks()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedTask = null
                    showDialog = true
                }
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Button(
                onClick = onLogout
            ) {
                Text("Cerrar sesión")
            }
            Button(
                onClick = onOpenDrafts
            ) {
                Text("Borradores")
            }

            when (val state = uiState) {

                TaskUiState.Loading -> {
                    CircularProgressIndicator()
                }

                TaskUiState.Empty -> {
                    Text("No hay tareas")
                }

                is TaskUiState.Success -> {

                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.tasks) { task ->

                            TaskItem(
                                task = task,

                                onEdit = {
                                    selectedTask = task
                                    showDialog = true
                                },

                                onDelete = {
                                    taskToDelete = task
                                }
                            )
                        }
                    }
                }

                is TaskUiState.Error -> {
                    Text(state.message)
                }
            }
        }
    }

    if (showDialog) {

        TaskDialog(
            task = selectedTask,

            onDismiss = {
                showDialog = false
                selectedTask = null
            },

            onConfirm = { title, description ->

                if (selectedTask == null) {

                    val newTask = Task(
                        id = "",
                        ownerId = "",
                        title = title,
                        description = description,
                        completed = false,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )

                    viewModel.createTask(newTask)

                } else {

                    val updatedTask = selectedTask!!.copy(
                        title = title,
                        description = description,
                        updatedAt = System.currentTimeMillis()
                    )

                    viewModel.updateTask(updatedTask)
                }

                showDialog = false
                selectedTask = null
            }
        )
    }

    if (taskToDelete != null) {

        AlertDialog(
            onDismissRequest = {
                taskToDelete = null
            },

            title = {
                Text("Eliminar tarea")
            },

            text = {
                Text("¿Seguro que quieres eliminar esta tarea?")
            },

            confirmButton = {

                Button(
                    onClick = {

                        viewModel.deleteTask(
                            taskToDelete!!.id
                        )

                        taskToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        taskToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}