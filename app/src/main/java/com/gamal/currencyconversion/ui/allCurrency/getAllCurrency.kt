package com.gamal.currencyconversion.allCurrencyScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.gamal.currencyconversion.data.model.SaveConvertCurrencys

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gamal.currencyconversion.R
import com.gamal.currencyconversion.data.local.*

import com.gamal.currencyconversion.data.model.Currency
import com.gamal.currencyconversion.ui.theme.dividerColor
import com.gamal.currencyconversion.ui.theme.hintTextColor
import com.gamal.currencyconversion.ui.theme.primaryColor
import com.gamal.currencyconversion.ui.intent.CurrencyViewIntent
import com.gamal.currencyconversion.ui.BaseCurrencyScreen.viewModel.BaseCurrencyViewModel
import com.gamal.currencyconversion.ui.destinations.HomeScreenDestination
import com.gamal.currencyconversion.ui.theme.MyCustomFont
import com.gamal.currencyconversion.util.ProjectConstants
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Composable
fun AllCurrencyScreen(screen: String, viewModel: BaseCurrencyViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: DestinationsNavigator, editCurrency:Int?=null) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(CurrencyViewIntent.getAllCurrency)
    }

    when {
        state.Loading -> {
            Row (horizontalArrangement =Arrangement.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)){
                CircularProgressIndicator(modifier = Modifier.size(36.dp))

            }
        }
        state.currencies.isNotEmpty() -> {

            Column(modifier = Modifier.fillMaxSize()) {
                SearchTextFailed(viewModel)
                CurrencyList(currencies =viewModel.CurrencyList.value,navController,screen,editCurrency)

            }
        }
        state.error != null -> {
            RetrySection(error = state.error.toString()) {
                viewModel.sendIntent(CurrencyViewIntent.getAllCurrency)
            }
        }
    }


}

@Composable
fun CurrencyList(
    currencies: List<Currency>,
    navController: DestinationsNavigator,
    screen: String,
    editCurrency: Int?
) {
    LazyColumn (modifier = Modifier
        .padding(top = 8.dp)
        .fillMaxSize()){
        items(currencies){
            CurrencyEntry(it,navController,screen,editCurrency)
        }
    }

}

@Composable
fun CurrencyEntry(
    currency: Currency,
    navController: DestinationsNavigator,
    screen: String,
    editCurrency: Int?
) {
    val dataStore=DataStoreHelper(LocalContext.current)
Column (
    Modifier
        .fillMaxWidth()
        .clickable {
            navController.popBackStack()

            when (screen) {

                ProjectConstants.BASE_SCREEN,
                ProjectConstants.BOTTOM_SHEET_SCREEN_BASE -> {
                    GlobalScope.launch {
                        dataStore.saveBaseCurrency(currency.currencyCode)
                        dataStore.saveBaseCurrencyImage(currency.icon)
                    }

                }

                ProjectConstants.BOTTOM_SHEET_SCREEN_ADD ->
                    GlobalScope.launch {
                        dataStore.saveAndAddConvertCurrencys(
                            SaveConvertCurrencys(
                                currency.currencyCode,
                                currency.icon
                            )
                        )
                    }

                ProjectConstants.BOTTOM_SHEET_SCREEN_CONVERT_CURRENCY ->
                    GlobalScope.launch {
                        dataStore.editConvertCurrencys(
                            SaveConvertCurrencys(
                                currency.currencyCode,
                                currency.icon
                            ), editCurrency
                        )
                    }


            }
            navController.navigate(HomeScreenDestination)


        }){
    Row (modifier = Modifier
        .padding(top = 12.dp, start = 24.dp)
        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){


        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(currency.icon).crossfade(true).crossfade(500).build(),
            contentDescription = "",
            onSuccess = {success->
                val drawable = success.result.drawable
            },
            placeholder = painterResource(id = R.drawable.logo),
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)    )

        Column {
        Text(modifier = Modifier.padding(start = 16.dp),fontFamily = MyCustomFont(),text = currency.currencyCode, color = primaryColor, textAlign = TextAlign.Center, fontSize = 18.sp)
        Text(modifier = Modifier.padding(start = 16.dp),fontFamily = MyCustomFont(),text = currency.currencyName, color = hintTextColor, textAlign = TextAlign.Center, fontSize = 12.sp)} }



}

    Box(modifier = Modifier
        .padding(top = 8.dp)
        .fillMaxWidth()
        .height(1.dp)
        .padding(start = 24.dp, end = 24.dp)
        .background(color = dividerColor))


    }



@Composable
fun SearchTextFailed(viewModel: BaseCurrencyViewModel) {
    var inputSearch by remember { mutableStateOf("") }
    var isHintDisplay by remember { mutableStateOf(true)    }
    Box (){
        BasicTextField( value = inputSearch,
            onValueChange ={ inputSearch=it
                isHintDisplay=it.isEmpty()
            viewModel.searchPokemonList(it)
            },
            maxLines = 1, singleLine = true ,

            modifier = Modifier
                .padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)


        )
        if (isHintDisplay) {
            Text(text = stringResource(id = R.string.search),color= Color.LightGray  ,  modifier = Modifier.padding(
                top = 38.dp,
                start = 32.dp,)) }


    }


}
@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center){
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}