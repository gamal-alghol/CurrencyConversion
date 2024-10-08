package com.gamal.currencyconversion.ui.HomeScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gamal.currencyconversion.R
import com.gamal.currencyconversion.allCurrencyScreen.RetrySection
import com.gamal.currencyconversion.allCurrencyScreen.AllCurrencyScreen
import com.gamal.currencyconversion.data.local.DataStoreHelper
import com.gamal.currencyconversion.data.model.CurrencyConversion
import com.gamal.currencyconversion.data.model.SaveConvertCurrencys
import com.gamal.currencyconversion.ui.theme.bgColor
import com.gamal.currencyconversion.ui.theme.bgColor1
import com.gamal.currencyconversion.ui.theme.bgTextColor
import com.gamal.currencyconversion.ui.theme.deleteColor
import com.gamal.currencyconversion.ui.theme.dividerColor
import com.gamal.currencyconversion.ui.theme.primaryColor
import com.gamal.currencyconversion.ui.theme.hintTextColor
import com.gamal.currencyconversion.ui.HomeScreen.viewModel.CurrencyConvertedViewModel
import com.gamal.currencyconversion.ui.HomeScreen.viewModel.VarViewModel
import com.gamal.currencyconversion.ui.intent.CurrencyViewIntent
import com.gamal.currencyconversion.ui.theme.MyCustomFont
import com.gamal.currencyconversion.util.ProjectConstants
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun HomeScreen(navController: DestinationsNavigator){
    val dataStore= DataStoreHelper(LocalContext.current)
    val varViewModel: VarViewModel =viewModel()
    GetConvertedCurrency(dataStore, CurrencyConvertedViewModel(),navController,varViewModel)
}


@Composable
fun BottomSheetCurrency(navController: DestinationsNavigator, varViewModel: VarViewModel) {
    val  bottomSheetOpenType by varViewModel.bottomSheetOpenType.collectAsStateWithLifecycle("")
    val  editCurrency by varViewModel.editCurrency.collectAsStateWithLifecycle(0)
    AllCurrencyScreen(navController = navController, screen = bottomSheetOpenType, editCurrency = editCurrency)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardConvert(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    dataStore: DataStoreHelper,
    value: List<CurrencyConversion?>,
    varViewModel: VarViewModel,

    ) {
    Card (modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor =Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ){
        Column {
            var amount by remember { mutableStateOf("1") }

            BaseCurrency( scope,scaffoldState,dataStore,amount,varViewModel){ newText ->
                amount = newText
            }
            Divider()
            convertedAmount(scope,scaffoldState,dataStore,value, amount, context = LocalContext.current,varViewModel)

        }

    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberSwipeDismissBoxState(
    context: Context,
    currency: SaveConvertCurrencys,
    dataStore: DataStoreHelper,
    scope: CoroutineScope,
    varViewModel: VarViewModel,
): SwipeToDismissBoxState {
    val  size by varViewModel.size.collectAsStateWithLifecycle(0)

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            Log.d("ttt", size.toString())
            if (dismissValue == SwipeToDismissBoxValue.StartToEnd&&size>1) {
                scope.launch {
                    dataStore.deleteConvertCurrency(currency)
                    Toast.makeText(context, "${currency.code} deleted", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
    )

    return dismissState

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun convertedAmount(

    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    dataStore: DataStoreHelper,
    value: List<CurrencyConversion?>,
    amount: String,
    context: Context,
    varViewModel: VarViewModel,
) {
    Text(modifier = Modifier.padding(top = 24.dp, start = 24.dp),fontFamily = MyCustomFont(),text = stringResource(id = R.string.ConvertedAmount), color = hintTextColor, fontSize = 15.sp)

    val convertCurrencysList by dataStore.getConvertCurrencys.collectAsStateWithLifecycle( emptyArray())
    val baseCurrency= dataStore.getBaseCurrency.collectAsStateWithLifecycle( "").value
varViewModel.setSize( convertCurrencysList.size)

    LazyColumn(state = rememberLazyListState()) {

        items(
            items = convertCurrencysList,
            key = { it.code } // Unique key for each item based on its code
        ) { currencyItem ->
            val currency by rememberUpdatedState(currencyItem)
            val dismissState = RememberSwipeDismissBoxState(context = context, currency = currency, dataStore = dataStore, scope =scope,varViewModel)

            SwipeToDismissBox(
                state =dismissState,
                enableDismissFromEndToStart=false,
                backgroundContent = { DismissBackground(dismissState) },
                content = {
                    Column {
                        val convertCurrency = value.find { it!!.currencyCode == currency.code}
                        Row (modifier = Modifier.padding(top = 8.dp, start = 24.dp), verticalAlignment = Alignment.CenterVertically){

                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current).data(currency.icon).crossfade(true).crossfade(500).build(),
                                contentDescription = "",
                                onSuccess = {success->
                                    val drawable = success.result.drawable
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(50.dp)
                            )

                            Text(modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    scope.launch {
                                        varViewModel.setBottomSheetOpenType(ProjectConstants.BOTTOM_SHEET_SCREEN_CONVERT_CURRENCY)
                                        varViewModel.setEditCurrency( convertCurrencysList.indexOf(currency))
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                },fontFamily = MyCustomFont(),text = currency.code, color = primaryColor, textAlign = TextAlign.Center, fontSize = 18.sp)
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "", tint =hintTextColor,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        scope.launch {
                                            varViewModel.setEditCurrency( convertCurrencysList.indexOf(currency))
                                            varViewModel.setBottomSheetOpenType(ProjectConstants.BOTTOM_SHEET_SCREEN_CONVERT_CURRENCY)
                                            scaffoldState.bottomSheetState.expand()
                                        }
                                    })


                            Text(text = (String.format("%.2f", (convertCurrency?.conversionRate?:1.0)*(amount.toDoubleOrNull()?:1.0)).toString()),  fontSize =  18.sp, modifier = Modifier
                                .padding(start = 24.dp, end = 24.dp)
                                .background(bgTextColor, RoundedCornerShape(12.dp))
                                .padding(16.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)))
                        }
                        Text(text = "1 ${baseCurrency} = ${String.format("%.2f",convertCurrency?.conversionRate)}  ${convertCurrency?.currencyCode}",  fontSize =  14.sp, color = Color.Black, modifier = Modifier.padding(start = 24.dp))
                        Spacer(modifier = Modifier.height(36.dp))
                    }

                })


        }

    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> deleteColor
        SwipeToDismissBoxValue.Settled -> Color.Transparent
        SwipeToDismissBoxValue.EndToStart -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(32.dp),

        ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "delete",
            tint=Color.White
        )

    }
}
@Composable
fun GetConvertedCurrency(
    dataStore: DataStoreHelper,
    viewModel: CurrencyConvertedViewModel,
    navController: DestinationsNavigator,
    varViewModel: VarViewModel,

    ) {

    val state by viewModel.viewState.collectAsStateWithLifecycle()
    var baseCurrency= dataStore.getBaseCurrency.collectAsStateWithLifecycle( "").value
    LaunchedEffect(baseCurrency) {
        viewModel.sendIntent(CurrencyViewIntent.getConvertCurrency(baseCurrency = baseCurrency))
    }
    when {
        state.Loading -> {

            Row (horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)){
                CircularProgressIndicator(modifier = Modifier.size(36.dp))

            }
        }

        state.currenciesConverted.isNotEmpty() -> {

            ShowUi( viewModel.currencyConvertdList.value,navController,dataStore,varViewModel)

        }
        state.error != null -> {

            RetrySection(error = state.error.toString()) {

                viewModel.sendIntent(CurrencyViewIntent.getConvertCurrency(baseCurrency))
            }
        }
    }



}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowUi(
    value: List<CurrencyConversion?>,
    navController: DestinationsNavigator,
    dataStore: DataStoreHelper,
    varViewModel: VarViewModel,

    ) {

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { BottomSheetCurrency(navController,varViewModel) },
        sheetPeekHeight = 0.dp
    ) {
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(bgColor, bgColor1)))
                .padding(24.dp)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxSize()
            ) {

                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    fontFamily = MyCustomFont(),
                    text = stringResource(id = R.string.CurrencyConverter),
                    color = primaryColor,
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    fontFamily = MyCustomFont(),
                    text = stringResource(id = R.string.Checkliverates),
                    color = hintTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CardConvert( scope, scaffoldState, dataStore, value,varViewModel)
                    Image(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (20).dp)
                            .clickable {

                                scope.launch {
                                    varViewModel.setBottomSheetOpenType(ProjectConstants.BOTTOM_SHEET_SCREEN_ADD)
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }
                    )

                }
            }

        }
    }
}



