package com.bigbigdw.moavara.Main.Screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Main.Model.StateRegister
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.Util.LoadingScreen
import com.bigbigdw.moavara.Util.MoavaraAlert
import com.bigbigdw.moavara.theme.*

@Composable
fun RegsisterScreen(
    state: StateRegister,
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
    } else {
        LoadingScreen()
    }
}

@Preview()
@Composable
fun PreviewEmptyScreen(){

    val (getter, setter) = remember { mutableStateOf(DataBaseUser()) }

    RegisterStep2(getter, setter, StateRegister(), {  }, {  })
}


@Composable
fun RegisterHead(state: StateRegister, getter: DataBaseUser) {
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

    if(!state.Step1Finish){
        Text(
            text = "모아바라에 오신것을 환영합니다.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = textColorType3,
            fontFamily = pretendardvariable
        )
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
    }
}

@Composable
fun RegisterStep2(
    getter: DataBaseUser,
    setter: (DataBaseUser) -> Unit,
    state: StateRegister,
    onStep2Finish: () -> Unit,
    onStep2Error: () -> Unit,
) {

    var isShow by remember { mutableStateOf(false) }

    if (isShow) {
        Dialog(
            onDismissRequest = { isShow = false },
        ) {
            MoavaraAlert({ isShow = false }, onStep2Finish ,"취소", "확인", { RegisterAlert(getter) })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundType1)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Card(modifier = Modifier
                .fillMaxSize(),
                backgroundColor = backgroundType2,
                shape = RoundedCornerShape(0.dp, 0.dp, 25.dp, 25.dp)
            ){
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    RegisterHead(state, getter)

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp))

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
                            .height(16.dp)
                    )
                }
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
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )

                GenreButtons(getter, setter)

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundType1),
            contentAlignment = Alignment.BottomEnd

        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    colors = if (getter.Genre.isEmpty()) {
                        ButtonDefaults.buttonColors(backgroundColor = backgroundType3)
                    } else {
                        ButtonDefaults.buttonColors(backgroundColor = colorPrimary)
                    },
                    onClick = if (getter.Genre.isEmpty()) {
                        onStep2Error
                    } else {
                        {isShow = true}
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    shape = RoundedCornerShape(0.dp)

                ) {
                    Text(text = "다음으로", textAlign = TextAlign.Center, color = textColorType3, fontSize = 20.sp, fontFamily = pretendardvariable)
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
        onClick = { setter(DataBaseUser().copy(Nickname = getter.Nickname, Genre = "FANTASY", UID = getter.UID, Email = getter.Email))},
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp)
            .height(48.dp),
        border = if(getter.Genre == "FANTASY"){
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
        onClick = { setter(DataBaseUser().copy(Nickname = getter.Nickname, Genre = "ROMANCE", UID = getter.UID, Email = getter.Email))},
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
        onClick = { setter(DataBaseUser().copy(Nickname = getter.Nickname, Genre = "ALL", UID = getter.UID, Email = getter.Email))},
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
        onClick = { setter(DataBaseUser().copy(Nickname = getter.Nickname, Genre = "BL", UID = getter.UID, Email = getter.Email))},
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
    state: StateRegister,
    onStep1Finish: () -> Unit,
    onStep1Error: () -> Unit
) {
    var isShow by remember { mutableStateOf(false) }

    if (isShow) {
        Dialog(
            onDismissRequest = { isShow = false },
        ) {
            MoavaraAlert({ isShow = false }, onStep1Finish, "뒤로가기", "확인", { RegisterAlert(getter) })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundType1)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Card(modifier = Modifier
                .fillMaxSize(),
                backgroundColor = backgroundType2,
                shape = RoundedCornerShape(0.dp, 0.dp, 25.dp, 25.dp)
            ){
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    RegisterHead(state, getter)

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp))
                }
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
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 0.dp),
                    value = getter.Nickname,
                    onValueChange = { setter(DataBaseUser().copy(Nickname = it, Genre = getter.Genre, UID = getter.UID, Email = getter.Email))},
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
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
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

                Button(
                    colors = if (getter.Nickname.isEmpty()) {
                        ButtonDefaults.buttonColors(backgroundColor = backgroundType3)
                    } else {
                        ButtonDefaults.buttonColors(backgroundColor = colorPrimary)
                    },
                    onClick = if (getter.Nickname.isEmpty()) {
                        onStep1Error
                    } else {
                        { isShow = true }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    shape = RoundedCornerShape(0.dp)

                ) {
                    Text(text = "다음으로", textAlign = TextAlign.Center, color = textColorType3, fontSize = 20.sp, fontFamily = pretendardvariable)
                }
            }
        }
    }
}

@Composable
fun RegisterAlert(getter: DataBaseUser){
    Row(
        Modifier
            .padding(20.dp, 0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "회원정보",
            color = colorPrimary,
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline
        )
        Text(
            text = "를 확인해 주세요.",
            color = textColorType3,
            fontSize = 16.sp
        )
    }

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(24.dp))

    Row(
        Modifier
            .padding(20.dp, 0.dp)
            .wrapContentSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "닉네임 : ",
            color = textColorType3,
            fontSize = 14.sp
        )
        Text(
            text = getter.Nickname,
            color = textColorType3,
            fontSize = 14.sp
        )
    }

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(8.dp))

    var genre = ""

    when (getter.Genre) {
        "ALL" -> {
            genre = "장르 무관"
        }
        "FANTASY" -> {
            genre = "판타지"
        }
        "ROMANCE" -> {
            genre = "로맨스"
        }
        "BL" -> {
            genre = "BL"
        }
    }

    Row(
        Modifier
            .padding(20.dp, 0.dp)
            .wrapContentSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "선호장르 : ",
            color = textColorType3,
            fontSize = 14.sp
        )
        Text(
            text = genre,
            color = textColorType3,
            fontSize = 14.sp
        )
    }

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(32.dp))
}

@Composable
fun FinishAlert(){
    Text(
        modifier = Modifier
            .padding(20.dp, 0.dp)
            .fillMaxWidth(),
        text = "안내",
        color = colorPrimary,
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(12.dp))

    Text(
        modifier = Modifier.padding(20.dp, 0.dp),
        text = "가입을 그만두고 로그인 화면으로 돌아가시겠습니까?",
        color = textColorType3,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(8.dp))

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(32.dp))
}

// 뒤로 가기 두 번 눌렀을 때 앱 종료
@Composable
fun BackOnPressedRegister() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }

    var isShow by remember { mutableStateOf(false) }

    if (isShow) {
        MoavaraAlert({ isShow = false }, { (context as Activity).finish() }, "취소", "확인",{ FinishAlert() })
    }

    BackHandler(enabled = backPressedState) {
        if(!backPressedState) {
            (context as Activity).finish()
        } else {
            isShow = true
        }
    }
}

@Preview()
@Composable
fun PreviewEmptyScreen2(){
    MoavaraAlert({ }, { }, "뒤로가기", "확인", { FinishAlert() })
}