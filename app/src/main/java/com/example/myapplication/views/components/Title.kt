package com.example.myapplication.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.Primary
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.Monstserrat
import com.example.myapplication.ui.theme.Text

data class TitleState(
    val title: String,
    val isBack: Boolean = false,
    val isClose: Boolean = false,
    val close:()->Unit = {},
    val back:()->Unit = {},
)

@Composable
fun Title(titleState: TitleState){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        if (titleState.isBack){
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "Back",
                modifier = Modifier
                    .clickable {
                        titleState.back()
                    }
            )
        }


        Text(
            text = titleState.title,
            color = Text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Monstserrat
        )

        if (titleState.isClose){
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Image(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable {
                            titleState.close()
                        }
                )
            }
        }
    }
}

data class SubTitleState(
    val title: String,
    val drawable: Int,
)

@Composable
fun SubTitle(subTitleState: SubTitleState){
    Row(modifier = Modifier
        .fillMaxWidth()
        .border(color = Primary, width = 4.dp, shape = RoundedCornerShape(10.dp))
        .background(Background)
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
        ){
        Image(
            painter = painterResource(id =subTitleState.drawable),
            contentDescription = "Back",
            modifier = Modifier
                .width(30.dp)
                .aspectRatio(1f / 1f)
        )

        Text(text = subTitleState.title,
            color = Primary,
            fontFamily = Monstserrat,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        )
    }
}

@Composable
@Preview
fun PreviewSubTitle(){
    SubTitle(subTitleState = SubTitleState("Test",R.drawable.person))
}

@Composable
@Preview(showBackground = true,backgroundColor = 0xFF0F141E)
fun PreviewTitle(){
    Title(titleState = TitleState("Test", isClose = true),)
}