@Composable
fun Divider() {
    Box(modifier = Modifier.padding(top = 24.dp)){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .align(Alignment.Center)
            .padding(start = 24.dp, end = 24.dp)
            .background(color = dividerColor))

        Image(painter = painterResource(id = R.drawable.divid_convert) , contentDescription ="", modifier = Modifier.align(Alignment.Center)  )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseCurrency(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    dataStore: DataStoreHelper,
    amount: String,
    varViewModel: VarViewModel,
    onAmountChange: (String) -> Unit,

    ) {
    Text(modifier = Modifier.padding(top = 24.dp, start = 24.dp),fontFamily = MyCustomFont(),text = stringResource(id = R.string.BaseCurrency), color = hintTextColor, fontSize = 15.sp)
    Row (modifier = Modifier.padding(top = 8.dp, start = 24.dp), verticalAlignment = Alignment.CenterVertically){
        val baseCurrencyImageState = dataStore.getBaseCurrencyImage.collectAsState(initial = "")
        val baseCurrencyImage = baseCurrencyImageState.value
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(baseCurrencyImage).crossfade(true).crossfade(500).build(),
            contentDescription = "",
            onSuccess = {success->
                val drawable = success.result.drawable
            },
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)


        )
        val baseCurrency= dataStore.getBaseCurrency.collectAsState(initial = "").value


        Text(modifier = Modifier
            .padding(start = 8.dp)
            .clickable {
                scope.launch {
                    varViewModel.setBottomSheetOpenType(ProjectConstants.BOTTOM_SHEET_SCREEN_BASE)

                    scaffoldState.bottomSheetState.expand()
                }
            }, fontFamily = MyCustomFont(), text = baseCurrency, color = primaryColor, textAlign = TextAlign.Center, fontSize = 18.sp)
        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "", tint =hintTextColor,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                })


        OutlinedTextField(
            value = amount,
            onValueChange = { newValue -> onAmountChange(newValue) },  // Handle the input change
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(fontSize = 18.sp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),  // Set the keyboard type to number
            label = { Text(stringResource(id = R.string.Amount)) },  // Use placeholder instead of decorationBox
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .padding(start = 8.dp)
        )




    }


}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}