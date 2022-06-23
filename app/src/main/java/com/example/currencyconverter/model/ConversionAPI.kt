package com.example.currencyconverter.model

import com.example.currencyconverter.entities.Conversion
import com.example.currencyconverter.entities.symbol
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface ConversionAPI {

    @GET("symbols")
    suspend fun getSymbols(@Query("apikey") apikey: String = "VXbHH3B6CKWTKxG5crWdaur8B2enjAoS"): Response<symbol>

    @GET("convert")
    suspend fun convertCurrencys(
        @Query("apikey") apikey: String = "VXbHH3B6CKWTKxG5crWdaur8B2enjAoS",
        @Query("to") to: String,
        @Query("from") from: String,
        @Query("amount") amount: String
    ): Response<Conversion>

    @GET("latest")
    suspend fun getCurrencyToOthers(
        @Query("apikey")apikey: String = "VXbHH3B6CKWTKxG5crWdaur8B2enjAoS",
        @Query("base") base: String,
        @Query("symbols") symbols: String="USD%2CEUR%2CSAR%2CAED%2CGBP%2CCAD%2CEGP%2CCHF%2CJPY%2CAUD",
    ): Response<Any>
    @GET("timeseries")
    suspend fun currencyConversionHistory(
        @Query("apikey")apikey: String = "VXbHH3B6CKWTKxG5crWdaur8B2enjAoS",
        @Query("base") base: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("symbols") symbols: String,
    ): Response<Any>


}