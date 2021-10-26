package com.shopping.shoppingapp.DisplayShops

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shopping.shoppingapp.DB.Chat
import com.shopping.shoppingapp.DB.Shop
import com.shopping.shoppingapp.DB.ShopChat
import com.shopping.shoppingapp.SellerHomePage.MyShop.FValueEventListener
import com.shopping.shoppingapp.SellerHomePage.MyShop.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PrivateChatViewModel: ViewModel() {

    var shopName : MutableState<String> = mutableStateOf("")
    var shopId : MutableState<String> = mutableStateOf("")
    var userId : MutableState<String> = mutableStateOf("")
    var shopLogo : MutableState<String> = mutableStateOf("")
//    var productsList : MutableList<Product> = mutableStateListOf<Product>()
    var messagesList : MutableList<Chat> = mutableStateListOf<Chat>()
    var messagesList2 : MutableList<Chat> = mutableStateListOf<Chat>()

    var imageList : MutableList<String> = mutableStateListOf<String>()
    var shopmessagesList : MutableList<Chat> = mutableStateListOf<Chat>()





    suspend fun  getUser(shopid: String) {

        val dbReference = FirebaseDatabase.getInstance().getReference()
        val auth = FirebaseAuth.getInstance()
        val query = dbReference.child("User").orderByChild("user_id").equalTo(auth.currentUser!!.uid)
        val db: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("user_id")
        val snapshot = query.getSnapshotValue()
        if (snapshot.exists()) {
            for (sp in snapshot.children) {
                val arr: ArrayList<Any?> = arrayListOf(sp.value)
                userId.value = sp.child("user_id").value.toString()
            }
        }
        getmessages(shopid)

    }

    suspend fun DatabaseReference.getSnapshotValue(): DataSnapshot {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<DataSnapshot> { continuation ->
                addListenerForSingleValueEvent(FValueEventListener(
                    onDataChange = {
                        continuation.resume(it)
                        val snapshot = it
                    }
                    ,onError = { continuation.resumeWithException(it.toException())},

                    ))}

        }



    }


    suspend fun Query.getSnapshotValue(): DataSnapshot {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<DataSnapshot> { continuation ->
                addListenerForSingleValueEvent(FValueEventListener(
                    onDataChange = {
                        continuation.resume(it)
                        val snapshot = it
                    },
                    onError = { continuation.resumeWithException(it.toException()) },

                    )
                )
            }

        }
    }


    suspend fun getShop(shopid: String){
        val dbReference = FirebaseDatabase.getInstance().getReference()
        val dbref = dbReference.child("Shop")
        val query = dbref.orderByChild("shop_id").equalTo(shopid)
        val snapshot = query.getSnapshotValue()
        if (snapshot.exists()) {
            for (sp in snapshot.children) {
                val arr: ArrayList<Any?> = arrayListOf(sp.value)
                shopName.value = sp.child("name").value.toString()
                shopLogo.value = sp.child("logo").value.toString()
                shopId.value = shopid
            }
        }
//        Log.d("tag2",shopLogo.toString())
//        Log.d("tag",shopid)
        getShopmessages(shopid)


    }


    suspend  fun addChat(message:String){
        return withContext(Dispatchers.IO) {

            //messagesList.clear()
            val firebaseDatabase = FirebaseDatabase.getInstance()
            var dbReference = firebaseDatabase.getReference("Chat")
            val chat_id = dbReference.push().key.toString()
            val chat: Chat = Chat(message.toString(), chat_id, getCurrentDate(),shop_id = shopId.value, user_id = userId.value)
            dbReference.child(chat_id).setValue(chat)
            messagesList.add(chat)

        }



    }

    suspend fun  getShopmessages(shopid: String){
        val dbReference = FirebaseDatabase.getInstance().getReference()
        val dbref = dbReference.child("ShopChat")
        val query = dbref.orderByChild("shop_id").equalTo(shopid)
        val snapshot = query.getSnapshotValue()
        if (snapshot.exists()) {
            for (sp in snapshot.children) {
                val shopmessage = sp.child("message").value.toString()
                val messagetime = sp.child("time").value.toString()
                val shopchatid = sp.child("chat_id").value.toString()
                var shopimageList = sp.child("images").value
                val  tags= sp.child("tags").value
                if(shopimageList!=null) {
                    val chat: Chat = Chat(
                        shopmessage.toString(), shopchatid, messagetime, shopimageList as ArrayList<String>,
                        tags as ArrayList<String>,
                        shopid, ""
                    )
                    shopmessagesList.add(chat)
                }
                else{
                    val chat: Chat = Chat(
                        shopmessage.toString(), shopchatid, messagetime,shop_id = shopid, user_id = "")
                    shopmessagesList.add(chat)
                }

            }
        }

    }


    suspend  fun getmessages(shopid: String) {
        val dbReference = FirebaseDatabase.getInstance().getReference()
        val dbref = dbReference.child("Chat")
        val query = dbref.orderByChild("user_id").equalTo(userId.value)
//        val query = query1.orderByChild("shop_id").equalTo(shopid)
        val snapshot=query.getSnapshotValue()
        if (snapshot.exists()) {
            for (sp in snapshot.children) {
                if (sp.child("shop_id").value.toString().equals(shopid)){
                    val message = sp.child("message").value.toString()
                    val  chat_id= sp.child("chat_id").value.toString()
                    val  shop_id= sp.child("shop_id").value.toString()
                    val  tags= sp.child("tags").value
                    val  time= sp.child("time").value.toString()
                    var   images= sp.child("images").value
                    if(images!=null) {
                        val chat = Chat(
                            message.toString(), chat_id, time, images as ArrayList<String>,
                            tags as ArrayList<String>,
                            shop_id,
                            userId.toString()
                        )
                        messagesList.add(chat)
                    }
                    else{
                        val chat = Chat(
                            message.toString(), chat_id, time, shop_id = shop_id, user_id = userId.toString())
                        messagesList.add(chat)
                    }
                }
                //   val product=Product(productName,productPrice,productDescription,images,shop_id,product_id,tags,time)
                //     productsList.add(product)
            }

        }

    }
    suspend  fun getBoth(){

        messagesList2.addAll(messagesList)
        messagesList2.addAll(shopmessagesList)
        messagesList2.sortBy { it.time }
    }

//    suspend fun sortbytime(msglist : MutableList<Chat>, msgList2 : MutableList<Chat>){
//
//
//        for (item in msglist){
//            val pattern = "dd/M/yyyy hh:mm:ss"
//            val dateformat = SimpleDateFormat(pattern)
//            val date1 = dateformat.parse(item.time)
//
//            for(msg in msgList2){
//                val date2 = dateformat.parse(msg.time)
//
////                Log.d("tag", date2.toString())
//                if (date1 < date2){
//                    messagesList2.add(item)
//                }else{
//                    messagesList2.add(msg)
//                    break
//                }
//
//
//            }
//
//        }
//
//        messagesList.removeAll(messagesList)
//        messagesList.addAll(messagesList2)
//
//
//
//    }



}