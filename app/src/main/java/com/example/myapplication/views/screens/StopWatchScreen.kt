package com.example.myapplication.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.datastore.TextStopwatch
import com.example.myapplication.di.appModule
import com.example.myapplication.ui.theme.AplicationState
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.viewmodel.StopwatchScreenViewModel
import com.example.myapplication.views.components.CardList
import com.example.myapplication.views.components.DragPlayBox
import com.example.myapplication.views.components.RecordList
import com.example.myapplication.views.components.TimeStopwatch
import com.example.myapplication.views.components.Title
import com.example.myapplication.views.components.TitleState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication

@Destination
@Composable
fun StopWatchScreen(
    navigator: DestinationsNavigator,
    lap_id: Int,
    stopwatchScreenViewModel: StopwatchScreenViewModel = koinViewModel()
) {
    val aplicationState by stopwatchScreenViewModel.aplicationState.collectAsState()
    val titleState by stopwatchScreenViewModel.titleState.collectAsState()
    val stateTimeStopwatch = stopwatchScreenViewModel.stateTimeStopwatch.collectAsState()
    val stateRecordList = stopwatchScreenViewModel.stateRecordList.collectAsState()
    val statePersonList = stopwatchScreenViewModel.statepPersonList.collectAsState()
    val record = stopwatchScreenViewModel.records.collectAsState()
    val lap = stopwatchScreenViewModel.lap.collectAsState()
    val stateDragPlayBox = stopwatchScreenViewModel.stateDragPlayBox.collectAsState()

    stopwatchScreenViewModel.setLap(lap_id)

    var widthEndDrawer by remember {
        mutableStateOf(0.dp)
    }

    MyApplicationTheme(
        titleState = titleState.copy(back = {
            navigator.popBackStack()
        }),
        aplicationState = aplicationState,
        drawer = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(end = widthEndDrawer)
            ) {
                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Background)
                            .padding(top = 40.dp, start = 20.dp, end = 20.dp)
                    ) {
                        Title(TitleState(title = lap.value.name, isClose = true, close = {
                            stopwatchScreenViewModel.closeDrawer()
                        }))

                        Spacer(modifier = Modifier.height(40.dp))

                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(1),
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalItemSpacing = 10.dp
                        ) {
                            items(statePersonList.value) { it ->
                                CardList(cardState = it)
                            }
                        }

                    }
                    VerticalDivider(
                        color = Primary, modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                    )
                }
            }
        }
    ) {

        TimeStopwatch(
            stateTimeStopwatch = stateTimeStopwatch.value,
            modifier = Modifier.fillMaxWidth(),
            def_textStopwatch = TextStopwatch()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxHeight()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(end = 15.dp)
            ) {
                Text(
                    text = lap.value.name,
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(1),
                    modifier = Modifier.fillMaxWidth(),
                    verticalItemSpacing = 15.dp
                ) {
                    items(record.value) { item ->
                        var count = record.value.reversed().indexOf(item)
                        RecordList(
                            stateRecordList.value.copy(
                                onSelectPerson = {
                                    stopwatchScreenViewModel.selectRecord(item)
                                },
                                stateTimeStopwatch = stateRecordList.value.stateTimeStopwatch.copy(
                                    time = stopwatchScreenViewModel.toTimeString(item.recordDto.time),
                                    count = stopwatchScreenViewModel.toCountString(item.recordDto.time)
                                ),
                                stateAutoSizeByWidth = stateRecordList.value.stateAutoSizeByWidth.copy(
                                    text = String.format("%03d", ++count)
                                ),
                                peson = item.person?.name ?: ""
                            )
                        )
                    }
                }
            }
            val localDensity = LocalDensity.current

            DragPlayBox(stateDragPlayBox.value.copy(
                onBoxGloballyPositioned = {
                    widthEndDrawer = with(localDensity) {
                        val data = it.size.width.toFloat()
                        data.toDp().plus(20.dp).plus(15.dp)
                    }
                }
            ))

        }
    }
}

@Composable
@Preview
fun PreviewStopWatchScreen() {
    KoinApplication(application = {
        // your preview config here
        modules(appModule)
    }) {
        // Compose to preview with Koin
        StopWatchScreen(lap_id = 12, navigator = EmptyDestinationsNavigator)
    }
}

