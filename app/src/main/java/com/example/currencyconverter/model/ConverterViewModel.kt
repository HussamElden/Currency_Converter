package com.example.currencyconverter.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyconverter.entities.Symbols
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.reflect.full.memberProperties


@HiltViewModel
class ConverterViewModel @Inject constructor( val conversionrepo: ConversionRepo) :
    ViewModel() {
    init {
        this.getCurrncies()
    }

    val currenciesLiveData = MutableLiveData<List<String>>()
    val fromLiveData = MutableLiveData<String>("USD")
    val toLiveData = MutableLiveData<String>("EGP")
    val amountLiveData = MutableLiveData<String>("1")
    val convertedAmountLiveData = MutableLiveData<String>("0")

    val doNavigate = MutableLiveData<Boolean>(false)

    fun getCurrncies() = CoroutineScope(Dispatchers.Main).launch {
        var response = conversionrepo.getCurrencies()

        var currencies = mutableListOf<String>()
        if (response.isSuccessful)
            for (prop in Symbols::class.memberProperties) {
                currencies.add(prop.name)

            }


        currenciesLiveData.value = currencies


    }

    fun swapCurrencies() {
        val temp = toLiveData.value
        toLiveData.value = fromLiveData.value
        fromLiveData.value = temp
    }

    fun convertCurrencies(other:Boolean= false) = CoroutineScope(Dispatchers.Main).launch {
        if (fromLiveData.value.toString() == "" || fromLiveData.value.toString() == null
            || toLiveData.value.toString() == "" || toLiveData.value.toString() == null
            || amountLiveData.value.toString() == "" || amountLiveData.value.toString() == null
        ){
            doNavigate.value=false
            return@launch
        }
        if(other){
            var response = conversionrepo.convertCurrency(
                toLiveData?.value.toString() ,
                fromLiveData?.value.toString(),
                convertedAmountLiveData?.value.toString()
            )

            if (response.isSuccessful) {
                doNavigate.value=true
                var concersionData = response.body()
                amountLiveData.value = concersionData?.result.toString()
            }
        }else{
            var response = conversionrepo.convertCurrency(
                fromLiveData?.value.toString(),
                toLiveData?.value.toString(),
                amountLiveData?.value.toString()
            )

            if (response.isSuccessful) {
                doNavigate.value=true
                var concersionData = response.body()
                convertedAmountLiveData.value = concersionData?.result.toString()
            }
        }


    }


}