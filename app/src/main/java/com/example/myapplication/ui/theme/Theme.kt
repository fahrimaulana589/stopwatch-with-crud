package com.example.myapplication.ui.theme

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.views.components.TitleState
import com.example.myapplication.views.components.Title

data class AplicationState(
    val blur: Dp = 0.dp,
    val darwerVisible: Boolean = false
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApplicationTheme(
    titleState: TitleState,
    aplicationState: AplicationState = AplicationState(),
    fab: @Composable () -> Unit = {},
    drawer: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        floatingActionButton = {

            fab()
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .background(Background)
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, top = 40.dp)
            )
            {
                Title(titleState)
                content()
            }
            AnimatedVisibility(
                visible = aplicationState.darwerVisible,
                enter = slideInHorizontally(animationSpec = tween(500, easing = LinearEasing)),
                exit = slideOutHorizontally(animationSpec = tween(500, easing = LinearEasing))
            ) {
                drawer()
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MyApplicationThemePreview() {
    MyApplicationTheme(titleState = TitleState(title = "SOKI STOPWATCH"), content = {

    })
}


