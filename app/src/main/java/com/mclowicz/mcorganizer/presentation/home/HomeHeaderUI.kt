package com.mclowicz.mcorganizer.presentation.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.mclowicz.mcorganizer.presentation.PagerScreen
import com.mclowicz.mcorganizer.presentation.welcome.OnBoardingPage

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun HomeHeaderUI(
    
) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third
    )
    val pagerState = rememberPagerState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .padding(8.dp)
            .clickable(
                onClick = {

                }
            ),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            HorizontalPager(
                modifier = Modifier.weight(10f),
                count = 3,
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { position ->
                PagerScreen(onBoardingPage = pages[position])
            }
            HorizontalPagerIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f),
                pagerState = pagerState
            )
        }
    }
}