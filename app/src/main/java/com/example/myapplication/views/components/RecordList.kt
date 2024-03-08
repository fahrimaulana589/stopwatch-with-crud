package com.example.myapplication.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.data.datastore.AutoTextByWidth
import com.example.myapplication.data.datastore.TextStopwatch
import com.example.myapplication.ui.theme.Background_Secondary
import com.example.myapplication.ui.theme.Secondary
import com.example.myapplication.ui.theme.Text_Secondary

data class StateRecordList(
    val stateTimeStopwatch: StateTimeStopwatch,
    val stateAutoSizeByWidth: StateAutoSizeByWidth,
    val peson: String = "",
    val onSelectPerson: () -> Unit = {}
)

@Composable
fun RecordList(
    stateRecordList: StateRecordList
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .height(IntrinsicSize.Max)
        ) {

            var heigtStopwatch by remember {
                mutableStateOf(0.dp)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        Text_Secondary,
                        shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
                    .padding(10.dp)
                    .fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(heigtStopwatch))
                autoSizeByWidth(
                    stateAutoSizeByWidth = stateRecordList.stateAutoSizeByWidth,
                    def_autoTextByWidth = AutoTextByWidth(
                        size_text = 50.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heigtStopwatch.plus(heigtStopwatch))
                )
                Spacer(modifier = Modifier.height(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "Floating action button.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f / 1f),
                    tint = Background_Secondary
                )
            }
            Column(
                modifier = Modifier
                    .weight(5f)
                    .background(
                        Background_Secondary,
                        shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                    )
                    .padding(10.dp)
                    .fillMaxHeight()
            ) {
                TimeStopwatch(
                    stateTimeStopwatch = stateRecordList.stateTimeStopwatch,
                    modifier = Modifier.fillMaxWidth(),
                    def_textStopwatch = TextStopwatch(),
                    onHeightChange = {
                        heigtStopwatch = it
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (stateRecordList.peson.isEmpty()) {
                    Text(
                        text = "Select",
                        color = Secondary,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { stateRecordList.onSelectPerson() }
                    )

                } else {
                    Text(
                        text = stateRecordList.peson,
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}