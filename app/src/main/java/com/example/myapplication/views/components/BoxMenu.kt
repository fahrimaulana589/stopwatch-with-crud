package com.example.myapplication.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.Monstserrat
import com.example.myapplication.views.screens.destinations.DirectionDestination
import com.example.myapplication.views.screens.destinations.PersonScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

data class BoxMenuState(
    val title: String,
    val drawable: Int,
    val onClick: ()-> Unit,
)

@Composable
fun BoxMenu(boxMenuState: BoxMenuState) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f / 1f)
        .border(color = Primary, width = 4.dp, shape = RoundedCornerShape(10.dp))
        .background(Background)
        .padding(10.dp)
        .clickable {
            boxMenuState.onClick()
        }){
        Image(
            painter = painterResource(id =boxMenuState.drawable),
            contentDescription = "Back",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5f / 4f)
        )

        Text(text = boxMenuState.title,
            color = Primary,
            fontFamily = Monstserrat,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(widthDp = 100, heightDp = 100)
@Composable
fun PreviewBoxMenu (){
    BoxMenu(boxMenuState = BoxMenuState(title = "Person", drawable = R.drawable.person, onClick = {}))
}