package com.example.currencyconverter

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.currencyconverter.entities.Screens
import com.example.currencyconverter.model.ConverterViewModel

@Composable
fun Navigation(viewModel: ConverterViewModel = hiltViewModel()){

    val navController = rememberNavController()
    NavHost(navController=navController , startDestination =Screens.mainScreen.route ){
        composable(route = Screens.mainScreen.route){
            mainScreen(navController = navController)
        }
        composable(route = Screens.detailsScreen.route,arguments = listOf(

            navArgument("from"){
                type= NavType.StringType
                defaultValue = "USD"
            } ,navArgument("to"){
                type= NavType.StringType
                defaultValue = "EGP"
            }
        )){

            DetailsScreen(from = it.arguments?.getString("from"),to = it.arguments?.getString("to"))
        }
    }
}