package com.example.currencyconverter

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.currencyconverter.entities.Screens
import com.example.currencyconverter.model.ConverterViewModel

@Composable
fun mainScreen(
    navController: NavController,
    viewModel: ConverterViewModel = hiltViewModel()
) {


    Surface(
        modifier = Modifier.fillMaxSize() .verticalScroll(rememberScrollState()),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
                    .padding(10.dp),
                text = "Currency converter",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.height(150.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(

                ) {

                    CurrencyDropDown("from", viewModel)
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(modifier = Modifier
                        .width(140.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        value = viewModel.amountLiveData.observeAsState()?.value.toString(),
                        onValueChange = { newValue ->
                            viewModel.amountLiveData.value = newValue
                            if (newValue != "") {
                                viewModel.convertCurrencies()
                            }

                        })

                }
                Button(modifier = Modifier.padding(10.dp), onClick = {
                    viewModel.swapCurrencies()
                    viewModel.convertCurrencies()
                }) {
                    Image(
                        painter = painterResource(R.drawable.ic_swap),
                        contentDescription = "Convertion_foreground"
                    )
                }
                Column(

                ) {

                    CurrencyDropDown("to", viewModel)
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(modifier = Modifier
                        .width(140.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        value = viewModel.convertedAmountLiveData.observeAsState()?.value.toString(),
                        onValueChange = { newValue ->
                            viewModel.convertedAmountLiveData.value = newValue
                            if (newValue != "") {
                                viewModel.convertCurrencies(true)
                            }

                        })
                }

            }
            Spacer(modifier = Modifier.height(100.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                viewModel.doNavigate.observeAsState().value?.let {
                    Button(modifier = Modifier.padding(10.dp), enabled = it, onClick = {
                        navController.navigate("${Screens.detailsScreen.route}?from=${viewModel.fromLiveData.value}&to=${viewModel.toLiveData.value}")
                    }) {
                        Text(text = "Details")
                    }
                }
            }


        }

    }
}

@Composable
fun CurrencyDropDown(type: String, viewModel: ConverterViewModel = hiltViewModel()) {

    var mExpanded by remember { mutableStateOf(false) }

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(
        modifier = Modifier.width(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        if (type == "from") {

            viewModel.fromLiveData.observeAsState().value?.let {

                OutlinedTextField(
                    readOnly = true,
                    value = it,
                    onValueChange = { newValue -> viewModel.fromLiveData.value = newValue },
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .clickable { mExpanded = !mExpanded },
                    label = { Text(fontSize = 13.sp, text = "Currency") },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable {
                                mExpanded = !mExpanded

                            })
                    }
                )
            }
        } else {
            viewModel.toLiveData.observeAsState().value?.let { it ->
                OutlinedTextField(
                    value = it,
                    onValueChange = { newValue -> viewModel.toLiveData.value = newValue },
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .clickable { mExpanded = !mExpanded },
                    label = { Text(fontSize = 13.sp, text = "Currency") },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable {
                                mExpanded = !mExpanded

                            })
                    }, readOnly = true
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        DropdownMenu(
            modifier = Modifier

                .height(200.dp)
                .width(140.dp),
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false }) {
            viewModel.currenciesLiveData.observeAsState().value?.let {

                it.forEach { value ->
                    DropdownMenuItem(onClick = {
                        if (type == "from") {
                            viewModel.fromLiveData.value = value
                        } else {
                            viewModel.toLiveData.value = value
                        }
                        mExpanded = false
                    }) {
                        Text(text = value)
                    }
                }
            }


        }
    }

}