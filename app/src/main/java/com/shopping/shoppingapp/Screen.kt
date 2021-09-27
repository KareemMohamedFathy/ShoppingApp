package com.shopping.shoppingapp

import android.util.Log

sealed class Screen(val route:String){
    object Register:Screen("Register_screen")
    object Login:Screen("Login_screen")
    object CreateShop:Screen("Create_screen")
}
