package com.example.moavara.Best.Screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moavara.Best.ViewModel.ViewModelBestList
import com.example.moavara.Main.Screen.BottomNavItem
import com.example.moavara.R
import com.example.moavara.Util.AnalysisScreen
import com.example.moavara.Util.SettingsScreen
import com.example.moavara.Util.TimelineScreen
import com.example.moavara.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BestListScreen(viewModelBestList: ViewModelBestList) {
    
    val state = viewModelBestList.state.collectAsState().value

    val tabData = listOf(
        "투데이",
        "주간",
        "월간"
    )
    val pagerState = rememberPagerState()

    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { BestHeader(tabData, pagerState, tabIndex, coroutineScope) },
    ) {
        Box(Modifier.padding(it)) {
            HorizontalPager(count = tabData.size, state = pagerState) { page ->
                if (pagerState.currentPage == 0) {
                    TimelineScreen()
                    viewModelBestList.fetchBestList()
                } else if (pagerState.currentPage == 1) {
                    AnalysisScreen()
                } else {
                    SettingsScreen()
                }
            }
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(color = backgroundType1)
//                    .verticalScroll(rememberScrollState())
//                    .semantics { contentDescription = "Overview Screen" },
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                ListKeyword()
//
//                if(state.Today){
//
//                } else if(state.Week){
//
//                } else if(state.Month){
//
//                }
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(color = colorPrimary)
//                ) {
//                    Text(
//                        text = stringResource(id = R.string.text_calendar),
//                        style = MaterialTheme.typography.h1,
//                        textAlign = TextAlign.Center,
//                        color = Color.White,
//                        modifier = Modifier.align(Alignment.Center),
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
        }
    }
}

@Composable
fun ListKeyword() {

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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BestHeader(
    tabData: List<String>,
    pagerState: PagerState,
    tabIndex: Int,
    coroutineScope: CoroutineScope
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = backgroundType4),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                modifier = Modifier.padding(16.dp, 8.dp),
                text = "베스트",
                fontSize = 27.sp,
                textAlign = TextAlign.Left,
                color = textColorType3,
                fontFamily = pretendardvariable,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier.width(250.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            TabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier.padding(top = 20.dp),
                contentColor = colorPrimary
            ) {
                tabData.forEachIndexed { index, text ->
                    Tab(
                        modifier = Modifier.background(color = backgroundType4),
                        selected = tabIndex == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                        selectedContentColor = colorPrimary,
                        unselectedContentColor = textColorType4,
                        text = {
                            Text(
                                text = text, fontSize = 17.sp,
                                textAlign = TextAlign.Left,
                                color = textColorType3,
                                fontFamily = pretendardvariable,
                                fontWeight = FontWeight.Bold
                            )
                        })
                }
            }
        }
    }
}

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
fun previewBest() {
    BestListScreen(ViewModelBestList())
}