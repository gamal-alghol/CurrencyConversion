package com.gamal.currencyconversion.util

object ProjectConstants {
    const val DATA_STORE_NAME="BaseCurrency"
    const val DATA_STORE_BASE_KEY_CODE="BaseCurrencyCode"
    const val DATA_STORE_BASE_KEY_IMAGE="BaseCurrencyImage"
    const val BASE_SCREEN="BaseCurrencyScreen"
    const val BOTTOM_SHEET_SCREEN_ADD="BottomSheetCurrencyAdd"
    const val BOTTOM_SHEET_SCREEN_BASE="BottomSheetCurrencyAddBASE"
    const val BOTTOM_SHEET_SCREEN_CONVERT_CURRENCY="BottomSheetConvertCurrency"
    const val CONVERT_CURRENCYS_KEY="ConvertCurrency's"

    const val INITIAL_CURRENCYS= """
        [
          {"code": "USD", "icon": "https://currencyfreaks.com/photos/flags/usd.png"},
          {"code": "GBP", "icon": "https://currencyfreaks.com/photos/flags/gbp.png"},
          {"code": "EUR", "icon": "https://currencyfreaks.com/photos/flags/eur.png"}
        ]
        """
}