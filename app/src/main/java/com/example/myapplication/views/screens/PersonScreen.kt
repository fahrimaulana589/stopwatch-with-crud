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
import com.example.myapplication.views.components.CardState
import com.example.myapplication.views.components.Fab
import com.example.myapplication.views.components.TitleState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AplicationState
import com.example.myapplication.viewmodel.PersonScreenViewModel
import com.example.myapplication.views.components.CardList
import com.example.myapplication.views.components.ModalAddEditPerson
import com.example.myapplication.views.components.ModalDelete
import com.example.myapplication.views.components.StateModalAddEditPerson
import com.example.myapplication.views.components.StateModalDelete
import com.example.myapplication.views.components.SubTitle
import com.example.myapplication.views.components.SubTitleState
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun PersonScreen(
    navigator: DestinationsNavigator, personViewmodel: PersonScreenViewModel = koinViewModel()
) {

    val aplicationState by personViewmodel.aplicationState.collectAsState()
    val titleState by personViewmodel.titleState.collectAsState()
    val subTitleState by personViewmodel.subTitleState.collectAsState()
    val modelAddEdit by personViewmodel.modalAddEdit.collectAsState()
    val formAddEdit by personViewmodel.formAddedit.collectAsState()
    val modelDelte by personViewmodel.modalDelete.collectAsState()
    val confirmlDelete by personViewmodel.confirmDelete.collectAsState()
    val persons by personViewmodel.persons.collectAsState()

    MyApplicationTheme(
        titleState = titleState.copy() { navigator.popBackStack() },
        fab = { Fab(action = { personViewmodel.popUpModalCreateForm() }) },
        aplicationState = aplicationState
    ) {
        SubTitle(subTitleState = subTitleState)

        Spacer(modifier = Modifier.height(40.dp))

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalItemSpacing = 10.dp
        ) {
            items( persons) { it ->
                CardList(cardState = it)
            }
        }

        if (modelAddEdit) {
            ModalAddEditPerson(
                stateModalAddEditPerson = formAddEdit
            )
        }

        if (modelDelte) {
            ModalDelete(
                stateModalDelete = confirmlDelete
            )
        }
    }
}

@Composable
@Preview
fun PreviewPerson() {
    PersonScreen(navigator = EmptyDestinationsNavigator)
}
