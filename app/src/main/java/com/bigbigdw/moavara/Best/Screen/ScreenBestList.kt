package com.bigbigdw.moavara.Best.Screen

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.moavara.Best.ViewModel.ViewModelBestList
import com.bigbigdw.moavara.Best.intent.StateBestList
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Search.BestKeyword
import com.bigbigdw.moavara.Util.BestRef
import com.bigbigdw.moavara.Util.LoadingScreen
import com.bigbigdw.moavara.theme.*
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
        Box(Modifier.padding(it).fillMaxSize().background(color = backgroundType1), contentAlignment = Alignment.TopStart) {
            HorizontalPager(count = tabData.size, state = pagerState, verticalAlignment = Alignment.Top) { page ->
                Box(modifier = Modifier.fillMaxSize()){
                    if (pagerState.currentPage == 0) {

                        if(state.Loading){
                            viewModelBestList.fetchBestList(LocalContext.current)
                            viewModelBestList.fetchBestListToday("Joara", LocalContext.current)
                        }

                        BestTodayScreen(viewModelBestList, state)
                    } else if (pagerState.currentPage == 1) {
                        LoadingScreen()
                    } else {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun BestTodayScreen(viewModelBestList: ViewModelBestList, state: StateBestList) {

    val (getType, setType) = remember { mutableStateOf("Joara") }

    LazyColumn() {
        item { ListKeyword(getType, setType, viewModelBestList) }
        if (state.Loading) {
            item { LoadingScreen() }
        } else {
            items(items = state.BestTodayItem){
                ListBestToday(getType, setType, state)
            }
        }

    }
}

@Composable
fun ListBestToday(getType: String, setType: (String) -> Unit, state: StateBestList) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp, 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = painterResource(id = R.drawable.moabara_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(23.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "HIHIHI",
            fontSize = 17.sp,
            textAlign = TextAlign.Left,
            color = textColorType3,
            fontFamily = pretendardvariable,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ListKeyword(getType: String, setType: (String) -> Unit, viewModelBestList: ViewModelBestList) {

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
                ItemKeyword(getType, setType, item, viewModelBestList)
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
    item: BestKeyword,
    viewModelBestList: ViewModelBestList
) {

    val context = LocalContext.current

    Card(
        modifier = if (getter == item.type) {
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
                .padding(12.dp, 8.dp)
                .clickable {
                    setter(item.type)
                    viewModelBestList.fetchBestListToday(item.type, context)
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                painter = painterResource(item.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(23.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = item.title,
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