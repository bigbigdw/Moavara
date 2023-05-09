package com.example.moavara.Main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.R
import com.example.moavara.theme.*


@Composable
fun LoginScreen(
    state: SplashState,
    onFetchClick: () -> Unit
) {
    if (!state.loading || state.error != null) {
        LoginScreen(onFetchClick)
    } else {
        LoadingProgressBar()
    }
}

@Preview
@Composable
fun PreviewEmptyScreen2(){
    val (getter, setter) = remember { mutableStateOf(DataBaseUser()) }
    var isStep1Finished by remember { mutableStateOf(false) }

    if(isStep1Finished){
        RegisterStep2(getter, setter, isStep1Finished)
    } else {
        RegisterStep1(getter, setter, isStep1Finished) { isStep1Finished = it }
    }
}

@Composable
fun RegisterStep2(
    getter: DataBaseUser,
    setter: (DataBaseUser) -> Unit,
    isStep1Finished: Boolean,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(color = backgroundType2)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterHead(isStep1Finished, getter)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = "장르",
                    color = colorPrimary,
                    fontSize = 18.sp
                )
                Text(
                    text = "를 선택해주세요",
                    color = textColorType3,
                    fontSize = 18.sp
                )

            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            )

            GenreButtons(getter, setter)

        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundType1)
                .align(Alignment.BottomStart)) {
                Button(
                    colors = if (getter.Genre.isEmpty()) {
                        ButtonDefaults.buttonColors(backgroundColor = backgroundType3)
                    } else {
                        ButtonDefaults.buttonColors(backgroundColor = colorPrimary)
                    },
                    onClick = if (getter.Genre.isEmpty()) {
                        {  }
                    } else {
                        {  }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    shape = RoundedCornerShape(0.dp)

                ) {
                    Text(text = "다음으로", textAlign = TextAlign.Center, color = textColorType3, fontSize = 16.sp, fontFamily = pretendardvariable)
                }
            }
        }
    }
}

@Composable
fun GenreButtons(
    getter: DataBaseUser,
    setter: (DataBaseUser) -> Unit
) {
    OutlinedButton(
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundType4),
        onClick = { setter(DataBaseUser().copy(Nickname = getter.Nickname, Genre = "FANTAGY")) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp)
            .height(48.dp),
        border = if(getter.Genre == "FANTAGY"){
            BorderStroke(width = 1.dp, color = colorPrimary)
        } else {
           null
        },
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(text = "판타지", textAlign = TextAlign.Center, color = textColorType3, fontSize = 16.sp, fontFamily = pretendardvariable)
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    )

    OutlinedButton(
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundType4),
        onClick = { setter(DataBaseUser().copy(Nickname = getter.Nickname, Genre = "ROMANCE")) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp)
            .height(48.dp),
        border = if(getter.Genre == "ROMANCE"){
            BorderStroke(width = 1.dp, color = colorPrimary)
        } else {
            null
        },
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(text = "로맨스", textAlign = TextAlign.Center, color = textColorType3, fontSize = 16.sp, fontFamily = pretendardvariable)
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    )

    OutlinedButton(
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundType4),
        onClick = { setter(DataBaseUser().copy(Nickname = getter.Nickname, Genre = "ALL")) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp)
            .height(48.dp),
        border = if(getter.Genre == "ALL"){
            BorderStroke(width = 1.dp, color = colorPrimary)
        } else {
            null
        },
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(text = "장르무관", textAlign = TextAlign.Center, color = textColorType3, fontSize = 16.sp, fontFamily = pretendardvariable)
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    )

    OutlinedButton(
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundType4),
        onClick = { setter(DataBaseUser().copy(Nickname = getter.Nickname, Genre = "BL")) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp)
            .height(48.dp),
        border = if(getter.Genre == "BL"){
            BorderStroke(width = 1.dp, color = colorPrimary)
        } else {
            null
        },
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(text = "BL", textAlign = TextAlign.Center, color = textColorType3, fontSize = 16.sp, fontFamily = pretendardvariable)
    }
}
@Composable
fun RegisterStep1(
    getter: DataBaseUser,
    setter: (DataBaseUser) -> Unit,
    isStep1Finished: Boolean,
    isStep1FinishedCallback: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(color = backgroundType2)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterHead(isStep1Finished, getter)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 0.dp),
                value = getter.Nickname,
                onValueChange = { setter(DataBaseUser().copy(Nickname = it)) },
                label = {
                    Text(
                        text = "닉네임을 입력해 주세요",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = textColorType3,
                        fontFamily = pretendardvariable
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0),
                    textColor = textColorType1
                )
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundType1)
                .align(Alignment.BottomStart)) {
                Button(
                    colors = if (getter.Nickname.isEmpty()) {
                        ButtonDefaults.buttonColors(backgroundColor = backgroundType3)
                    } else {
                        ButtonDefaults.buttonColors(backgroundColor = colorPrimary)
                    },
                    onClick = if (getter.Nickname.isEmpty()) {
                        { }
                    } else {
                        { isStep1FinishedCallback(true) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    shape = RoundedCornerShape(0.dp)

                ) {
                    Text(text = "다음으로", textAlign = TextAlign.Center, color = textColorType3, fontSize = 16.sp, fontFamily = pretendardvariable)
                }
            }
        }
    }
}

