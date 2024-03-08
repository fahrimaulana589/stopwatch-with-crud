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
import com.example.myapplication.views.components.Fab
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.myapplication.R
import com.example.myapplication.viewmodel.GroupScreenViewModel
import com.example.myapplication.views.components.CardLinkState
import com.example.myapplication.views.components.CardList
import com.example.myapplication.views.components.ModalAddEditGroup
import com.example.myapplication.views.components.ModalDelete
import com.example.myapplication.views.components.SubTitle
import com.example.myapplication.views.screens.destinations.GroupPersonScreenDestination
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun GroupScreen(
    navigator: DestinationsNavigator, groupViewmodel: GroupScreenViewModel = koinViewModel()
) {

    val aplicationState by groupViewmodel.aplicationState.collectAsState()
    val titleState by groupViewmodel.titleState.collectAsState()
    val subTitleState by groupViewmodel.subTitleState.collectAsState()
    val modelAddEdit by groupViewmodel.modalAddEdit.collectAsState()
    val formAddEdit by groupViewmodel.formAddedit.collectAsState()
    val modelDelte by groupViewmodel.modalDelete.collectAsState()
    val confirmlDelete by groupViewmodel.confirmDelete.collectAsState()
    val groups by groupViewmodel.groups.collectAsState()

    MyApplicationTheme(
        titleState = titleState.copy { navigator.popBackStack() },
        fab = { Fab(action = { groupViewmodel.popUpModalCreateForm() }) },
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
            items( groups) { it ->
                CardList(cardState = it.copy(
                    action = CardLinkState(
                        drawable = R.drawable.person,
                        action = {
                            navigator.navigate(GroupPersonScreenDestination(it.id))
                            }
                    ),
                ))
            }
        }

        if (modelAddEdit) {
            ModalAddEditGroup(
                stateModalAddEditGroup = formAddEdit
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
fun PreviewGroup() {
    GroupScreen(navigator = EmptyDestinationsNavigator)
}
