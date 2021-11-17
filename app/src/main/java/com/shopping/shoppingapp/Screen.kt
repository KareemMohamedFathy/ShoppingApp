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
    object MyProducts : Screen("myProducts")
    object ProductPhotos : Screen("productPhotos")
    object EditProduct : Screen("editProduct")
    object DisplayShops:Screen("displayShops")
    object PrivateChats:Screen("privatechats")
    object AdminHomePage:Screen("adminHomePage")
    object Buyers:Screen("Buyer")
    object ViewPhotos : Screen("viewPhotos")
    object Shops : Screen("displayAllShops")



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
