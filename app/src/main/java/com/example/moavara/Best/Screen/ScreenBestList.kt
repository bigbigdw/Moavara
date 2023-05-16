package com.example.moavara.Best.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moavara.Best.ViewModel.ViewModelBestList
import com.example.moavara.Search.BestKeyword
import com.example.moavara.Util.AnalysisScreen
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.SettingsScreen
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
                    viewModelBestList.fetchBestList()
                    BestTodayScreen()
                } else if (pagerState.currentPage == 1) {
                    AnalysisScreen()
                } else {
                    SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun BestTodayScreen() {

    val (getType, setType) = remember { mutableStateOf("Joara") }

    LazyColumn(
        modifier = Modifier
            .background(color = backgroundType1)
            .fillMaxWidth(),
    ) {
        item { ListKeyword(getType, setType) }
        item { ListBestToday(getType, setType) }
    }
}

@Composable
fun ListBestToday(getType: String, setType: (String) -> Unit) {

    val typeItems = ArrayList<BestKeyword>()

    for(i in BestRef.typeList().indices){
        typeItems.add(
            BestKeyword(
                title= BestRef.typeListTitle()[i],
                type = BestRef.typeList()[i],
                img = BestRef.typeImg()[i],
            )
        )
    }

    LazyRow(
        modifier = Modifier.padding(16.dp, 20.dp, 0.dp, 20.dp),
    ) {
        items(typeItems) { item ->
            Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)){
                ItemKeyword(getType, setType, item.img, item.title, item.type)
            }
        }
    }
}

@Composable
fun ListKeyword(getType: String, setType: (String) -> Unit) {

    val typeItems = ArrayList<BestKeyword>()

    for(i in BestRef.typeList().indices){
        typeItems.add(
            BestKeyword(
                title= BestRef.typeListTitle()[i],
                type = BestRef.typeList()[i],
                img = BestRef.typeImg()[i],
            )
        )
    }

    LazyRow(
        modifier = Modifier.padding(16.dp, 20.dp, 0.dp, 20.dp),
    ) {
        items(typeItems) { item ->
            Box(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)){
                ItemKeyword(getType, setType, item.img, item.title, item.type)
            }
        }
    }
}

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
                                text = text,
                                fontSize = 17.sp,
                                textAlign = TextAlign.Left,
                                color = if (tabIndex == index) {
                                    colorPrimary
                                } else {
                                    textColorType4
                                },
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
fun ItemKeyword(
    getter: String,
    setter: (String) -> Unit,
    img: Int,
    title: String,
    type: String
) {

    Card(
        modifier = if (getter == type) {
            Modifier.border(2.dp, backgroundType7, CircleShape)
        } else {
            Modifier.border(2.dp, backgroundType8, CircleShape)
        },
        backgroundColor = backgroundType1,
        shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp)
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp, 8.dp).clickable { setter(type) },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                painter = painterResource(img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(23.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = title,
                fontSize = 17.sp,
                textAlign = TextAlign.Left,
                color = textColorType3,
                fontFamily = pretendardvariable,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun previewBest() {
    BestListScreen(ViewModelBestList())
}