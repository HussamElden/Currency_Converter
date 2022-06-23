package com.example.currencyconverter.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val conversionrepo: ConversionRepo) :
    ViewModel() {



    val fromLiveData = MutableLiveData<String>("USD")
    val toLiveData = MutableLiveData<String>("EGP")
    val LastThreeDaysHistory = MutableLiveData<List<List<String>>>()
    val currencyToOthers = MutableLiveData<List<List<String>>>()


    fun getLastThreedays() = CoroutineScope(Dispatchers.Main).launch {
        val calender = Calendar.getInstance();
        val calender2 = Calendar.getInstance();

        calender.add(Calendar.DATE, -2);
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        var response = conversionrepo.currencyHistory(
            fromLiveData?.value.toString(),
            toLiveData?.value.toString(),
            formatter.format(calender.time),
            formatter.format(calender2.time)
        )
        if (response.isSuccessful) {

            val body = JSONObject(response.body().toString())
            val rates = body.getJSONObject("rates")
            val dataArray: MutableList<MutableList<String>> = mutableListOf(mutableListOf<String>())
            val key = rates.keys()

            key.forEach {

                dataArray.add(
                    element = mutableListOf<String>(
                        it,
                        rates.getJSONObject(it)
                            .get(toLiveData?.value.toString())
                            .toString()
                    )
                )
            }
            dataArray.removeAt(0)
            LastThreeDaysHistory.value = dataArray


        }
    }

    fun getCurrencyToOthers() = CoroutineScope(Dispatchers.Main).launch {
        var response = conversionrepo.currencyLatestToOters(fromLiveData?.value.toString())
        if (response.isSuccessful) {

            val body = JSONObject(response.body().toString())

            val dataArray: MutableList<MutableList<String>> = mutableListOf(mutableListOf<String>())
            val rates = body.getJSONObject("rates")
            val key = rates.keys()
            key.forEach {

                dataArray.add(
                    element = mutableListOf<String>(
                        it,
                        rates.get(it)

                            .toString()
                    )
                )
            }
            dataArray.removeAt(0)
            currencyToOthers.value = dataArray
        }
    }
}