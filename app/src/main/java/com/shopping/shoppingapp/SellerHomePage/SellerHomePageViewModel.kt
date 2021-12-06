package com.shopping.shoppingapp.SellerHomePage

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.shopping.shoppingapp.SellerHomePage.MyShop.FValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SellerHomePageViewModel: ViewModel() {
    var shopStatus : MutableState<String> = mutableStateOf("")
    var userStatus : MutableState<String> = mutableStateOf("")

    suspend fun getShopStatus(){
        val dbref=FirebaseDatabase.getInstance().getReference()
        val auth = FirebaseAuth.getInstance()
        val query = dbref.child("Shop").orderByChild("user_id").equalTo(auth.currentUser!!.uid)
        val snapshot = query.getSnapshotValue()
        if (snapshot.exists()) {
            for (sp in snapshot.children) {
                shopStatus.value = sp.child("shopStatus").value.toString()
            }
        }
        val query1 = dbref.child("User").orderByChild("user_id").equalTo(auth.currentUser!!.uid)
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