@Composable
fun RegisterHead(isStep1Finished: Boolean, getter: DataBaseUser) {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(163.dp))
    Image(
        painter = painterResource(id = R.mipmap.ic_launcher),
        contentDescription = null,
        modifier = Modifier
            .width(72.dp)
            .height(72.dp)
    )
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(8.dp))

    if(!isStep1Finished){
        Text(
            text = "모아바라에 오신것을 환영합니다.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = textColorType3,
            fontFamily = pretendardvariable
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(8.dp))
    } else {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = getter.Nickname,
                color = colorPrimary,
                fontSize = 18.sp
            )
            Text(
                text = "님 환영합니다.",
                color = textColorType3,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp))
    }
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
fun LoginScreen(onFetchClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .background(color = backgroundType1),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Card(modifier = Modifier
                .fillMaxSize(),
                backgroundColor = backgroundType2,
                shape = RoundedCornerShape(0.dp, 0.dp, 25.dp, 25.dp)
            ){

            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier
                        .width(72.dp)
                        .height(72.dp)
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.moabara_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .width(110.dp)
                        .height(24.dp)
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorPrimary),
                    onClick = onFetchClick,
                    modifier = Modifier
                        .width(260.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(50.dp)

                ) {
                    Text(text = "Google로 로그인", textAlign = TextAlign.Center, color = textColorType3, fontSize = 16.sp, fontFamily = pretendardvariable)
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundType1),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp))
                Text(
                    text = "조아라 구글계정만 가입이 가능합니다. \n다른 계정으로 가입 시 임의로 삭제될 수 있습니다.",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = textColorType2,
                    fontFamily = pretendardvariable
                )

            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(color = backgroundType1),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "© Joara Corp",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = textColorType2,
                    fontFamily = pretendardvariable
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(23.dp))
            }
        }
    }
}
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(color = backgroundType1)
                .fillMaxSize()
                .background(color = backgroundType1)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .width(72.dp)
                    .height(72.dp)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            Text(
                text = "로딩 중...",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = textColorType1,
                fontFamily = pretendardvariable
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            CircularProgressIndicator()
        }

    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(color = backgroundType1)
                .fillMaxSize()
                .background(color = backgroundType1)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .width(72.dp)
                    .height(72.dp)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.moabara_logo),
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(24.dp)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(22.dp))
            Text(
                text = "당신의 하루를 모아바라가 응원합니다.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = textColorType1,
                fontFamily = pretendardvariable
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "By 김대우, 유성아, 박주은",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = textColorType2,
                fontFamily = pretendardvariable
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))
            Text(
                text = "© Joara Corp",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = textColorType2,
                fontFamily = pretendardvariable
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp))
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