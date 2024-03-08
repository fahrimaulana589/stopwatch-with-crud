package com.example.myapplication.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import com.example.myapplication.data.dto.GroupDto
import com.example.myapplication.data.dto.LapDto
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.Monstserrat
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.ui.theme.Secondary
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class StateModalAddEditLap(
    val lap: LapDto,
    val groups: List<GroupDto> = mutableListOf(),
    val groupSelected: List<GroupDto> = mutableListOf(),
    val openGroupSelected: Boolean = false,
    val type: ModalAddEditLapType = ModalAddEditLapType.Create,
    val isNameEror: Boolean = false,
    val errorNameMessage: String = "",
    val nameOnCahnge: (name: String) -> Unit = {},
    val isDateEror: Boolean = false,
    val errorDateMessage: String = "",
    val dateOnCahnge: (name: String) -> Unit = {},
    val isGroupEror: Boolean = false,
    val errorGroupMessage: String = "",
    val groupOnCahnge: (name: String) -> Unit = {},
    val addOnClick: () -> Unit = {},
    val editOnClick: () -> Unit = {},
    val delateOnClick: () -> Unit = {},
    val closeOnClick: () -> Unit = {},
    val closeSelectGroup: () -> Unit = {},
    val openSelectGroup: () -> Unit = {},
    val selectGroup: (groupDto: GroupDto) -> Unit = {},
)

enum class ModalAddEditLapType {
    Edit, Create
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModalAddEditLap(stateModalAddEditLap: StateModalAddEditLap) {
    Dialog(onDismissRequest = { stateModalAddEditLap.closeOnClick }) {
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
                    text = "Lap",
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))

                if (stateModalAddEditLap.openGroupSelected) {
                    FlowRow(
                        modifier = Modifier.padding(0.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {

                        stateModalAddEditLap.groupSelected.forEach {

                            stateModalAddEditLap.groupSelected.contains(it)
                            Button(
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary,
                                ),
                                border = BorderStroke(2.dp, Primary),
                                onClick = { stateModalAddEditLap.selectGroup(it) }) {
                                Text(
                                    text = it.name,
                                    fontFamily = Monstserrat,
                                    color = Background,
                                )
                            }

                        }

                    }

                    Column(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(2.dp)
                                .fillMaxWidth()
                                .background(color = Primary)
                        )
                    }

                    FlowRow(
                        modifier = Modifier.padding(0.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {

                        stateModalAddEditLap.groups.forEach {

                            stateModalAddEditLap.groupSelected.contains(it)
                            Button(
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Background,
                                ),
                                border = BorderStroke(2.dp, Primary),
                                onClick = { stateModalAddEditLap.selectGroup(it) }) {
                                Text(
                                    text = it.name,
                                    fontFamily = Monstserrat,
                                    color = Primary,
                                )
                            }

                        }

                    }
                } else {
                    Textinput(
                        TextInputState(
                            label = "Name",
                            value = stateModalAddEditLap.lap.name,
                            error = stateModalAddEditLap.isNameEror,
                            errorMessage = stateModalAddEditLap.errorNameMessage,
                            onValueChange = { value -> stateModalAddEditLap.nameOnCahnge(value) })
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Dateinput(
                        dateInputState = DateInputState(
                            label = "Date",
                            value = stateModalAddEditLap.lap.date,
                            onValueChange = {
                                stateModalAddEditLap.dateOnCahnge(it)
                            },
                            error = stateModalAddEditLap.isDateEror,
                            errorMessage = stateModalAddEditLap.errorDateMessage
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GroupSelectInput(StateGroupSelectInput(
                        label = "Group",
                        value = stateModalAddEditLap.groupSelected.map { it.name }.joinToString(separator = ", "),
                        error = stateModalAddEditLap.isGroupEror,
                        errorMessage = stateModalAddEditLap.errorGroupMessage,
                        onValueChange = {},
                        onClick = {
                            stateModalAddEditLap.openSelectGroup()
                        }
                    ))
                }

                Spacer(modifier = Modifier.height(40.dp))


                if (stateModalAddEditLap.openGroupSelected) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            shape = RoundedCornerShape(9.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary
                            ),
                            onClick = { stateModalAddEditLap.closeSelectGroup() }) {
                            Text(text = "OK", fontFamily = Monstserrat, color = Background)
                        }
                    }
                } else {
                    if (stateModalAddEditLap.type == ModalAddEditLapType.Create) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Background,
                                ),
                                border = BorderStroke(2.dp, Secondary),
                                onClick = { stateModalAddEditLap.closeOnClick() }) {
                                Text(
                                    text = "Cancel",
                                    fontFamily = Monstserrat,
                                    color = Secondary,
                                )
                            }

                            Button(
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary
                                ),
                                onClick = { stateModalAddEditLap.addOnClick() }) {
                                Text(text = "Add", fontFamily = Monstserrat, color = Background)
                            }
                        }
                    }
                    if (stateModalAddEditLap.type == ModalAddEditLapType.Edit) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Background,
                                ),
                                border = BorderStroke(2.dp, Secondary),
                                onClick = { stateModalAddEditLap.delateOnClick() }) {
                                Text(
                                    text = "Delete",
                                    fontFamily = Monstserrat,
                                    color = Secondary,
                                )
                            }

                            Button(
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary
                                ),
                                onClick = { stateModalAddEditLap.editOnClick() }) {
                                Text(text = "Edit", fontFamily = Monstserrat, color = Background)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewModalAddEditLap() {
    MyApplicationTheme(titleState = TitleState(title = "Soki")) {

        var lap by remember {
            mutableStateOf(LapDto(name = "op", date = "12123333"))
        }

        var groups by remember {
            mutableStateOf(
                listOf(
                    GroupDto(name = "asasa", group_id = 2),
                    GroupDto(name = "ok", group_id = 3),
                    GroupDto(name = "asasas", group_id = 4),
                )
            )
        }

        var groupSelected by remember {
            mutableStateOf(listOf(GroupDto(name = "asasasas", group_id = 1)))
        }

        ModalAddEditLap(stateModalAddEditLap = StateModalAddEditLap(
            lap = lap,
            groups = groups,
            openGroupSelected = false,
            groupSelected = groupSelected,
            nameOnCahnge = {},
            dateOnCahnge = {},
            selectGroup = {
                when {

                }
            }
        ))
    }
}