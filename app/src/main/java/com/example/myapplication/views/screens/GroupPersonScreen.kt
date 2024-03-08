package com.example.myapplication.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.myapplication.R
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.PersonDto
import com.example.myapplication.viewmodel.GroupPersonScreenViewModel
import com.example.myapplication.views.components.CardLinkState
import com.example.myapplication.views.components.CardList
import com.example.myapplication.views.components.OptionPerson
import com.example.myapplication.views.components.PersonOption
import com.example.myapplication.views.components.SearchMenu
import com.example.myapplication.views.components.StatePersonOption
import com.example.myapplication.views.components.SubTitle
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun GroupPersonScreen(
    groupUid: Int,
    navigator: DestinationsNavigator,
    groupPersonViewmodel: GroupPersonScreenViewModel = koinViewModel()
) {

    val aplicationState by groupPersonViewmodel.aplicationState.collectAsState()
    val titleState by groupPersonViewmodel.titleState.collectAsState()
    val subTitleState by groupPersonViewmodel.subTitleState.collectAsState()
    val statePersonOption by groupPersonViewmodel.optionPerson.collectAsState()
    val stateSearcListPerson by groupPersonViewmodel.searchListPerson.collectAsState()
    val stateSearcAllPerson by groupPersonViewmodel.searchAllPerson.collectAsState()
    val allPersons by groupPersonViewmodel.allPersons.collectAsState()
    val listPersons by groupPersonViewmodel.listPersons.collectAsState()

    groupPersonViewmodel.setGroup(groupUid)

    MyApplicationTheme(
        titleState = titleState.copy { navigator.popBackStack() },
        aplicationState = aplicationState
    ) {
        SubTitle(subTitleState = subTitleState)
        Spacer(modifier = Modifier.height(15.dp))
        PersonOption(statePersonOption = statePersonOption)
        Spacer(modifier = Modifier.height(15.dp))
        if (statePersonOption.optionPerson == OptionPerson.ListPerson){
            SearchMenu(stateSearchMenu = stateSearcListPerson)
            Spacer(modifier = Modifier.height(40.dp))
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(1),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalItemSpacing = 10.dp
            ) {
                items( listPersons) { it ->
                    CardList(cardState = it)
                }
            }
        }

        if (statePersonOption.optionPerson == OptionPerson.AllPerson){
            SearchMenu(stateSearchMenu = stateSearcAllPerson)
            Spacer(modifier = Modifier.height(40.dp))
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(1),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalItemSpacing = 10.dp
            ) {
                items( allPersons) { it ->
                    CardList(cardState = it)
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewGroupPerson() {
    GroupPersonScreen(groupUid = 1,navigator = EmptyDestinationsNavigator)
}
