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

    var imageList : MutableList<String> = mutableStateListOf<String>()

    var shopmessagesList : MutableList<ShopChat> = mutableStateListOf<ShopChat>()





    suspend fun  getUser() {

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
        Log.d("tag2",shopLogo.toString())
//        Log.d("tag",shopid)
//        getShopmessages(shopid)


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
        val query = dbref.child("shop_id").equalTo(shopid)
        val snapshot = query.getSnapshotValue()
        if (snapshot.exists()) {
            for (sp in snapshot.children) {
                val shopmessage = sp.child("message").value.toString()
                val messagetime = sp.child("time").value.toString()
                val shopchatid = sp.child("chat_id").value.toString()
                var shopimageList = sp.child("images").value
                val  tags= sp.child("tags").value
                if(shopimageList!=null) {
                    val chat: ShopChat = ShopChat(
                        shopmessage.toString(), shopchatid, messagetime, shopimageList as ArrayList<String>,
                        tags as ArrayList<String>,
                        shopid
                    )
                    shopmessagesList.add(chat)
                }
                else{
                    val chat: ShopChat = ShopChat(
                        shopmessage.toString(), shopchatid, messagetime,shop_id = shopid)
                    shopmessagesList.add(chat)
                }

            }
        }


    }



}