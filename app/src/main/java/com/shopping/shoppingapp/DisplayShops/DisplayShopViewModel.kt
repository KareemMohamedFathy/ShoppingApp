package com.shopping.shoppingapp.DisplayShops

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shopping.shoppingapp.DB.Shop

class DisplayShopViewModel : ViewModel() {

    var entrees : MutableList<String> = mutableStateListOf<String>()
    var shopi : MutableList<String> = mutableStateListOf<String>()
    var shopss : MutableList<Shop> = mutableStateListOf<Shop>()
   fun getFromDb(){
       entrees.clear()
       shopi.clear()
       shopss.clear()
        val dbReference = FirebaseDatabase.getInstance().getReference()
        val dbref = dbReference.child("Shop")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (sp in snapshot.children) {
                        val arr: ArrayList<Any?> = arrayListOf(sp.value)
                        val shopname = sp.child("name").value.toString()
                        val shopicon = sp.child("logo").value.toString()
                        val shopidd = sp.child("shop_id").value.toString()
                        val useridd = sp.child("user_id").value.toString()
                        val shop = Shop()
                        shop.name = shopname
                        shop.logo = shopicon
                        shop.shop_id = shopidd
                        shop.user_id = useridd
                        entrees.add(shopname)
                        shopi.add(shopicon)
                        shopss.add(shop)
                    }
                }
//                        Log.d("tag", entrees.toString())
//                    Log.d("tag", shopi.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

}
