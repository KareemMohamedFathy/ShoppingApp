package com.shopping.shoppingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.shopping.shoppingapp.DB.Tag


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    /*   addTags("Clothes")
        addTags("Accessories")
        addTags("OnSale")
        addTags("fashion")
        addTags("instagood")
        addTags("plants")
        addTags("for home")
        addTags("for office")
        addTags("limited time only")
        addTags("Comfortable")
        addTags("Unique")
        addTags("Save")
        addTags("Games")
*/









    }

    private fun addTags(name:String) {
        val firebaseDatabase= FirebaseDatabase.getInstance()
        var  dbReference=firebaseDatabase.getReference("Tag")
        val tagId = dbReference.push().key.toString()
        val tag= Tag(tagId,name)
        dbReference.child(tagId).setValue(tag)
    }
}
