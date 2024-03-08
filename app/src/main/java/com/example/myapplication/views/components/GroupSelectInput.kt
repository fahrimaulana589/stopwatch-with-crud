package com.example.myapplication.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.LapDto
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.ui.theme.Text

data class StateGroupSelectInput(
    val label: String = "label",
    val value: String,
    val onValueChange: (value: String) -> Unit = {},
    val error: Boolean = false,
    val errorMessage: String = "",
    val onClick: () -> Unit,
)

@Composable
fun GroupSelectInput(
    stateGroupSelectInput: StateGroupSelectInput
) {
    Text(
        text = stateGroupSelectInput.label,
        color = Color.White,
        fontSize = 16.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Background)
        .border(1.dp, Primary, RoundedCornerShape(3.dp))
        .padding(17.dp)
        .clickable { stateGroupSelectInput.onClick() }
    ) {
        Text(
            text = stateGroupSelectInput.value,
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (stateGroupSelectInput.error) {
        Text(
            text = stateGroupSelectInput.errorMessage,
            color = Color.Red,
            fontSize = 12.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewGroupSelectInput() {
    MyApplicationTheme(titleState = TitleState(title = "Soki")) {

        var lap by remember {
            mutableStateOf(LapDto(name = "op", date = "12123333"))
        }

        var groups by remember {
            mutableStateOf(
                mutableListOf(
                    GroupDto(name = "asasa", group_id = 2),
                    GroupDto(name = "ok", group_id = 3),
                    GroupDto(name = "asasas", group_id = 4),
                )
            )
        }

        var groupSelected by remember {
            mutableStateOf(mutableListOf(GroupDto(name = "asasasas", group_id = 1)))
        }

        ModalAddEditLap(stateModalAddEditLap = StateModalAddEditLap(
            lap = lap,
            groups = groups,
            openGroupSelected = false,
            groupSelected = groupSelected,
            nameOnCahnge = {},
            dateOnCahnge = {},
            selectGroup = {

            }
        ))
    }
}