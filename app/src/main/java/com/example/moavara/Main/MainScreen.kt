package com.example.moavara.Main

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit


@Composable
fun MainScreen(
    state: SplashState,
    onFetchClick: () -> Unit
) {
    if (state.error != null) {
        if(!state.loading){
            EmptyScreen(onFetchClick)
        }
    } else {
        EmptyScreen(onFetchClick)
//        UserListScreen(state.users)
    }

    if (state.loading) {
        LoadingProgressBar()
    }

}

@Preview
@Composable
fun PreviewEmptyScreen(){
    EmptyScreen {}
}

@Composable
fun EmptyScreen(onFetchClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(onClick = onFetchClick) {
            Text(text = "Fetch")
        }
    }
}

@Composable
fun LoadingProgressBar() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

//@Composable
//fun UserListScreen(users: List<User>) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        items(
//            items = users,
//            key = { item: User -> item.id },
//            itemContent = { user: User ->
//                Column {
//                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
//                        AsyncImage(
//                            modifier = Modifier
//                                .size(100.dp)
//                                .padding(8.dp),
//                            model = user.avatar,
//                            contentDescription = null
//                        )
//                        Column() {
//                            Text(
//                                text = user.name,
//                                fontSize = 25.dp.toTextUnit(),
//                                fontWeight = FontWeight.Bold,
//                                color = Color.Black
//                            )
//                            Spacer(modifier = Modifier.size(10.dp))
//                            Text(
//                                text = user.email,
//                                fontSize = 20.dp.toTextUnit(),
//                                color = Color.DarkGray
//                            )
//                        }
//
//                    }
//                    Divider(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 10.dp, vertical = 2.dp))
//                }
//
//            }
//        )
//    }
//}

@Composable
fun Dp.toTextUnit(): TextUnit = with(LocalDensity.current) { toSp() }