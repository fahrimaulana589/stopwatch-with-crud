package com.example.myapplication.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Background_Secondary
import com.example.myapplication.ui.theme.Monstserrat
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Text_Secondary

data class CardState(
    val id: Int,
    val nomer: Int,
    val name: String,
    val action: CardLinkState? = null,
    val onClick: () -> Unit = {}
)

data class CardLinkState(
    val drawable: Int,
    val action: () -> Unit
)

@Composable
fun CardList(cardState: CardState) {
    Row(modifier = Modifier.fillMaxWidth()
        .clickable { cardState.onClick() },) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Background_Secondary, shape = RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(text = cardState.nomer.toString()+".",
                fontFamily = Monstserrat,
                fontSize = 24.sp,
                color = Text_Secondary,
                modifier = Modifier.padding(end = 5.dp))
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = cardState.name,
                    fontFamily = Monstserrat,
                    fontSize = 24.sp,
                    color = Text_Secondary,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .weight(1f))
                if (cardState.action != null){
                    Image(
                        painter = painterResource(id =cardState.action.drawable),
                        contentDescription = "Back",
                        modifier = Modifier
                            .width(20.dp)
                            .padding(top = 8.dp)
                            .aspectRatio(1f / 1f)
                            .clickable {
                                cardState.action.action()
                            }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, backgroundColor = 0xFF0F141E)
@Composable
fun PreviewCardList() {
    val items : List<CardState> = (1..10).map {
        CardState(id = 1,
            nomer = 1,
            name = "Fahri Fahri Fahri Fahri Fahri Fahri Fahri Fahri Fahri Fahri Fahri Fahri Fahri Fahri "
        )
    }
    MyApplicationTheme(titleState = TitleState(title = "Soki"), content = {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
            modifier = Modifier.fillMaxWidth(),
            verticalItemSpacing = 15.dp
        ) {
            items(items) { item ->
                CardList(cardState = item)
            }
        }
    })
}