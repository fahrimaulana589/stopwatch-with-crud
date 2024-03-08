package com.example.myapplication.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.Monstserrat
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.ui.theme.Secondary

data class StateModalDelete(
    val message: String,
    val removeOnClick: () -> Unit = {},
    val closeOnClick: () -> Unit = {},
)

@Composable
fun ModalDelete(stateModalDelete: StateModalDelete){
    Dialog(onDismissRequest = {  }) {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Background,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Column(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(15.dp),
            ) {
                Text(
                    text = "Delete",
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stateModalDelete.message,
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        shape = RoundedCornerShape(9.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Background,
                        ),
                        border = BorderStroke(2.dp, Secondary),
                        onClick = { stateModalDelete.closeOnClick() }) {
                        Text(
                            text = "Cancel",
                            fontFamily = Monstserrat,
                            color = Secondary,
                        )
                    }

                    Button(
                        shape = RoundedCornerShape(9.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Secondary
                        ),
                        onClick = { stateModalDelete.removeOnClick() }) {
                        Text(text = "Remove", fontFamily = Monstserrat, color = Background)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewModalDelete(){
    MyApplicationTheme(titleState = TitleState(title = "Soki")) {
        ModalDelete(stateModalDelete = StateModalDelete(
            message = "delate?"
        )
        )
    }
}