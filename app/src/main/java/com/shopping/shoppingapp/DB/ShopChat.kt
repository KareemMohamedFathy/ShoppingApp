package com.shopping.shoppingapp.DB

data class ShopChat(
    var message:String="",
    var chat_id:String="",
    var time:String="",
    var images :ArrayList<String> =ArrayList(),
    var tags:ArrayList<String> =ArrayList(),
    var shop_id:String=""
    )
