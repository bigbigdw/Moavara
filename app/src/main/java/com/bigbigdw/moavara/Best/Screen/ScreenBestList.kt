package com.bigbigdw.moavara.Best.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.bigbigdw.moavara.Best.ViewModel.ViewModelBestList
import com.bigbigdw.moavara.Best.intent.StateBestList
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Search.*
import com.bigbigdw.moavara.Util.BestRef
import com.bigbigdw.moavara.Util.LoadingScreen
import com.bigbigdw.moavara.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun BestListScreen(viewModelBestList: ViewModelBestList, modalSheetState: ModalBottomSheetState) {

    val state = viewModelBestList.state.collectAsState().value

    val (getType, setType) = remember { mutableStateOf("") }

    if(state.todayInit){
        viewModelBestList.fetchBestList(LocalContext.current)
        if(getType == ""){
            setType("Joara")
            viewModelBestList.fetchBestListToday("Joara", LocalContext.current)
        } else {
            viewModelBestList.fetchBestListToday(getType, LocalContext.current)
        }

        viewModelBestList.fetchBestTodayDone()
    }

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
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = color1E1E20), contentAlignment = Alignment.TopStart) {

            HorizontalPager(userScrollEnabled = false, count = tabData.size, state = pagerState, verticalAlignment = Alignment.Top) { page ->
                Box(modifier = Modifier.fillMaxSize()){
                    if (pagerState.currentPage == 0) {
                        BestTodayScreen(viewModelBestList, state, getType, setType, modalSheetState)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BestTodayScreen(
    viewModelBestList: ViewModelBestList,
    state: StateBestList,
    getType: String,
    setType: (String) -> Unit,
    modalSheetState: ModalBottomSheetState
) {

    val listState = rememberLazyListState()

    Column {
        ListKeyword(getType, setType, viewModelBestList, listState)
        LazyColumn(state = listState) {
            if (state.loading) {
                item { LoadingScreen() }
            } else {
                itemsIndexed(items = state.bestTodayItem) { index, item ->
                    ListBestToday(item, state.bestTodayItemBookCode[index], index, modalSheetState)
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListBestToday(
    bestItemData: BestItemData,
    bookCodeItems: BestListAnalyze,
    index: Int,
    modalSheetState: ModalBottomSheetState
) {

    val coroutineScope = rememberCoroutineScope()

    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = rememberAsyncImagePainter(bestItemData.bookImg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clickable {
                    coroutineScope.launch {
                        modalSheetState.show()
                    }
                },
            backgroundColor = backgroundType9,
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "${index + 1} ",
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(16.dp, 0.dp, 0.dp, 0.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Left,
                    color = textColorType6,
                    fontFamily = pretendardvariable,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    maxLines = 1,
                    text = "${bestItemData.title}",
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Left,
                    color = textColorType6,
                    fontFamily = pretendardvariable,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.width(16.dp))

                if(bookCodeItems.trophyCount > 1){

                    if(bookCodeItems.numberDiff > 0){
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_drop_up_24px),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = bookCodeItems.numberDiff.toString(),
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(0.dp, 0.dp, 16.dp, 0.dp)
                                .wrapContentSize(),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Left,
                            color = textColorType7,
                            fontFamily = pretendardvariable,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else if(bookCodeItems.numberDiff < 0){
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_drop_down_24px),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = bookCodeItems.numberDiff.toString(),
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(0.dp, 0.dp, 16.dp, 0.dp)
                                .wrapContentSize(),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Left,
                            color = textColorType8,
                            fontFamily = pretendardvariable,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else if(bookCodeItems.numberDiff == 0){
                        Text(
                            text = "-",
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(0.dp, 0.dp, 16.dp, 0.dp)
                                .wrapContentSize(),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Left,
                            color = textColorType9,
                            fontFamily = pretendardvariable,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    Text(
                        text = "NEW",
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(0.dp, 0.dp, 16.dp, 0.dp)
                            .wrapContentSize(),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Left,
                        color = textColorType6,
                        fontFamily = pretendardvariable,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun ListKeyword(
    getType: String,
    setType: (String) -> Unit,
    viewModelBestList: ViewModelBestList,
    listState: LazyListState
) {

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
                ItemKeyword(getType, setType, item, viewModelBestList, listState)
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
            .background(color = backgroundType4),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .wrapContentSize(),
                text = "베스트",
                fontSize = 27.sp,
                textAlign = TextAlign.Left,
                color = textColorType3,
                fontFamily = pretendardvariable,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .height(60.dp)
                .width(270.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            TabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .wrapContentSize(),
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
    viewModelBestList: ViewModelBestList,
    listState: LazyListState
) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = if (getter == item.type) {
            Modifier.border(2.dp, color621CEF, CircleShape)
        } else {
            Modifier.border(2.dp, color3E424B, CircleShape)
        },
        backgroundColor = color1E1E20,
        shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp)
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp, 8.dp)
                .clickable {
                    setter(item.type)
                    viewModelBestList.fetchBestListToday(item.type, context)
                    coroutineScope.launch {
                        listState.scrollToItem(index = 0)
                    }
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

@Composable
fun BottomDialogBest(contents : @Composable ()-> Unit) {

    val weekItem = ArrayList<BestListAnalyzeWeek>()
    val item = BottomBestItemData()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(color = backgroundType3),

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(0.dp, 10.dp, 0.dp, 0.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(4.dp)
                    .background(color = backgroundType10)
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            ){
            Image(
                painter = rememberAsyncImagePainter(item.bookImg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(92.dp)
                    .height(142.dp)
                    .clip(RoundedCornerShape(15.dp))
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Left,
                    color = colorE2E3E7,
                    fontFamily = pretendardvariable,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.size(8.dp))

                Row(){
                    Text(
                        text = item.info1,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Left,
                        color = color6E7686,
                        fontFamily = pretendardvariable,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                val info2 = buildAnnotatedString {
                    append(item.info2Title)
                    withStyle(style = SpanStyle(color6E7686)) {
                        append(" ${item.info2}")
                    }
                }

                Text(
                    text = info2,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Left,
                    color = colorE2E3E7,
                    fontFamily = pretendardvariable,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )

                val info3 = buildAnnotatedString {
                    append(item.info3Title)
                    withStyle(style = SpanStyle(color6E7686)) {
                        append(" ${item.info3}")
                    }
                }

                Text(
                    text = info3,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Left,
                    color = colorE2E3E7,
                    fontFamily = pretendardvariable,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )

                val info4 = buildAnnotatedString {
                    append(item.info4Title)
                    withStyle(style = SpanStyle(color6E7686)) {
                        append(" ${item.info4}")
                    }
                }

                Text(
                    text = info4,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Left,
                    color = colorE2E3E7,
                    fontFamily = pretendardvariable,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.size(12.dp))

                Text(
                    text = item.info5,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Left,
                    color = color6E7686,
                    fontFamily = pretendardvariable,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row {
            weekItem.forEach { item ->
                Column(
                    modifier = Modifier.padding(16.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "item.dateString",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Left,
                        color = textColorType12,
                        fontFamily = pretendardvariable,
                        fontWeight = FontWeight.Bold
                    )

                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_best_gr_24px),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                        )

                        Text(
                            text = "100",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Left,
                            color = textColorType12,
                            fontFamily = pretendardvariable,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "item.dateString",
                fontSize = 14.sp,
                textAlign = TextAlign.Left,
                color = textColorType12,
                fontFamily = pretendardvariable,
                fontWeight = FontWeight.Bold
            )

            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_best_gr_24px),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                )

                Text(
                    text = "100",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Left,
                    color = textColorType12,
                    fontFamily = pretendardvariable,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.size(12.dp))

        Row(){
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = color3E424B),
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(
                    text = "상세 보기",
                    textAlign = TextAlign.Center,
                    color = textColorType3,
                    fontSize = 14.sp,
                    fontFamily = pretendardvariable
                )
            }
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = color621CEF),
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(
                    text = "Pick하기",
                    textAlign = TextAlign.Center,
                    color = textColorType3,
                    fontSize = 14.sp,
                    fontFamily = pretendardvariable
                )
            }
        }
    }
}

@Preview
@Composable
fun previewBottomDialog(){
    BottomDialogBest({})
}

@Composable
fun BottomDialogBestBtn(state: StateBestList){
    Row(){
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = color3E424B),
            onClick = { },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = "상세 보기",
                textAlign = TextAlign.Center,
                color = textColorType3,
                fontSize = 14.sp,
                fontFamily = pretendardvariable
            )
        }
        Button(
            colors = if (state.isPicked) {
                ButtonDefaults.buttonColors(backgroundColor = colorA7ACB7)
            } else {
                ButtonDefaults.buttonColors(backgroundColor = color621CEF)
            },
            onClick = { },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = if (state.isPicked) {
                    "Pick 완료"
                } else {
                    "Pick하기"
                },
                textAlign = TextAlign.Center,
                color = textColorType3,
                fontSize = 14.sp,
                fontFamily = pretendardvariable
            )
        }
    }
}