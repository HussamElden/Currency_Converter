package com.example.currencyconverter.entities

sealed class Screens(val route:String){
    object mainScreen : Screens("main_screen")
    object detailsScreen:Screens("details_screen")
}
