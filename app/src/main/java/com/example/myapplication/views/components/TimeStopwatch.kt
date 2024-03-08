package com.example.myapplication.views.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.datastore.DataStoreManagerHolder
import com.example.myapplication.data.datastore.TextStopwatch
import com.example.myapplication.ui.theme.Background_Secondary
import com.example.myapplication.ui.theme.Digital
import com.example.myapplication.ui.theme.Primary
import org.koin.androidx.compose.get

data class StateTimeStopwatch(
    val time: String,
    val count: String = "0",
    val textStopwatch: TextStopwatch
)

@Composable
fun TimeStopwatch(
    stateTimeStopwatch: StateTimeStopwatch,
    modifier: Modifier = Modifier,
    def_textStopwatch: TextStopwatch,
    onHeightChange: (dp: Dp) -> Unit = {},
    dataStoreManagerHolder: DataStoreManagerHolder = get()
) {
    var textStopwatch by remember {
        mutableStateOf(
            def_textStopwatch.copy(
                text_id = stateTimeStopwatch.textStopwatch.text_id
            )
        )
    }

    if (stateTimeStopwatch.textStopwatch.should_draw_stopwatch) {
        textStopwatch = stateTimeStopwatch.textStopwatch
    }

    var height by remember {
        mutableStateOf(0.dp)
    }

    val localDensity = LocalDensity.current

    Column(
        modifier = Modifier
            .height(height)
    ) {
        // Misalnya, tampilkan tinggi komponen di sini
        Text(
            text = stateTimeStopwatch.count,
            color = Primary,
            textAlign = TextAlign.End,
            modifier = Modifier
                .drawWithContent {
                    if (textStopwatch.should_draw_count) {
                        drawContent()
                    }
                }
                .fillMaxHeight()
                .fillMaxWidth(),
            softWrap = false,
            fontSize = textStopwatch.size_count,
            fontFamily = Digital,
            onTextLayout = { result ->
                if (!result.didOverflowHeight) {
                    textStopwatch = textStopwatch.copy(
                        size_count = textStopwatch.size_count * 1.01,
                        should_draw_count = false
                    )
                    dataStoreManagerHolder.saveDataStopwatch(textStopwatch)
                } else {
                    textStopwatch = textStopwatch.copy(
                        should_draw_count = true
                    )
                    dataStoreManagerHolder.saveDataStopwatch(textStopwatch)
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                // Ambil tinggi komponen dalam px
                height = with(localDensity) {
                    val data = coordinates.size.height.toFloat() * 0.5
                    onHeightChange(
                        data
                            .toFloat()
                            .toDp()
                    )
                    data
                        .toFloat()
                        .toDp()
                }
            }
    ) {
        Text(
            text = "00:00:00",
            color = Background_Secondary,
            modifier = Modifier
                .drawWithContent {
                    if (textStopwatch.should_draw_stopwatch) {
                        drawContent()
                    }
                }
                .fillMaxWidth(),
            softWrap = false,
            fontSize = textStopwatch.size_stopwatch,
            fontFamily = Digital,
        )
        Text(
            text = stateTimeStopwatch.time,
            color = Primary,
            modifier = Modifier
                .drawWithContent {
                    if (textStopwatch.should_draw_stopwatch) {
                        drawContent()
                    }
                }
                .fillMaxWidth(),
            softWrap = false,
            fontSize = textStopwatch.size_stopwatch,
            fontFamily = Digital,
            onTextLayout = { result ->

                if (!result.didOverflowWidth) {
                    textStopwatch = textStopwatch.copy(
                        size_stopwatch = textStopwatch.size_stopwatch * 1.01,
                    )
                    dataStoreManagerHolder.saveDataStopwatch(textStopwatch)
                } else {
                    textStopwatch = textStopwatch.copy(
                        should_draw_stopwatch = true
                    )
                    dataStoreManagerHolder.saveDataStopwatch(textStopwatch)
                }

            }
        )
    }
}