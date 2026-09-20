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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.domain.model.TaskDraftModel
import com.example.taskmanager.ui.component.draft.DraftDialog
import com.example.taskmanager.ui.component.draft.DraftItem
import com.example.taskmanager.ui.state.DraftUiState
import com.example.taskmanager.ui.viewmodel.DraftViewModel

@Composable
fun DraftScreen(
    viewModel: DraftViewModel,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    var selectedDraft by remember {
        mutableStateOf<TaskDraftModel?>(null)
    }

    var draftToDelete by remember {
        mutableStateOf<TaskDraftModel?>(null)
    }

    var draftToPublish by remember {
        mutableStateOf<TaskDraftModel?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.getDrafts()
    }

    Scaffold(

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    selectedDraft = null
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
                onClick = onBack
            ) {
                Text("Volver")
            }

            when (val state = uiState) {

                DraftUiState.Loading -> {
                    CircularProgressIndicator()
                }

                DraftUiState.Empty -> {
                    Text("No hay borradores")
                }

                is DraftUiState.Success -> {

                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        items(state.drafts) { draft ->

                            DraftItem(
                                draft = draft,

                                onEdit = {
                                    selectedDraft = draft
                                    showDialog = true
                                },

                                onDelete = {
                                    draftToDelete = draft
                                },

                                onPublish = {
                                    draftToPublish = draft
                                }
                            )
                        }
                    }
                }

                is DraftUiState.Error -> {
                    Text(state.message)
                }
            }
        }
    }

    // Crear / editar

    if (showDialog) {

        DraftDialog(
            draft = selectedDraft,

            onDismiss = {
                showDialog = false
                selectedDraft = null
            },

            onConfirm = { title, description ->

                if (selectedDraft == null) {

                    val newDraft = TaskDraftModel(
                        id = 0,
                        ownerId = "",
                        title = title,
                        description = description,
                        savedAt = System.currentTimeMillis()
                    )

                    viewModel.saveDraft(newDraft)

                } else {

                    val updatedDraft =
                        selectedDraft!!.copy(
                            title = title,
                            description = description,
                            savedAt = System.currentTimeMillis()
                        )

                    viewModel.updateDraft(
                        updatedDraft
                    )
                }

                showDialog = false
                selectedDraft = null
            }
        )
    }

    // Confirmación de eliminación

    if (draftToDelete != null) {

        AlertDialog(

            onDismissRequest = {
                draftToDelete = null
            },

            title = {
                Text("Eliminar borrador")
            },

            text = {
                Text(
                    "¿Seguro que quieres eliminar este borrador?"
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        viewModel.deleteDraft(
                            draftToDelete!!.id
                        )

                        draftToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        draftToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Confirmación de publicación

    if (draftToPublish != null) {

        AlertDialog(

            onDismissRequest = {
                draftToPublish = null
            },

            title = {
                Text("Publicar borrador")
            },

            text = {
                Text(
                    "¿Quieres publicar este borrador?"
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        viewModel.publishDraft(
                            draftToPublish!!
                        )

                        draftToPublish = null
                    }
                ) {
                    Text("Publicar")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        draftToPublish = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}