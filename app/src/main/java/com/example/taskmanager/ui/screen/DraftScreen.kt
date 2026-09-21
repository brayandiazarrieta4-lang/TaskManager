package com.example.taskmanager.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanager.domain.model.TaskDraftModel
import com.example.taskmanager.ui.component.draft.DraftDialog
import com.example.taskmanager.ui.component.draft.DraftItem
import com.example.taskmanager.ui.state.DraftUiState
import com.example.taskmanager.ui.viewmodel.DraftViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftScreen(
    viewModel: DraftViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedDraft by remember { mutableStateOf<TaskDraftModel?>(null) }
    var draftToDelete by remember { mutableStateOf<TaskDraftModel?>(null) }
    var draftToPublish by remember { mutableStateOf<TaskDraftModel?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getDrafts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Borradores",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedDraft = null
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo borrador")
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                DraftUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                DraftUiState.Empty -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No hay borradores",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Guarda ideas para publicarlas después",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is DraftUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
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
                    Text(
                        state.message,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

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
                    val updatedDraft = selectedDraft!!.copy(
                        title = title,
                        description = description,
                        savedAt = System.currentTimeMillis()
                    )
                    viewModel.updateDraft(updatedDraft)
                }
                showDialog = false
                selectedDraft = null
            }
        )
    }

    if (draftToDelete != null) {
        AlertDialog(
            onDismissRequest = { draftToDelete = null },
            title = { Text("Eliminar borrador") },
            text = { Text("¿Seguro que quieres eliminar este borrador?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteDraft(draftToDelete!!.id)
                        draftToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { draftToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (draftToPublish != null) {
        AlertDialog(
            onDismissRequest = { draftToPublish = null },
            title = { Text("Publicar borrador") },
            text = { Text("¿Quieres publicar este borrador?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.publishDraft(draftToPublish!!)
                        draftToPublish = null
                    }
                ) {
                    Text("Publicar")
                }
            },
            dismissButton = {
                Button(onClick = { draftToPublish = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
