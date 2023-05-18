package com.bigbigdw.moavara.Main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bigbigdw.moavara.DataBase.GuideComponent
import com.bigbigdw.moavara.Main.Model.StateLogin
import com.bigbigdw.moavara.R
import com.bigbigdw.moavara.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState


@Composable
fun CheckLoginScreen(
    state: StateLogin,
    onFetchClick: () -> Unit,
    onFetchRegister: () -> Unit
) {
    LoginScreen(state, onFetchClick, onFetchRegister)
}



@Preview()
@Composable
fun PreviewEmptyScreen2(){
    var guideComponent = ArrayList<GuideComponent>()

    val imgList = intArrayOf(
        R.drawable.coach_mark_img_011,
        R.drawable.coach_mark_img_02,
        R.drawable.coach_mark_img_03,
        R.drawable.coach_mark_img_04,
        R.drawable.coach_mark_img_051,
        R.drawable.coach_mark_img_06,
    )

    val textList = arrayListOf(
        "각 플랫폼 별로 매일 업데이트 되는 이벤트를 확인할 수 있습니다.",
        "각 플랫폼 별로 매일의 작품 순위를 확인할 수 있습니다.",
        "각 플랫폼 별로 매일 업데이트 되는 이벤트를 확인할 수 있습니다.",
        "베스트/이벤트 탭에서 마이픽!한 작품, 이벤트를 확인할 수 있습니다.",
        "플랫폼과 작품코드를 알면 원하는 작품을 빠르게 찾을 수 있어요.",
        "각 플랫폼 별로 업데이트 되는 <조아라> 게시글을 확인해 보세요.",
    )

    for (i in 0..5) {
        guideComponent.add(GuideComponent(
            Img = imgList[i],
            Comment = textList[i],
        ))
    }

    QuesViewPagerDiagnosticResult(guideComponent, {})
}

@Composable
fun DialogLoginScreen(isShow: () -> Unit, onFetchClick: () -> Unit) {

    var checked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { isShow() },
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
                    backgroundColor = color3E424B,
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
                            color = colorEDE6FD,
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
                                checkedColor = color844DF3,
                                uncheckedColor = colorEDE6FD
                            ), checked = checked, onCheckedChange = { checked = !checked })

                            Text(
                                text = "확인했습니다",
                                color = colorEDE6FD,
                                fontSize = 12.sp
                            )
                        }

                        if(!checked){
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp))
                        } else {
                            Button(
                                colors = ButtonDefaults.buttonColors(backgroundColor = color844DF3),
                                onClick = onFetchClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp)

                            ) {
                                Text(text = "다음으로", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
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
fun LoginScreen(state: StateLogin, onFetchClick: () -> Unit, onFetchRegister: () -> Unit) {

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
                .background(color = color1E1E20),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Card(modifier = Modifier
                .fillMaxSize(),
                backgroundColor = color121212,
                shape = RoundedCornerShape(0.dp, 0.dp, 25.dp, 25.dp)
            ){
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
                        colors = ButtonDefaults.buttonColors(backgroundColor = color844DF3),
                        onClick = onFetchClick,
                        modifier = Modifier
                            .width(260.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(50.dp)

                    ) {
                        Text(text = "Google로 로그인", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 16.sp, fontFamily = pretendardvariable)
                    }
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = color1E1E20),
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
                .background(color = color1E1E20),
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
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color1E1E20)
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
                color = colorA7ACB7,
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun QuesViewPagerDiagnosticResult(data: ArrayList<GuideComponent>, moveToResult: () -> Unit) {

    val pageCount = data.size
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color1E1E20),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(count = pageCount, state = pagerState) { page ->

                val (getter, setter) = remember { mutableStateOf(data.get(page)) }

                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp,0.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp))

                            Text(
                                text = getter.Comment,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = colorEDE6FD,
                                fontFamily = pretendardvariable
                            )

                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp))

                            viewPagerIndicator(pageCount, pagerState)

                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd,
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp,0.dp),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                painter = painterResource(id = getter.Img),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = color844DF3),
                onClick = moveToResult,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                shape = RoundedCornerShape(0.dp)

            ) {
                Text(text = "모아바라 시작하기", textAlign = TextAlign.Center, color = colorEDE6FD, fontSize = 20.sp, fontFamily = pretendardvariable)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun viewPagerIndicator(pageCount : Int, pagerState : PagerState){
    Row(
        Modifier
            .height(15.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
    ) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) colorEDE6FD else Color.Transparent

            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(10.dp)
                    .border(1.dp, colorEDE6FD,  CircleShape)

            )
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