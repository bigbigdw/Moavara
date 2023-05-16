package com.bigbigdw.moavara.Main.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bigbigdw.moavara.Best.Screen.BestListScreen
import com.bigbigdw.moavara.Best.ViewModel.ViewModelBestList
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Util.AnalysisScreen
import com.bigbigdw.moavara.Util.CalendarScreen
import com.bigbigdw.moavara.Util.SettingsScreen
import com.bigbigdw.moavara.Util.TimelineScreen
import com.bigbigdw.moavara.theme.*

@Composable
fun MainScreenView(
    callbackAdmin: () -> Unit,
    callbackOption: () -> Unit,
    callbackSearch: () -> Unit,
    viewModelBestList: ViewModelBestList
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = { MainTopBar(callbackAdmin, callbackOption, callbackSearch) },
        bottomBar = { BottomNavigation(navController = navController, currentRoute = currentRoute) }
    ) {
        Box(Modifier.padding(it).background(color = backgroundType1).fillMaxSize()){
            NavigationGraph(navController = navController, viewModelBestList)
        }
    }
}

@Composable
fun MainTopBar(callbackAdmin : () -> Unit, callbackOption : () -> Unit, callbackSearch : () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(color = backgroundType4)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.moabara_logo),
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(22.dp)
                    .clickable { callbackAdmin() }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_search_24px),
            contentDescription = null,
            modifier = Modifier
                .width(22.dp)
                .height(22.dp)
                .clickable { callbackSearch() }
        )

        Spacer(modifier = Modifier
            .wrapContentWidth()
            .width(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_settings_wt_24px),
            contentDescription = null,
            modifier = Modifier
                .width(22.dp)
                .height(22.dp)
                .clickable { callbackOption() }
        )
    }
}

@Composable
fun BottomNavigation(navController: NavHostController, currentRoute: String?) {
    val items = listOf(
        BottomNavItem.BEST,
        BottomNavItem.EVENT,
        BottomNavItem.PICK,
        BottomNavItem.QUICKSEARCH,
        BottomNavItem.COMMUNITY,
    )

    BottomNavigation(
        backgroundColor = backgroundType4,
        contentColor = backgroundType2
    ) {

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = if (currentRoute == item.screenRoute) {
                            painterResource(id = item.iconOn)
                        } else {
                            painterResource(id = item.iconOff)
                        },
                        contentDescription = item.title,
                        modifier = Modifier
                            .width(26.dp)
                            .height(26.dp)
                    )
                },
                label = { Text( text = item.title, fontSize = 13.sp, fontFamily = pretendardvariable) },
                selected = currentRoute == item.screenRoute,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(val title: String, val iconOn: Int, val iconOff: Int, val screenRoute: String) {
    object BEST : BottomNavItem("베스트", R.drawable.ic_best_vt_24px, R.drawable.ic_best_gr_24px, "BEST")
    object EVENT : BottomNavItem("이벤트", R.drawable.ic_event_vt_24px, R.drawable.ic_event_gr_24px, "Event")
    object PICK : BottomNavItem("PICK", R.drawable.ic_push_pin_vt_24px, R.drawable.ic_push_pin_gr_24px, "PICK")
    object QUICKSEARCH : BottomNavItem("퀵서치", R.drawable.ic_barcode_search_vt_24px, R.drawable.ic_barcode_search_gr_24px, "QUICKSEARCH")
    object COMMUNITY : BottomNavItem("커뮤니티", R.drawable.ic_community_vt_24px, R.drawable.ic_community_gr_24px, "COMMUNITY")
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModelBestList : ViewModelBestList) {
    NavHost(navController = navController, startDestination = BottomNavItem.BEST.screenRoute) {
        composable(BottomNavItem.BEST.screenRoute) {
            BestListScreen(viewModelBestList)
        }
        composable(BottomNavItem.EVENT.screenRoute) {
            TimelineScreen()
        }
        composable(BottomNavItem.PICK.screenRoute) {
            AnalysisScreen()
        }
        composable(BottomNavItem.QUICKSEARCH.screenRoute) {
            SettingsScreen()
        }
        composable(BottomNavItem.COMMUNITY.screenRoute) {
            CalendarScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = { MainTopBar({}, {}, {}) },
        bottomBar = { BottomNavigation(navController = navController, currentRoute = currentRoute) }
    ) {
        Box(Modifier.padding(it)){
            NavigationGraph(navController = navController, ViewModelBestList())
        }
    }
}