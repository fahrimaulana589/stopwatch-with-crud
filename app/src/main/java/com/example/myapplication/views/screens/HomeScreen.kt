package com.example.myapplication.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.example.myapplication.R
import com.example.myapplication.views.components.BoxMenu
import com.example.myapplication.views.components.BoxMenuState
import com.example.myapplication.views.components.TitleState
import com.example.myapplication.views.screens.destinations.GroupScreenDestination
import com.example.myapplication.views.screens.destinations.LapScreenDestination
import com.example.myapplication.views.screens.destinations.PersonScreenDestination
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    MyApplicationTheme(titleState = TitleState(title = "SOKI STOPWATCH"), content = {
        Spacer(modifier = Modifier.padding(top = 20.dp))
        Row() {
            val items = listOf<BoxMenuState>(
                BoxMenuState(
                    title = "Persons",
                    drawable = R.drawable.person,
                    onClick = {
                        navigator.navigate(PersonScreenDestination)
                    }
                ),
                BoxMenuState(
                    title = "Groups",
                    drawable = R.drawable.group,
                    onClick = { navigator.navigate(GroupScreenDestination) }
                ),
                BoxMenuState(
                    title = "Laps",
                    drawable = R.drawable.lap,
                    onClick = { navigator.navigate(LapScreenDestination) }
                ),
            );

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalItemSpacing = 10.dp
            ) {
                items(items) { item ->
                    BoxMenu(boxMenuState = item)
                }
            }
        }
    })
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHome() {
    val destinationsNavigator: DestinationsNavigator
    HomeScreen(navigator = EmptyDestinationsNavigator)
}