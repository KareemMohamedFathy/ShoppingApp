package com.shopping.shoppingapp.DB

data class Product(
    var name:String="",
    var price:String="",
    var description:String="",
    var images :ArrayList<String>? =ArrayList(),
    var shop_id:String="",
    var product_id:String="",
    var tags:ArrayList<String>? =ArrayList(),
    var time:String=""
    )
