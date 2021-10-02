package com.shopping.shoppingapp.SellerHomePage.MyShop

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shopping.shoppingapp.DB.Product
import com.shopping.shoppingapp.DB.ShopChat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MyShopViewModel:ViewModel() {
        var shopName : MutableState<String> = mutableStateOf("")
    var shopId : MutableState<String> = mutableStateOf("")

    var shopLogo : MutableState<String> = mutableStateOf("")
      var productsList : MutableList<Product> = mutableStateListOf<Product>()
    var messagesList : MutableList<ShopChat> = mutableStateListOf<ShopChat>()
    var imageList : MutableList<String> = mutableStateListOf<String>()


    suspend fun  getShop() {


        val dbReference = FirebaseDatabase.getInstance().getReference()
        val auth = FirebaseAuth.getInstance()
        val query = dbReference.child("Shop").orderByChild("user_id").equalTo(auth.currentUser!!.uid)
        val db: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("user_id")
        val snapshot = query.getSnapshotValue()
        if (snapshot.exists()) {
            for (sp in snapshot.children) {
                val arr: ArrayList<Any?> = arrayListOf(sp.value)
                shopName.value = sp.child("name").value.toString()
                shopLogo.value = sp.child("logo").value.toString()
                shopId.value=sp.child("shop_id").value.toString()
            }
        }

        getallProducts()


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
                    }
                    ,onError = { continuation.resumeWithException(it.toException())},

                    ))}

        }



}





suspend  fun getallProducts() {
        val dbReference = FirebaseDatabase.getInstance().getReference()
        val dbref = dbReference.child("ShopChat")
     val snapshot=dbref.getSnapshotValue()
    if (snapshot.exists()) {
        for (sp in snapshot.children) {
            val message = sp.child("message").value.toString()
            val  chat_id= sp.child("chat_id").value.toString()
            val  shop_id= sp.child("shop_id").value.toString()
            val  tags= sp.child("tags").value
            val  time= sp.child("time").value.toString()
            var   images= sp.child("images").value
            if(images!=null) {
                val chat: ShopChat = ShopChat(
                    message.toString(), chat_id, time, images as ArrayList<String>,
                    tags as ArrayList<String>,
                    shop_id
                )
                messagesList.add(chat)
            }
            else{
                val chat: ShopChat = ShopChat(
                    message.toString(), chat_id, time,shop_id = shop_id)
                messagesList.add(chat)
            }
            //   val product=Product(productName,productPrice,productDescription,images,shop_id,product_id,tags,time)
            //     productsList.add(product)
        }

    }

}
   suspend  fun addChat(message:String){
         return withContext(Dispatchers.IO) {

             //messagesList.clear()
             val firebaseDatabase = FirebaseDatabase.getInstance()
             var dbReference = firebaseDatabase.getReference("ShopChat")
             val shopChat_id = dbReference.push().key.toString()
             val chat: ShopChat = ShopChat(message.toString(), shopChat_id, getCurrentDate(),shop_id = shopId.value)
             dbReference.child(shopChat_id).setValue(chat)
               messagesList.add(chat)

         }

    }


}
