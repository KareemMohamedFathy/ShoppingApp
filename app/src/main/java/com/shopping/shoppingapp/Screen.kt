package com.shopping.shoppingapp

import android.util.Log

sealed class Screen(val route:String) {
    object Register : Screen("Register_screen")
    object Login : Screen("Login_screen")
    object CreateShop : Screen("Create_screen")
    object SellerHomePage : Screen("SellerHomePage")
    object AddProduct : Screen("addProductScreen")
    object AddTags : Screen("addTags")
    object MyShop : Screen("myShop")
    object ProductPhotos : Screen("productPhotos")
    object DisplayShops:Screen("displayShops")
    object Admin:Screen("admin")
    object DisplayShopsAdmin:Screen("shops_admin")
    object Buyers:Screen("buyers_display")
    fun withArgs(vararg args: String): String {
        return buildString {

            append(route)
            args.forEach { arg ->
                append("/$arg")
                Log.d("baga",route)

            }
        }
    }
}
