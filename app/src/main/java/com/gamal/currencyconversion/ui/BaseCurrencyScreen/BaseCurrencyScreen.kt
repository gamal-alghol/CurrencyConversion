package com.gamal.currencyconversion.ui.BaseCurrencyScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamal.currencyconversion.R
import com.gamal.currencyconversion.allCurrencyScreen.AllCurrencyScreen
import com.gamal.currencyconversion.ui.theme.MyCustomFont
import com.gamal.currencyconversion.ui.theme.bgColor
import com.gamal.currencyconversion.ui.theme.bgColor1
import com.gamal.currencyconversion.ui.theme.hintTextColor
import com.gamal.currencyconversion.ui.theme.primaryColor
import com.gamal.currencyconversion.util.ProjectConstants
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun BaseCurrencyScreen(navController: DestinationsNavigator){

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(listOf(bgColor, bgColor1)))
        .padding(24.dp)) {

        Text(modifier = Modifier.padding(top = 24.dp),fontFamily = MyCustomFont(),text = stringResource(id = R.string.choseBaseCurrency), color = primaryColor, textAlign = TextAlign.Center, fontSize = 25.sp)
        Text(modifier = Modifier.padding(top = 8.dp),fontFamily =  MyCustomFont(),text = stringResource(id = R.string.can_change_it),color = hintTextColor, textAlign = TextAlign.Center, fontSize =    14.sp)
        AllCurrencyScreen(navController = navController, screen = ProjectConstants.BASE_SCREEN)

    }
}