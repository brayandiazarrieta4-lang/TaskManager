package com.example.taskmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.domain.model.TaskDraftModel
import com.example.taskmanager.domain.usecase.draft.DeleteDraftUseCase
import com.example.taskmanager.domain.usecase.draft.GetDraftsUseCase
import com.example.taskmanager.domain.usecase.draft.PublishDraftUseCase
import com.example.taskmanager.domain.usecase.draft.SaveDraftUseCase
import com.example.taskmanager.domain.usecase.draft.UpdateDraftUseCase
import com.example.taskmanager.ui.state.DraftUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DraftViewModel @Inject constructor(
    private val saveDraftUseCase: SaveDraftUseCase,
    private val getDraftsUseCase: GetDraftsUseCase,
    private val updateDraftUseCase: UpdateDraftUseCase,
    private val deleteDraftUseCase: DeleteDraftUseCase,
    private val publishDraftUseCase: PublishDraftUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DraftUiState>(
            DraftUiState.Loading
        )

    val uiState: StateFlow<DraftUiState> =
        _uiState.asStateFlow()

    private suspend fun loadDrafts() {

        _uiState.value =
            DraftUiState.Loading

        val result =
            getDraftsUseCase()

        result
            .onSuccess { drafts ->

                _uiState.value =
                    if (drafts.isEmpty()) {
                        DraftUiState.Empty
                    } else {
                        DraftUiState.Success(drafts)
                    }
            }
            .onFailure { exception ->

                _uiState.value =
                    DraftUiState.Error(
                        exception.message
                            ?: "No se pudieron cargar los borradores"
                    )
            }
    }

    fun getDrafts() {

        viewModelScope.launch {
            loadDrafts()
        }
    }

    fun saveDraft(draft: TaskDraftModel) {

        viewModelScope.launch {

            _uiState.value =
                DraftUiState.Loading

            val result =
                saveDraftUseCase(draft)

            result
                .onSuccess {
                    loadDrafts()
                }
                .onFailure { exception ->

                    _uiState.value =
                        DraftUiState.Error(
                            exception.message
                                ?: "No se pudo guardar el borrador"
                        )
                }
        }
    }

    fun updateDraft(draft: TaskDraftModel) {

        viewModelScope.launch {

            _uiState.value =
                DraftUiState.Loading

            val result =
                updateDraftUseCase(draft)

            result
                .onSuccess {
                    loadDrafts()
                }
                .onFailure { exception ->

                    _uiState.value =
                        DraftUiState.Error(
                            exception.message
                                ?: "No se pudo actualizar el borrador"
                        )
                }
        }
    }

    fun deleteDraft(draftId: Long) {

        viewModelScope.launch {

            _uiState.value =
                DraftUiState.Loading

            val result =
                deleteDraftUseCase(draftId)

            result
                .onSuccess {
                    loadDrafts()
                }
                .onFailure { exception ->

                    _uiState.value =
                        DraftUiState.Error(
                            exception.message
                                ?: "No se pudo eliminar el borrador"
                        )
                }
        }
    }

    fun publishDraft(draft: TaskDraftModel) {

        viewModelScope.launch {

            _uiState.value =
                DraftUiState.Loading

            val result =
                publishDraftUseCase(draft)

            result
                .onSuccess {
                    loadDrafts()
                }
                .onFailure { exception ->

                    _uiState.value =
                        DraftUiState.Error(
                            exception.message
                                ?: "No se pudo publicar el borrador"
                        )
                }
        }
    }
}