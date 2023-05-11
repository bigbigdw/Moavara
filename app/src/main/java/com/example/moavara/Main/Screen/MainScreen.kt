package com.example.moavara.Main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.moavara.Main.Model.LoginState
import com.example.moavara.R
import com.example.moavara.theme.*


@Composable
fun CheckLoginScreen(
    state: LoginState,
    onFetchClick: () -> Unit,
    onFetchRegister: () -> Unit
) {
    LoginScreen(state, onFetchClick, onFetchRegister)
}



@Preview()
@Composable
fun PreviewEmptyScreen2(){
    LoginScreen(LoginState(), {  }, { })
}

@Composable
fun DialogLoginScreen(isShow: () -> Unit, onFetchClick: () -> Unit) {

    var checked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().clickable { isShow() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(260.dp),
        ) {
            Column(
                modifier = Modifier
                    .width(260.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp))

                Card(modifier = Modifier
                    .wrapContentSize(),
                    backgroundColor = backgroundType5,
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                ){

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp))

                    Column(
                        modifier = Modifier
                            .semantics { contentDescription = "Overview Screen" },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp))

                        Text(
                            modifier = Modifier.padding(20.dp, 0.dp),
                            text = "모아바라는 (주)조아라의 사내 스터디에서 개발한 어플리케이션으로 (주)조아라의 임직원만 사용이 가능합니다. 모아바라의 데이터는 사내 자산으로 외부 유출이 불가능하며, 유출 시 법적 책임을 질 수 있습니다.",
                            fontSize = 13.sp,
                            textAlign = TextAlign.Left,
                            color = textColorType3,
                            fontFamily = pretendardvariable
                        )


                        Row(
                            Modifier
                                .padding(20.dp, 0.dp)
                                .wrapContentSize()
                                .toggleable(
                                    value = checked,
                                    role = Role.Checkbox,
                                    onValueChange = { checked = !checked }
                                ),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(colors = CheckboxDefaults.colors(
                                checkedColor = colorPrimary,
                                uncheckedColor = textColorType3
                            ), checked = checked, onCheckedChange = { checked = !checked })

                            Text(
                                text = "확인했습니다",
                                color = textColorType3,
                                fontSize = 12.sp
                            )
                        }

                        if(!checked){
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp))
                        } else {
                            Button(
                                colors = ButtonDefaults.buttonColors(backgroundColor = colorPrimary),
                                onClick = onFetchClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp)

                            ) {
                                Text(text = "다음으로", textAlign = TextAlign.Center, color = textColorType3, fontSize = 16.sp, fontFamily = pretendardvariable)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.width(260.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.moavara_logo_dialog),
                    contentDescription = null,
                    modifier = Modifier
                        .height(70.dp)
                )
            }
        }
    }
}

@Composable
fun BeforeRegisterAlert(onFetchClick: () -> Unit) {
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
fun LoginScreen(state: LoginState, onFetchClick: () -> Unit, onFetchRegister: () -> Unit) {

    var isShow by remember { mutableStateOf(true) }

    if(state.Register) {
        if (isShow) {
            Dialog(
                onDismissRequest = { isShow = false },
            ) {
                DialogLoginScreen({ isShow = false }, onFetchRegister)
            }
        }
    }

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