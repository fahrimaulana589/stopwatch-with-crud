package com.example.myapplication.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.Background_Secondary
import com.example.myapplication.ui.theme.Primary
import kotlin.math.roundToInt

data class StateDragPlayBox(
    val onPlayClick : () -> Unit,
    val onStopClick : () -> Unit,
    val onRestartClick : () -> Unit,
    val onRecordClick : () -> Unit,
    val status: DragPlayBoxStatus = DragPlayBoxStatus.Stop,
    val onBoxGloballyPositioned: (layout: LayoutCoordinates) -> Unit = {}
) {
}

enum class DragPlayBoxStatus {
    Play,Stop
}

@Composable
fun DragPlayBox(stateDragPlayBox: StateDragPlayBox) {
    var offsetY by remember { mutableStateOf(0f) }
    var maxOffsety by remember { mutableStateOf(0f) }
    var heightBox by remember { mutableStateOf(0f) }
    var heightBoxMenu by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .onGloballyPositioned {
                heightBox = it.size.height.toFloat()
                stateDragPlayBox.onBoxGloballyPositioned(it)
            }, contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(5.dp)
                .background(Primary)
        ) {
        }

        Column(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .background(Background_Secondary, shape = RoundedCornerShape(7.dp))
                .width(70.dp)
                .padding(6.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        if ((offsetY + dragAmount.y) >= 0 && (offsetY + dragAmount.y) <= (heightBox - heightBoxMenu)) {
                            maxOffsety = offsetY + dragAmount.y
                            offsetY += dragAmount.y
                        }
                    }
                }
                .onGloballyPositioned {
                    heightBoxMenu = it.size.height.toFloat()
                }
        ) {

            when(stateDragPlayBox.status){
                DragPlayBoxStatus.Play -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Primary, shape = CircleShape)
                            .aspectRatio(1f / 1f)
                            .clickable {
                                stateDragPlayBox.onStopClick()
                            }
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.stop),
                            contentDescription = "Floating action button.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                                .aspectRatio(1f / 1f),
                        )
                    }
                }

                DragPlayBoxStatus.Stop -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Primary, shape = CircleShape)
                            .aspectRatio(1f / 1f)
                            .clickable {
                                stateDragPlayBox.onPlayClick()
                            }
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.play2),
                            contentDescription = "Floating action button.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 20.dp, end = 5.dp, bottom = 20.dp)
                                .aspectRatio(1f / 1f),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            when(stateDragPlayBox.status){
                DragPlayBoxStatus.Play -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Primary, shape = CircleShape)
                            .aspectRatio(1f / 1f)
                            .clickable {
                                stateDragPlayBox.onRecordClick()
                            }
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.record),
                            contentDescription = "Floating action button.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .aspectRatio(1f / 1f),
                        )
                    }
                }
                DragPlayBoxStatus.Stop -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Primary, shape = CircleShape)
                            .aspectRatio(1f / 1f)
                            .clickable {
                                stateDragPlayBox.onRestartClick()
                            }
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.repeat),
                            contentDescription = "Floating action button.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .aspectRatio(1f / 1f),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.drag),
                contentDescription = "Floating action button.",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 1f),
            )
        }
    }
}