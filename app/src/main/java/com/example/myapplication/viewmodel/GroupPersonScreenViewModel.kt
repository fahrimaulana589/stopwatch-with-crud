package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.repository.GroupRepository
import com.example.myapplication.data.repository.PersonRepository
import com.example.myapplication.ui.theme.AplicationState
import com.example.myapplication.views.components.CardLinkState
import com.example.myapplication.views.components.CardState
import com.example.myapplication.views.components.OptionPerson
import com.example.myapplication.views.components.StatePersonOption
import com.example.myapplication.views.components.StateSearchMenu
import com.example.myapplication.views.components.SubTitleState
import com.example.myapplication.views.components.TitleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class GroupPersonScreenViewModel(private val groupRepository: GroupRepository,private val personRepository: PersonRepository) : ViewModel() {
    private val _aplicationState = MutableStateFlow(AplicationState())
    val aplicationState = _aplicationState.asStateFlow()

    private val _titleState = MutableStateFlow(
        TitleState(
            title = "Group",
            isBack = true
        ),
    )
    val titleState = _titleState.asStateFlow()

    private val _subTitleState = MutableStateFlow(
        SubTitleState(title = "Group", drawable = R.drawable.group)
    )
    val subTitleState = _subTitleState.asStateFlow()

    private var _group = GroupDto()

    private val _optionPerson = MutableStateFlow(
        StatePersonOption(
            optionPerson = OptionPerson.ListPerson,
            onAllPersonClick = {
                Log.e("Grorup person", "setOtionPerson: "+OptionPerson.AllPerson )
                setOtionPerson(OptionPerson.AllPerson)
                getAllPersonNotInGroup()
            },
            onListPersonClick = {
                Log.e("Grorup person", "setOtionPerson: "+OptionPerson.AllPerson )
                setOtionPerson(OptionPerson.ListPerson)
                getAllPersonInGroup()
            }
        )
    )
    val optionPerson = _optionPerson.asStateFlow()

    private val _allPersons = MutableStateFlow<List<CardState>>(emptyList())
    val allPersons = _allPersons.asStateFlow()

    private val _listPersons = MutableStateFlow<List<CardState>>(emptyList())
    val listPersons = _listPersons.asStateFlow()

    private val _searchListPerson = MutableStateFlow(
        StateSearchMenu(
            search = "",
            onSearchChange = {
                onSearchListPersonChange(it)
                getAllPersonNotInGroup()
            }
        )
    )
    val searchListPerson = _searchListPerson.asStateFlow()

    private val _searchAllPerson = MutableStateFlow(
        StateSearchMenu(
            search = "",
            onSearchChange = {
                onSearchAllPersonChange(it)
                getAllPersonInGroup()
            }
        )
    )
    val searchAllPerson = _searchAllPerson.asStateFlow()

    fun setGroup(groupUid: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _group = groupRepository.findByUid(groupUid)!!
            _subTitleState.value = _subTitleState.value.copy(
                title = _group.name
            )
            _optionPerson.value = _optionPerson.value.copy(
                optionPerson = OptionPerson.ListPerson
            )
            getAllPersonInGroup()
            getAllPersonNotInGroup()
        }
    }

    private fun getAllPersonInGroup(){
        CoroutineScope(Dispatchers.IO).launch {
            var count = 0
            _listPersons.value = personRepository.getAllPersonInGroup(_group.group_id,_searchListPerson.value.search).map {
                CardState(
                    id = it.person_id!!,
                    nomer = ++count,
                    name = it.name!!,
                    action = CardLinkState(drawable = R.drawable.delete){
                        deletePerson(_group.group_id,it.person_id)
                    }
                )
            }
        }
    }

    private fun getAllPersonNotInGroup(){
        CoroutineScope(Dispatchers.IO).launch {
            var count = 0
            _allPersons.value = personRepository.getAllPersonNotInGroup(_group.group_id,_searchAllPerson.value.search).map {
                CardState(
                    id = it.person_id!!,
                    nomer = ++count,
                    name = it.name!!,
                    action = CardLinkState(drawable = R.drawable.plus){
                        addPerson(_group.group_id,it.person_id)
                    }
                )
            }
        }
    }

    private fun setOtionPerson(optionPerson: OptionPerson) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("Grorup person", "setOtionPerson: "+_optionPerson.value, )
            _optionPerson.value = _optionPerson.value.copy(
                optionPerson = optionPerson
            )
        }
    }

    private fun onSearchAllPersonChange(search: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _searchAllPerson.value = _searchAllPerson.value.copy(
                search = search
            )
            getAllPersonNotInGroup()
        }
    }

    private fun onSearchListPersonChange(search: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _searchListPerson.value = _searchListPerson.value.copy(
                search = search
            )
            getAllPersonInGroup()
        }
    }

    private fun addPerson(group_id:Int,person_id :Int){
        CoroutineScope(Dispatchers.IO).launch {
            groupRepository.addPersonToGroup(group_id,person_id)
            getAllPersonInGroup()
            getAllPersonNotInGroup()
        }
    }

    private fun deletePerson(group_id:Int,person_id :Int){
        CoroutineScope(Dispatchers.IO).launch {
            groupRepository.deletePersonInGroup(group_id,person_id)
            getAllPersonInGroup()
            getAllPersonNotInGroup()
        }
    }
}