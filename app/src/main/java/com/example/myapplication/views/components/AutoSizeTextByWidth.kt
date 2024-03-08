package com.example.myapplication.views.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import com.example.myapplication.data.datastore.AutoTextByWidth
import com.example.myapplication.data.datastore.DataStoreManagerHolder
import com.example.myapplication.ui.theme.Background_Secondary
import com.example.myapplication.ui.theme.Odibee
import org.koin.androidx.compose.get

data class StateAutoSizeByWidth(
    val text: String,
    val autoTextByWidth: AutoTextByWidth
)

@Composable
fun autoSizeByWidth(
    modifier: Modifier,
    stateAutoSizeByWidth: StateAutoSizeByWidth,
    def_autoTextByWidth: AutoTextByWidth,
    dataStoreManagerHolder: DataStoreManagerHolder = get()
) {

    var autoTextByWidth by remember {
        mutableStateOf(
            def_autoTextByWidth.copy(
                text_id = stateAutoSizeByWidth.autoTextByWidth.text_id
            )
        )
    }

    if(stateAutoSizeByWidth.autoTextByWidth.should_draw_text){
        autoTextByWidth = stateAutoSizeByWidth.autoTextByWidth
    }

    Text(
        modifier = modifier.drawWithContent {
            if (autoTextByWidth.should_draw_text) {
                drawContent()
            }
        },
        maxLines = 1,
        text = stateAutoSizeByWidth.text,
        fontSize = autoTextByWidth.size_text,
        color = Background_Secondary,
        fontFamily = Odibee,
        onTextLayout = { result ->
            if (result.didOverflowHeight) {
                autoTextByWidth = autoTextByWidth.copy(
                    size_text = autoTextByWidth.size_text * 0.99,
                    should_draw_text = false
                )
                dataStoreManagerHolder.saveDataAutoText(autoTextByWidth)
            } else {
                autoTextByWidth = autoTextByWidth.copy(
                    should_draw_text = true
                )
                dataStoreManagerHolder.saveDataAutoText(autoTextByWidth)
            }
        }
    )
}

