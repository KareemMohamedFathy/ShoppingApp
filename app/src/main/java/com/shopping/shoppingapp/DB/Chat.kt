package com.shopping.shoppingapp.DB

data class Chat(
    var message:String="",
    var chat_id:String="",
    var time:String="",
    var images :ArrayList<String> =ArrayList(),
    var tags:ArrayList<String> =ArrayList(),
    var shop_id:String="",
    var user_id: String="",
    var sender:String="",
    var messagReply:Int=-1
)