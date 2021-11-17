package com.shopping.shoppingapp.DB

data class ChatReply(
    var sendermessage:String="",
    var recievermessage:String="",
    var messagephoto:String="",
    var chat_id:String="",
    var time:String="",
    var images :ArrayList<String> =ArrayList(),
    var tags:ArrayList<String> =ArrayList(),
    var shop_id:String="",
    var user_id: String=""

)
