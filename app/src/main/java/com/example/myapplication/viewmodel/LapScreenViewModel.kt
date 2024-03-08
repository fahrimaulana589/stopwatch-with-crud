package com.example.myapplication.viewmodel

import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.LapDto
import com.example.myapplication.data.entity.GroupEntity
import com.example.myapplication.data.repository.GroupRepository
import com.example.myapplication.data.repository.LapRepository
import com.example.myapplication.data.repository.ValidationState
import com.example.myapplication.ui.theme.AplicationState
import com.example.myapplication.views.components.CardLinkState
import com.example.myapplication.views.components.CardState
import com.example.myapplication.views.components.ModalAddEditLapType
import com.example.myapplication.views.components.StateModalAddEditLap
import com.example.myapplication.views.components.StateModalDelete
import com.example.myapplication.views.components.SubTitleState
import com.example.myapplication.views.components.TitleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LapScreenViewModel(
    private val lapRepository: LapRepository,
    private val groupRepository: GroupRepository
) : ViewModel() {
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
        SubTitleState(title = "Lap", drawable = R.drawable.lap)
    )
    val subTitleState = _subTitleState.asStateFlow()

    private val _modalAddedit = MutableStateFlow(false)
    val modalAddEdit = _modalAddedit.asStateFlow()

    private val _formAddedit = MutableStateFlow(
        StateModalAddEditLap(
            lap = LapDto(),
            isNameEror = false,
            errorNameMessage = "",
            openGroupSelected = false,
            type = ModalAddEditLapType.Create,
            nameOnCahnge = { name -> onChangeNameGroup(name) },
            dateOnCahnge = { date -> onChangeDateGroup(date) },
            closeOnClick = { closeModalModalEditForm() },
            addOnClick = { addLap() },
            editOnClick = { updateGroup() },
            delateOnClick = { popUpModalDelete() },
            openSelectGroup = { openSelectGroup() },
            closeSelectGroup = { closeSelectGroup() },
            selectGroup = { selectGroup(it) }
        )
    )

    val formAddedit = _formAddedit.asStateFlow()

    private val _modalDelete = MutableStateFlow(false)
    val modalDelete = _modalDelete.asStateFlow()

    private val _confirmDelete = MutableStateFlow(StateModalDelete(
        message = "delate ?",
        closeOnClick = { closeModalDelete() },
        removeOnClick = { deleteLap() }
    ))
    val confirmDelete = _confirmDelete.asStateFlow()

    private val _laps = MutableStateFlow<List<CardState>>(emptyList())
    val laps = _laps.asStateFlow()

    init {
        getAllLap()
    }

    fun getAllLap() {
        CoroutineScope(Dispatchers.IO).launch {
            var count = 0
            _laps.value = lapRepository.getAll().map {
                CardState(
                    id = it.lap_id,
                    nomer = ++count,
                    name = it.name,
                    onClick = {
                        popUpModalEditForm(it)
                    }
                )
            }
        }
    }

    fun addLap() {
        _formAddedit.value = _formAddedit.value.copy(
            isNameEror = false,
            errorNameMessage = "",
            isDateEror = false,
            errorDateMessage = ""
        )
        CoroutineScope(Dispatchers.IO).launch {
            lapRepository.add(_formAddedit.value.lap,_formAddedit.value.groupSelected, ValidationState(
                onSuccess = {
                    getAllLap()
                    closeModalModalEditForm()
                },
                onFailure = {
                    Log.e("Why", "addLap: "+it, )
                    for ((message, path) in it) {
                        when {
                            path.toString().contains("[name]") -> {
                                _formAddedit.value = _formAddedit.value.copy(
                                    isNameEror = true,
                                    errorNameMessage = _formAddedit.value.errorNameMessage + ", " + message
                                )
                            }

                            path.toString().contains("[date]") -> {
                                _formAddedit.value = _formAddedit.value.copy(
                                    isDateEror = true,
                                    errorDateMessage = _formAddedit.value.errorDateMessage + ", " + message
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
            lapRepository.update(_formAddedit.value.lap,_formAddedit.value.groupSelected, ValidationState(
                onSuccess = {
                    getAllLap()
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

    fun deleteLap() {
        CoroutineScope(Dispatchers.IO).launch {
            lapRepository.delete(_formAddedit.value.lap)
            closeModalDelete()
            getAllLap()
        }
    }

    fun popUpModalCreateForm() {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                lap = LapDto(),
                type = ModalAddEditLapType.Create,
                errorNameMessage = "",
                errorDateMessage = "",
                errorGroupMessage = "",
                isNameEror = false,
                isGroupEror = false,
                isDateEror = false,
                groups = groupRepository.getAll(),
                groupSelected = emptyList()
            )
            _modalAddedit.value = true
            _aplicationState.value = _aplicationState.value.copy(blur = 8.dp)
        }
    }

    fun popUpModalEditForm(lapDto: LapDto) {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                lap = lapDto,
                type = ModalAddEditLapType.Edit,
                errorNameMessage = "",
                errorDateMessage = "",
                errorGroupMessage = "",
                isNameEror = false,
                isGroupEror = false,
                isDateEror = false,
                groups = groupRepository.getAll().filter {
                    !lapRepository.findByUidWithGroup(lapDto.lap_id).groups.contains(GroupEntity(it.group_id,it.name))
                },
                groupSelected = lapRepository.findByUidWithGroup(lapDto.lap_id).groups.map {
                    GroupDto(it.group_id!!,it.name)
                }
            )
            _modalAddedit.value = true
            _aplicationState.value = _aplicationState.value.copy(blur = 8.dp)
        }
    }

    private fun openSelectGroup() {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                openGroupSelected = true,
            )
        }
    }

    private fun closeSelectGroup() {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                openGroupSelected = false,
            )
        }
    }

    private fun selectGroup(groupDto: GroupDto) {
        CoroutineScope(Dispatchers.IO).launch {
            when {
                _formAddedit.value.groups.contains(groupDto) -> {
                    _formAddedit.value = _formAddedit.value.copy(
                        groupSelected = _formAddedit.value.groupSelected.plus(groupDto),
                        groups = _formAddedit.value.groups.filter {
                            it.group_id != groupDto.group_id
                        }
                    )
                }

                _formAddedit.value.groupSelected.contains(groupDto) -> {
                    _formAddedit.value = _formAddedit.value.copy(
                        groups = _formAddedit.value.groups.plus(groupDto),
                        groupSelected = _formAddedit.value.groupSelected.filter {
                            it.group_id != groupDto.group_id
                        }
                    )
                }
            }
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
                lap = _formAddedit.value.lap.copy(name = name)
            )
        }
    }

    private fun onChangeDateGroup(date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                lap = _formAddedit.value.lap.copy(date = date)
            )
        }
    }
}