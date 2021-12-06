package com.shopping.shoppingapp.AdminHomePage.ShopsPage

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.shopping.shoppingapp.DB.Shop
import com.shopping.shoppingapp.SellerHomePage.MyShop.FValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ShopsViewModel(): ViewModel() {
    var productsList : MutableList<Shop> = mutableStateListOf<Shop>()

    suspend fun getShops(){
        val dbReference=FirebaseDatabase.getInstance().getReference()

        val query = dbReference.child("Shop")
        val snapshot = query.getSnapshotValue()
        if (snapshot.exists()) {
            for (sp in snapshot.children) {

                val shopName = sp.child("name").value.toString()
                val shopLogo = sp.child("logo").value.toString()
                val shopId=sp.child("shop_id").value.toString()
                val user_id=sp.child("user_id").value.toString()
                productsList.add(Shop(shopName,shopLogo,shopId,user_id))
            }
        }

    }
    suspend fun acceptShop(shopId: String) {
        return withContext(Dispatchers.IO) {
            val dbref=FirebaseDatabase.getInstance().getReference("Shop").child(shopId).child("shopStatus").setValue("Accepted")

        }
    }
    suspend fun rejectShop( shopId: String){
        return withContext(Dispatchers.IO) {
            val dbref=FirebaseDatabase.getInstance().getReference("Shop").child(shopId).child("shopStatus").setValue("Rejected")
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
                        }
                        ,onError = { continuation.resumeWithException(it.toException())},

                        )
                )}
        }
    }

}