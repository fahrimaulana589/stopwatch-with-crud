package com.example.myapplication.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.ui.theme.Text

data class DateInputState(
    val label: String = "label",
    val value: String,
    val onValueChange: (value: String) -> Unit,
    val error: Boolean = false,
    val errorMessage: String = "",
)

@Composable
fun Dateinput(
    dateInputState: DateInputState
) {
    Text(
        text = dateInputState.label,
        color = Color.White,
        fontSize = 16.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        singleLine = true,
        value = dateInputState.value,
        onValueChange = {
            if (it.length <= 8) dateInputState.onValueChange(it)
        },
        textStyle = TextStyle(color = Text),
        modifier = Modifier.padding(1.dp).fillMaxWidth(),
        colors = TextFieldDefaults
            .colors(
                unfocusedContainerColor = Background,
                focusedContainerColor = Background,
                focusedIndicatorColor = Primary,
                unfocusedIndicatorColor = Primary,
            ),
        visualTransformation = DateTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    if (dateInputState.error) {
        Text(
            text = dateInputState.errorMessage,
            color = Color.Red,
            fontSize = 12.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

class DateTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return dateFilter(text)
    }
}

fun dateFilter(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 2 == 1 && i < 4) out += "/"
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 3) return offset + 1
            if (offset <= 8) return offset + 2
            return 10
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 2) return offset
            if (offset <= 5) return offset - 1
            if (offset <= 10) return offset - 2
            return 8
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}