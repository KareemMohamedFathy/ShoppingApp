package com.shopping.shoppingapp.SellerHomePage.MyShop

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shopping.shoppingapp.DB.Product
import com.shopping.shoppingapp.DB.ShopChat
import java.lang.StringBuilder

class AddProductPhotosViewModel:ViewModel() {
    var shopName : MutableState<String> = mutableStateOf("")
    var shopLogo : MutableState<String> = mutableStateOf("")
    var shopId : MutableState<String> = mutableStateOf("")

    var productsList : MutableList<Product> = mutableStateListOf<Product>()
    var messagesList : MutableList<ShopChat> = mutableStateListOf<ShopChat>()
    var imageList : MutableList<String> = mutableStateListOf<String>()

    fun  getShop(){

        productsList.clear()
        messagesList.clear()
        imageList.clear()

        val dbReference = FirebaseDatabase.getInstance().getReference()
        val auth=FirebaseAuth.getInstance()
        val query = dbReference.child("Shop").orderByChild("user_id").equalTo(auth.currentUser!!.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (sp in snapshot.children) {
                        val arr: ArrayList<Any?> = arrayListOf(sp.value)
                        shopName.value = sp.child("name").value.toString()
                        shopLogo.value = sp.child("logo").value.toString()
                        shopId.value= sp.child("shop_id").value.toString()
                    }
                }
                getallProducts()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun getallProducts() {

        productsList.clear()
        messagesList.clear()
        imageList.clear()

        val dbReference = FirebaseDatabase.getInstance().getReference()
        val dbref = dbReference.child("ShopChat")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (sp in snapshot.children) {
                        val message = sp.child("message").value.toString()
                        val  chat_id= sp.child("chat_id").value.toString()
                        val  user_id= sp.child("user_id").value.toString()
                        val  tags= sp.child("tags").value

                        val  time= sp.child("time").value.toString()
                        var   images= sp.child("images").value


                        if(images!=null) {
                            val chat: ShopChat = ShopChat(
                                message.toString(), chat_id, time, images as ArrayList<String>,
                                tags as ArrayList<String>,
                                shopId.value
                            )
                            messagesList.add(chat)
                        }
                        else{
                            val chat: ShopChat = ShopChat(
                                message.toString(), chat_id, time,shop_id =shopId.value)
                            messagesList.add(chat)
                        }
                        //   val product=Product(productName,productPrice,productDescription,images,shop_id,product_id,tags,time)
                        //     productsList.add(product)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}