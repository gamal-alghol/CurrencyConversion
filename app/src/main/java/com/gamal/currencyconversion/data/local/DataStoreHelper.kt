package com.gamal.currencyconversion.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gamal.currencyconversion.data.model.SaveConvertCurrencys
import com.gamal.currencyconversion.util.ProjectConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// DataStore extension property for the context
val Context.dataStore by preferencesDataStore(name = ProjectConstants.DATA_STORE_NAME)

class DataStoreHelper(private val context: Context) {

    // Preference Key
    private val baseCurrencyCode = stringPreferencesKey(ProjectConstants.DATA_STORE_BASE_KEY_CODE)
    private val baseCurrencyImage = stringPreferencesKey(ProjectConstants.DATA_STORE_BASE_KEY_IMAGE)
    private val ConvertCurrencys = stringPreferencesKey(ProjectConstants.CONVERT_CURRENCYS_KEY)
    // Function to save the base currency



    suspend fun saveAndAddConvertCurrencys(itemToSave: SaveConvertCurrencys) {
        val json = context.dataStore.data.first()[ConvertCurrencys] ?: ProjectConstants.INITIAL_CURRENCYS
        val existingArray: Array<SaveConvertCurrencys> = Gson().fromJson(json, Array<SaveConvertCurrencys>::class.java)
        val mutableList = existingArray.toMutableList()
        val itemExists = mutableList.any { it.code == itemToSave.code }
        if (!itemExists) {
            // If the item doesn't exist, add it to the list
            mutableList.add(itemToSave)

            // Convert the updated list back to JSON
            val updatedJson = Gson().toJson(mutableList.toTypedArray())

            // Save the updated JSON back to the data store
            context.dataStore.edit { preferences ->
                preferences[ConvertCurrencys] = updatedJson
            }
        } else {
            // Handle the case where the item already exists, if needed
            Log.d("SaveConvertCurrencys", "Item with code ${itemToSave.code} already exists and will not be added again.")
        }

    }

    suspend fun editConvertCurrencys(itemToEdit: SaveConvertCurrencys, editCurrency: Int?) {
        val json = context.dataStore.data.first()[ConvertCurrencys] ?: ProjectConstants.INITIAL_CURRENCYS
        val existingArray: Array<SaveConvertCurrencys> = Gson().fromJson(json, Array<SaveConvertCurrencys>::class.java)
        existingArray.set(editCurrency!!,itemToEdit)
            // If the item doesn't exist, add it to the list

            // Convert the updated list back to JSON
            val updatedJson = Gson().toJson(existingArray)

            // Save the updated JSON back to the data store
            context.dataStore.edit { preferences ->
                preferences[ConvertCurrencys] = updatedJson
        }

    }

     suspend fun deleteConvertCurrency(itemToDelete: SaveConvertCurrencys) {
        // Retrieve the current JSON array from DataStore
        val json = context.dataStore.data.first()[ConvertCurrencys] ?: ProjectConstants.INITIAL_CURRENCYS


        // Convert the JSON back to an array
        val existingArray: Array<SaveConvertCurrencys> = Gson().fromJson(json, Array<SaveConvertCurrencys>::class.java)
        // Create a new array without the item to delete
        val updatedArray = existingArray.filter { it != itemToDelete }.toTypedArray()

        // Convert the updated array back to JSON
        val updatedJson = Gson().toJson(updatedArray)

        // Save the updated JSON array back to DataStore
        context.dataStore.edit { preferences ->
            preferences[ConvertCurrencys] = updatedJson
        }
    }
    val getConvertCurrencys: Flow<Array<SaveConvertCurrencys>> = context.dataStore.data
        .map { preferences ->
            val json = preferences[ConvertCurrencys] ?: ProjectConstants.INITIAL_CURRENCYS
            if (json.isNotEmpty()) {
                val listType = object : TypeToken<Array<SaveConvertCurrencys>>() {}.type
                Gson().fromJson(json, listType)
            } else {
                emptyArray()
            }
        }
    suspend fun saveBaseCurrency(value: String) {
        context.dataStore.edit { preferences ->
            preferences[baseCurrencyCode] = value
        }
    }  suspend fun saveBaseCurrencyImage(value: String) {
        context.dataStore.edit { preferences ->
            preferences[baseCurrencyImage] = value
        }
    }

    // Flow to get the base currency
    val getBaseCurrency: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[baseCurrencyCode] ?: "usd"
        }
    val getBaseCurrencyImage: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[baseCurrencyImage] ?: "https://currencyfreaks.com/photos/flags/usd.png"
        }

    // Flow to check if the base currency has been chosen
    val isBaseCurrencyChosen: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences.contains(baseCurrencyCode)
        }
}
