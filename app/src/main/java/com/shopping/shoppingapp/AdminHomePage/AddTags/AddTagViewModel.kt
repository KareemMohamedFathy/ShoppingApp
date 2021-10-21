package com.shopping.shoppingapp.AdminHomePage.AddTags

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shopping.shoppingapp.DB.Product

class AddTagViewModel:ViewModel() {
    var tagsList : MutableList<String> = mutableStateListOf<String>()

    init {
        val dbref = FirebaseDatabase.getInstance().getReference("Tag").addValueEventListener( object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (sp in snapshot.children) {
                    val tagName = sp.child("tagName").value.toString()
                    tagsList.add(tagName)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}