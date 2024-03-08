package com.example.myapplication.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.ui.theme.Text

data class TextInputState(
    val label: String = "label",
    val value: String,
    val onValueChange: (value:String) -> Unit,
    val error: Boolean = false,
    val errorMessage: String = "",
)

@Composable
fun Textinput(
   textInputState: TextInputState
){
    Text(
        text = textInputState.label,
        color = Color.White,
        fontSize = 16.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = textInputState.value,
        onValueChange = {textInputState.onValueChange(it)},
        textStyle = TextStyle(color = Text),
        modifier = Modifier.padding(1.dp).fillMaxWidth(),
        colors = TextFieldDefaults
            .colors(
                unfocusedContainerColor = Background,
                focusedContainerColor = Background,
                focusedIndicatorColor = Primary,
                unfocusedIndicatorColor = Primary,
            ),
    )
    if (textInputState.error){
        Text(
            text = textInputState.errorMessage,
            color = Color.Red,
            fontSize = 12.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}