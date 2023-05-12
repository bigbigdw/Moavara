package com.example.moavara.Best.Screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moavara.Best.ViewModel.ViewModelBestList
import com.example.moavara.R
import com.example.moavara.theme.backgroundType1
import com.example.moavara.theme.colorPrimary

@Composable
fun BestListScreen(viewModelBestList: ViewModelBestList) {

    viewModelBestList.fetchBestList()

    var state = viewModelBestList.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundType1)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListKeyword()

        if(state.Today){

        } else if(state.Week){

        } else if(state.Month){

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorPrimary)
        ) {
            Text(
                text = stringResource(id = R.string.text_calendar),
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ListKeyword(){

    val alignYourBodyData = listOf(
        R.drawable.logo_joara to R.string.Best_Joara1,
        R.drawable.logo_joara to R.string.Best_Joara1,
        R.drawable.logo_joara to R.string.Best_Joara1,
        R.drawable.logo_joara to R.string.Best_Joara1,
        R.drawable.logo_joara to R.string.Best_Joara1,
        R.drawable.logo_joara to R.string.Best_Joara1
    ).map { DrawableStringPair(it.first, it.second) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(alignYourBodyData) { item ->
            AlignYourBodyElement(item.drawable, item.text)
        }
    }
}

private data class DrawableStringPair(
    val drawable: Int,
    val text: Int
)

@Composable
fun AlignYourBodyElement(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.paddingFromBaseline(
                top = 24.dp, bottom = 8.dp
            )
        )
    }
}

@Preview
@Composable
fun previewBest(){
    BestListScreen(ViewModelBestList())
}