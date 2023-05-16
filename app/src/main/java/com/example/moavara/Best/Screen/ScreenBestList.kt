package com.example.moavara.Best.Screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moavara.Best.ViewModel.ViewModelBestList
import com.example.moavara.Main.Screen.BottomNavItem
import com.example.moavara.R
import com.example.moavara.Util.SettingsScreen
import com.example.moavara.theme.*

@Composable
fun BestListScreen(viewModelBestList: ViewModelBestList) {

    viewModelBestList.fetchBestList()

    val state = viewModelBestList.state.collectAsState().value

    val navController = rememberNavController()

    Scaffold(
        topBar = { BestHeader(navController) },
    ) {
        Box(Modifier.padding(it)){
            NavigationGraphHeader(navController = navController)
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
fun NavigationGraphHeader(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.BEST.screenRoute) {
        composable(BestHeaderNavItem.TODAY.screenRoute) {
            SettingsScreen()
        }
        composable(BestHeaderNavItem.WEEK.screenRoute) {
            SettingsScreen()
        }
        composable(BestHeaderNavItem.MONTH.screenRoute) {
            SettingsScreen()
        }
    }
}

sealed class BestHeaderNavItem(val title: String, val iconOn: Int, val iconOff: Int, val screenRoute: String) {
    object TODAY : BestHeaderNavItem("베스트", R.drawable.ic_best_vt_24px, R.drawable.ic_best_gr_24px, "BEST")
    object WEEK : BestHeaderNavItem("이벤트", R.drawable.ic_event_vt_24px, R.drawable.ic_event_gr_24px, "Event")
    object MONTH : BestHeaderNavItem("PICK", R.drawable.ic_push_pin_vt_24px, R.drawable.ic_push_pin_gr_24px, "PICK")
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
fun BestHeader(navController: NavHostController) {

    val items = listOf(
        BestHeaderNavItem.TODAY,
        BestHeaderNavItem.WEEK,
        BestHeaderNavItem.MONTH,
    )

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(color = backgroundType4),
    verticalAlignment = Alignment.Bottom) {
        Text(
            modifier = Modifier.padding(16.dp, 8.dp),
            text = "베스트",
            fontSize = 27.sp,
            textAlign = TextAlign.Left,
            color = textColorType3,
            fontFamily = pretendardvariable,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier.weight(1f).background(color = Color.Red),
            contentAlignment = Alignment.BottomEnd
        ){
            Row(){
                items.forEach { item ->

                    Text(
                        modifier = Modifier.background(color = Color.Blue),
                        text = item.title,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Left,
                        color = textColorType3,
                        fontFamily = pretendardvariable,
                        fontWeight = FontWeight.Bold
                    )
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
fun previewBest(){
    BestListScreen(ViewModelBestList())
}