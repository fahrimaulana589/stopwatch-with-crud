package com.example.myapplication.viewmodel

import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.dto.PersonDto
import com.example.myapplication.data.repository.PersonRepository
import com.example.myapplication.data.repository.ValidationState
import com.example.myapplication.ui.theme.AplicationState
import com.example.myapplication.views.components.CardState
import com.example.myapplication.views.components.ModalAddEditPersonType
import com.example.myapplication.views.components.StateModalAddEditPerson
import com.example.myapplication.views.components.StateModalDelete
import com.example.myapplication.views.components.SubTitleState
import com.example.myapplication.views.components.TitleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonScreenViewModel(private val personRepository: PersonRepository) : ViewModel() {
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
        SubTitleState(title = "Person", drawable = R.drawable.person)
    )
    val subTitleState = _subTitleState.asStateFlow()

    private val _modalAddedit = MutableStateFlow(false)
    val modalAddEdit = _modalAddedit.asStateFlow()

    private val _formAddedit = MutableStateFlow(
        StateModalAddEditPerson(
            person = PersonDto(),
            isNameEror = false,
            errorNameMessage = "",
            type = ModalAddEditPersonType.Create,
            nameOnCahnge = { name -> onChangeNamePerson(name) },
            closeOnClick = { closeModalModalEditForm() },
            addOnClick = { addPerson() },
            editOnClick = { updatePerson() },
            delateOnClick = { popUpModalDelete() },
        )
    )
    val formAddedit = _formAddedit.asStateFlow()

    private val _modalDelete = MutableStateFlow(false)
    val modalDelete = _modalDelete.asStateFlow()

    private val _confirmDelete = MutableStateFlow(StateModalDelete(
        message = "delate ?",
        closeOnClick = { closeModalDelete() },
        removeOnClick = { deletePerson() }
    ))
    val confirmDelete = _confirmDelete.asStateFlow()

    private val _persons = MutableStateFlow<List<CardState>>(emptyList())
    val persons = _persons.asStateFlow()

    init {
        getAllPerson()
    }

    fun getAllPerson() {
        CoroutineScope(Dispatchers.IO).launch {
            var count = 0
            _persons.value = personRepository.getAll().map {
                CardState(
                    id = it.person_id,
                    nomer = ++count,
                    name = it.name,
                    onClick = {
                        popUpModalEditForm(it)
                    }
                )
            }
            Log.e("person", "getAllPerson: "+persons.value, )
        }
    }

    fun addPerson() {
        _formAddedit.value = _formAddedit.value.copy(
            isNameEror = false,
            errorNameMessage = ""
        )
        CoroutineScope(Dispatchers.IO).launch {
            personRepository.add(_formAddedit.value.person, ValidationState(
                onSuccess = {
                    getAllPerson()
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

    fun updatePerson() {
        _formAddedit.value = _formAddedit.value.copy(
            isNameEror = false,
            errorNameMessage = ""
        )
        CoroutineScope(Dispatchers.IO).launch {
            personRepository.update(_formAddedit.value.person, ValidationState(
                onSuccess = {
                    getAllPerson()
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

    fun deletePerson() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("person", "deletePerson: ", )
            personRepository.delete(_formAddedit.value.person,ValidationState(
                onSuccess = {
                    closeModalDelete()
                    getAllPerson()
                },
                onFailure = {
                    for ((message, path) in it) {
                        when {
                            path.toString().contains("[person_id]") -> {
                                Log.e("person", "deletePersonError: "+message, )
                            }
                        }
                    }
                }
            ))
        }
    }

    fun popUpModalCreateForm() {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                person = PersonDto(),
                errorNameMessage = "",
                isNameEror = false,
                type = ModalAddEditPersonType.Create
            )
            _modalAddedit.value = true
            _aplicationState.value = _aplicationState.value.copy(blur = 8.dp)
        }
    }

    fun popUpModalEditForm(personDto: PersonDto) {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                person = personDto,
                errorNameMessage = "",
                isNameEror = false,
                type = ModalAddEditPersonType.Edit
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

    fun onChangeNamePerson(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _formAddedit.value = _formAddedit.value.copy(
                person = _formAddedit.value.person.copy(name = name)
            )
        }
    }
}