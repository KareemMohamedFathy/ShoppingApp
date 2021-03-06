package com.shopping.shoppingapp.DisplayShops

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shopping.shoppingapp.DB.Shop
import com.shopping.shoppingapp.SellerHomePage.MyShop.FValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DisplayShopViewModel : ViewModel() {
    var userStatus : MutableState<String> = mutableStateOf("")
    var entrees : MutableList<String> = mutableStateListOf<String>()
    var shopi : MutableList<String> = mutableStateListOf<String>()
    var shopss : MutableList<Shop> = mutableStateListOf<Shop>()
  suspend fun getFromDb(){
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
     val dbref1=FirebaseDatabase.getInstance().getReference()
       val query1 = dbref1.child("User").orderByChild("user_id").equalTo(FirebaseAuth.getInstance().currentUser!!.uid)
       val snapshot1 = query1.getSnapshotValue()

      if (snapshot1.exists()) {
           for (sp in snapshot1.children) {
               userStatus.value = sp.child("status").value.toString()
           }
       }

    }
    suspend fun DatabaseReference.getSnapshotValue(): DataSnapshot {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<DataSnapshot> { continuation ->
                addListenerForSingleValueEvent(
                    FValueEventListener(
                        onDataChange = {
                            continuation.resume(it)
                            val snapshot = it
                        }
                        ,onError = { continuation.resumeWithException(it.toException())},

                        )
                )}

        }



    }


    suspend fun Query.getSnapshotValue(): DataSnapshot {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<DataSnapshot> { continuation ->
                addListenerForSingleValueEvent(
                    FValueEventListener(
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

}
