package com.example.moavara.Main.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moavara.R

@Composable
fun MainScreenView() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        Box(Modifier.padding(it)){
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf<BottomNavItem>(
        BottomNavItem.BEST,
        BottomNavItem.EVENT,
        BottomNavItem.PICK,
        BottomNavItem.QUICKSEARCH,
        BottomNavItem.COMMUNITY,
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xFF3F414E)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

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
                label = { Text( text = item.title, fontSize = 9.sp) },
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
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.BEST.screenRoute) {
        composable(BottomNavItem.BEST.screenRoute) {
            CalendarScreen()
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


@Composable
fun CalendarScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
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

@Composable
fun TimelineScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primaryVariant)
    ) {
        Text(
            text = stringResource(id = R.string.text_timeline),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun AnalysisScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
    ) {
        Text(
            text = stringResource(id = R.string.text_analysis),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondaryVariant)
    ) {
        Text(
            text = stringResource(id = R.string.text_settings),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalendarScreen()
}