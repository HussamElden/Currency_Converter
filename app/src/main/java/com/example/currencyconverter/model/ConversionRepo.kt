package com.example.currencyconverter.model

import com.example.currencyconverter.entities.Conversion
import com.example.currencyconverter.entities.symbol
import retrofit2.Response

class ConversionRepo(private val api: ConversionAPI) {

    suspend fun getCurrencies(): Response<symbol> {
        return api.getSymbols()
    }
    suspend fun convertCurrency(from:String,to:String,amount:String): Response<Conversion> {
         return api.convertCurrencys(from=from,to=to, amount = amount)
     }

    suspend fun currencyHistory(base:String,symbols:String,start_date:String,end_date:String): Response<Any> {
         return api.currencyConversionHistory(base=base,symbols=symbols, start_date = start_date, end_date = end_date)
     }
    suspend fun currencyLatestToOters(base:String): Response<Any> {
         return api.getCurrencyToOthers(base=base)
     }
}
