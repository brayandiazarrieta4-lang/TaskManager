package com.example.taskmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.usecase.task.CreateTaskUseCase
import com.example.taskmanager.domain.usecase.task.DeleteTaskUseCase
import com.example.taskmanager.domain.usecase.task.GetTasksUseCase
import com.example.taskmanager.domain.usecase.task.UpdateTaskUseCase
import com.example.taskmanager.ui.state.TaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<TaskUiState>(
            TaskUiState.Loading
        )

    val uiState: StateFlow<TaskUiState> =
        _uiState.asStateFlow()

    private suspend fun loadTasks() {

        _uiState.value = TaskUiState.Loading

        val result = getTasksUseCase()

        result
            .onSuccess { tasks ->

                _uiState.value =
                    if (tasks.isEmpty()) {
                        TaskUiState.Empty
                    } else {
                        TaskUiState.Success(tasks)
                    }
            }
            .onFailure { exception ->

                _uiState.value =
                    TaskUiState.Error(
                        exception.message
                            ?: "Ocurrió un error"
                    )
            }
    }

    fun getTasks() {

        viewModelScope.launch {
            loadTasks()
        }
    }

    fun createTask(task: Task) {

        viewModelScope.launch {

            _uiState.value = TaskUiState.Loading

            val result = createTaskUseCase(task)

            result
                .onSuccess {
                    loadTasks()
                }
                .onFailure { exception ->

                    _uiState.value =
                        TaskUiState.Error(
                            exception.message
                                ?: "No se pudo crear la tarea"
                        )
                }
        }
    }

    fun updateTask(task: Task) {

        viewModelScope.launch {

            _uiState.value = TaskUiState.Loading

            val result = updateTaskUseCase(task)

            result
                .onSuccess {
                    loadTasks()
                }
                .onFailure { exception ->

                    _uiState.value =
                        TaskUiState.Error(
                            exception.message
                                ?: "No se pudo actualizar la tarea"
                        )
                }
        }
    }

    fun deleteTask(taskId: String) {

        viewModelScope.launch {

            _uiState.value = TaskUiState.Loading

            val result = deleteTaskUseCase(taskId)

            result
                .onSuccess {
                    loadTasks()
                }
                .onFailure { exception ->

                    _uiState.value =
                        TaskUiState.Error(
                            exception.message
                                ?: "No se pudo eliminar la tarea"
                        )
                }
        }
    }
}