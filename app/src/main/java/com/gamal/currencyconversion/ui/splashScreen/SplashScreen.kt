package com.gamal.currencyconversion.ui.splashScreen

import android.content.res.Resources.Theme
import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavOptions
import com.gamal.currencyconversion.R
import com.gamal.currencyconversion.data.local.DataStoreHelper
import com.gamal.currencyconversion.ui.destinations.BaseCurrencyScreenDestination
import com.gamal.currencyconversion.ui.destinations.HomeScreenDestination
import com.gamal.currencyconversion.ui.destinations.SplashScreenDestination

import com.gamal.currencyconversion.ui.theme.bgColor
import com.gamal.currencyconversion.ui.theme.bgColor1
import com.gamal.currencyconversion.ui.theme.primaryColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single


@Destination(start = true)
@Composable
fun SplashScreen(navController: DestinationsNavigator){
    var dataStore= DataStoreHelper(LocalContext.current)

    var scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    val isBaseCurrencyChosen by dataStore.isBaseCurrencyChosen.collectAsStateWithLifecycle(false)
    LaunchedEffect(key1 = true ){

        scale.animateTo(targetValue = .8f, animationSpec = tween(
            durationMillis = 1000,
            easing ={ OvershootInterpolator(2f).getInterpolation(it)}
        ))

        delay(2500L)
        navController.popBackStack(SplashScreenDestination,true)
        if (isBaseCurrencyChosen) {
            navController.navigate(HomeScreenDestination)
        }else{
            navController.navigate(BaseCurrencyScreenDestination)
        }


    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(listOf(bgColor, bgColor1)))){
        Column(
            modifier= Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.dc_logo),    contentDescription ="",
                modifier = Modifier
                    .size(300.dp)
                    .scale(scale.value)
            )

        }


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}