package com.example.moavara.Main.Screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Main.Model.RegiserState
import com.example.moavara.R
import com.example.moavara.theme.*

@Composable
fun RegsisterScreen(
    state: RegiserState,
    onStep1Finish: () -> Unit,
    onStep1Error: () -> Unit,
    onStep2Finish: () -> Unit,
    onStep2Error: () -> Unit,
    getter: DataBaseUser,
    setter: (DataBaseUser) -> Unit,
) {
    if(state.BeginRegister){
        RegisterStep1(getter, setter, state, onStep1Finish, onStep1Error)
    } else if(state.Step1Finish || state.Step2Finish){
        RegisterStep2(getter, setter, state, onStep2Finish, onStep2Error)
    }
}

@Composable
fun RegisterHead(state: RegiserState, getter: DataBaseUser) {
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

    if(!state.Step2Finish){
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
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline
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
fun RegisterStep2(
    getter: DataBaseUser,
    setter: (DataBaseUser) -> Unit,
    state: RegiserState,
    onStep2Finish: () -> Unit,
    onStep2Error: () -> Unit,
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
            RegisterHead(state, getter)
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
                    fontSize = 18.sp,
                    textDecoration = TextDecoration.Underline
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
                        onStep2Error
                    } else {
                        onStep2Finish
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
    state: RegiserState,
    onStep1Finish: () -> Unit,
    onStep1Error: () -> Unit
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
            RegisterHead(state, getter)
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
                        onStep1Error
                    } else {
                        onStep1Finish
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