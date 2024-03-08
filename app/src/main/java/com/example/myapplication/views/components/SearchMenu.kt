package com.example.myapplication.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.Monstserrat
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.views.screens.destinations.DirectionDestination
import com.example.myapplication.views.screens.destinations.PersonScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

data class StateSearchMenu(
    val search: String,
    val onSearchChange: (search: String) -> Unit,
)

@Composable
fun SearchMenu(stateSearchMenu: StateSearchMenu) {
    Row(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = stateSearchMenu.search,
            onValueChange = { stateSearchMenu.onSearchChange(it) },
            maxLines = 1,
            textStyle = TextStyle(
                color = Color.White,
                fontFamily = Monstserrat,
                fontSize = 20.sp,
            ),
            cursorBrush = SolidColor(Primary),
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth(),
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = Primary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp)
                    ) {
                        innerTextField()
                    }
                    Image(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Back",
                        modifier = Modifier
                            .width(20.dp)
                            .align(Alignment.CenterVertically)
                            .aspectRatio(1f / 1f)
                    )

                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSearchMenu() {
    MyApplicationTheme(titleState = TitleState(title = "Soki")) {
        SubTitle(
            subTitleState = SubTitleState(
                title = "Group", drawable = R.drawable.group
            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        PersonOption(
            statePersonOption = StatePersonOption(
                optionPerson = OptionPerson.ListPerson
            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        SearchMenu(stateSearchMenu = StateSearchMenu(search = "", {}))
    }
}