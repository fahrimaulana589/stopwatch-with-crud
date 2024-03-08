package com.example.myapplication.viewmodel

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.repository.GroupRepository
import com.example.myapplication.data.repository.ValidationState
import com.example.myapplication.ui.theme.AplicationState
import com.example.myapplication.views.components.CardState
import com.example.myapplication.views.components.ModalAddEditGroupType
import com.example.myapplication.views.components.StateModalAddEditGroup
import com.example.myapplication.views.components.StateModalDelete
import com.example.myapplication.views.components.SubTitleState
import com.example.myapplication.views.components.TitleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupScreenViewModel(private val groupRepository: GroupRepository) : ViewModel() {
    private val _aplicationState = MutableStateFlow(AplicationState())
    val aplicationState = _aplicationState.asStateFlow()

    private val _titleState = MutableStateFlow(
        TitleState(
            title = "SOKI STOPWATCH",
            isBack = true
        ),
    )
    val titleState = _titleState.asStateFlow()

    private val _subTitleState = MutableStateFlow(
        SubTitleState(title = "Group", drawable = R.drawable.group)
    )
    val subTitleState = _subTitleState.asStateFlow()

    private val _modalAddedit = MutableStateFlow(false)
    val modalAddEdit = _modalAddedit.asStateFlow()

    private val _formAddedit = MutableStateFlow(
        StateModalAddEditGroup(
            group = GroupDto(),
            isNameEror = false,
            errorNameMessage = "",
            type = ModalAddEditGroupType.Create,
            nameOnCahnge = { name -> onChangeNameGroup(name) },
            closeOnClick = { closeModalModalEditForm() },
            addOnClick = { addGroup() },
            editOnClick = { updateGroup() },
            delateOnClick = { popUpModalDelete() },
        )
    )
    val formAddedit = _formAddedit.asStateFlow()

    private val _modalDelete = MutableStateFlow(false)
    val modalDelete = _modalDelete.asStateFlow()

    private val _confirmDelete = MutableStateFlow(StateModalDelete(
        message = "delate ?",
        closeOnClick = { closeModalDelete() },
        removeOnClick = { deleteGroup() }
    ))
    val confirmDelete = _confirmDelete.asStateFlow()

    private val _groups = MutableStateFlow<List<CardState>>(emptyList())
    val groups = _groups.asStateFlow()

    init {
        getAllGroup()
    }

    fun getAllGroup() {
        CoroutineScope(Dispatchers.IO).launch {
            var count = 0
            _groups.value = groupRepository.getAll().map {
                CardState(
                    id = it.group_id,
                    nomer = ++count,
                    name = it.name,
                    onClick = {
                        popUpModalEditForm(it)
                    }
                )
            }
        }
    }

    fun addGroup() {
        _formAddedit.value = _formAddedit.value.copy(
            isNameEror = false,
            errorNameMessage = ""
        )
        CoroutineScope(Dispatchers.IO).launch {
            groupRepository.add(_formAddedit.value.group, ValidationState(
                onSuccess = {
                    getAllGroup()
                    closeModalModalEditForm()
                },
                onFailure = {
                    for ((message, path) in it) {
                        when {
                            path.toString().contains("[name]") -> {
                                _formAddedit.value = _formAddedit.value.copy(
                                    isNameEror = true,
                                    errorNameMessage = _formAddedit.value.errorNameMessage + ", " + message
                                )
                            }
                        }
                    }
                }
            ))
        }
    }

    fun updateGroup() {
        _formAddedit.value = _formAddedit.value.copy(
            isNameEror = false,
            errorNameMessage = ""
        )
        CoroutineScope(Dispatchers.IO).launch {
            groupRepository.update(_formAddedit.value.group, ValidationState(
                onSuccess = {
                    getAllGroup()
                    closeModalModalEditForm()
                },
                onFailure = {
                    for ((message, path) in it) {
                        when {
                            path.toString().contains("[name]") -> {
                                _formAddedit.value = _formAddedit.value.copy(
                                    isNameEror = true,
                                    errorNameMessage = _formAddedit.value.errorNameMessage + ", " + message
                                )
                            }
                        }
                    }
                }
            ))
        }
    }

    fun deleteGroup() {
        CoroutineScope(Dispatchers.IO).launch {
            groupRepository.delete(_formAddedit.value.group, ValidationState(
                onSuccess = {
                    getAllGroup()
                    closeModalDelete()
                },
                onFailure = {
                    var error = ""
                    for ((message, path) in it) {
                        when {
                            path.toString().contains("[group_id]") -> {
                                error += "$message, "
                            }
                        }
                    }
                    _confirmDelete.value = _confirmDelete.value.copy(
                        message = "Error : $error"
                    )
                }
            ))
        }
    }

    fun popUpModalCreateForm() {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                group = GroupDto(),
                errorNameMessage = "",
                isNameEror = false,
                type = ModalAddEditGroupType.Create,
            )
            _modalAddedit.value = true
            _aplicationState.value = _aplicationState.value.copy(blur = 8.dp)
        }
    }

    fun popUpModalEditForm(groupDto: GroupDto) {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                group = groupDto,
                errorNameMessage = "",
                isNameEror = false,
                type = ModalAddEditGroupType.Edit,
            )
            _modalAddedit.value = true
            _aplicationState.value = _aplicationState.value.copy(blur = 8.dp)
        }
    }

    fun popUpModalDelete() {
        CoroutineScope(Dispatchers.IO).launch {
            _modalAddedit.value = false
            _modalDelete.value = true
        }
    }

    fun closeModalModalEditForm() {
        CoroutineScope(Dispatchers.IO).launch {
            _modalAddedit.value = false
            _aplicationState.value = _aplicationState.value.copy(blur = 0.dp)
        }
    }

    fun closeModalDelete() {
        CoroutineScope(Dispatchers.IO).launch {
            _modalDelete.value = false
            _aplicationState.value = _aplicationState.value.copy(blur = 0.dp)
        }
    }

    fun onChangeNameGroup(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                group = _formAddedit.value.group.copy(name = name)
            )
        }
    }
}