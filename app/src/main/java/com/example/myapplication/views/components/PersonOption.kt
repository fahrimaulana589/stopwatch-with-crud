package com.example.myapplication.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.Monstserrat
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Primary

data class StatePersonOption(
    val optionPerson: OptionPerson,
    val onListPersonClick: () -> Unit = {},
    val onAllPersonClick: () -> Unit = {},
)

enum class OptionPerson {
    ListPerson, AllPerson
}

@Composable
fun PersonOption(statePersonOption: StatePersonOption) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Primary)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = when (statePersonOption.optionPerson) {
                        OptionPerson.AllPerson -> Background
                        OptionPerson.ListPerson -> Primary
                    },
                )
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    statePersonOption.onListPersonClick()
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "List Person",
                fontFamily = Monstserrat,
                fontSize = 20.sp,
                color = when (statePersonOption.optionPerson) {
                    OptionPerson.AllPerson -> Primary
                    OptionPerson.ListPerson -> Background
                },
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier
                .background(
                    color = when (statePersonOption.optionPerson) {
                        OptionPerson.AllPerson -> Primary
                        OptionPerson.ListPerson -> Background
                    },
                )
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    statePersonOption.onAllPersonClick()
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "All Person",
                fontFamily = Monstserrat,
                fontSize = 20.sp,
                color = when (statePersonOption.optionPerson) {
                    OptionPerson.AllPerson -> Background
                    OptionPerson.ListPerson -> Primary
                },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPersonOption() {

    MyApplicationTheme(titleState = TitleState(title = "Soki"), content = {
        SubTitle(subTitleState = SubTitleState(title = "Soki", R.drawable.group))
        Spacer(modifier = Modifier.height(15.dp))
        PersonOption(
            statePersonOption = StatePersonOption(
                optionPerson = OptionPerson.ListPerson
            )
        )
    })

}