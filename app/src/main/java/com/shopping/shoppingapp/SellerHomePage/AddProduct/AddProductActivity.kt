package com.shopping.shoppingapp.SellerHomePage.AddProduct

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.shopping.shoppingapp.R
import com.shopping.shoppingapp.Screen
import com.shopping.shoppingapp.DB.Product
import com.shopping.shoppingapp.DB.ShopChat
import com.shopping.shoppingapp.SellerHomePage.MyShop.MyShop
import com.shopping.shoppingapp.SellerHomePage.SellerHomePage
import com.shopping.shoppingapp.ui.theme.MyApplicationTheme
import kotlinx.coroutines.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class AddProductActivity : AppCompatActivity() {

    companion object {
        private const val MY_PERMISSION_CODE = 124
    }
    private var currentImage=0

    private var imagesList: ArrayList<String> = ArrayList<String>()
    private var imagesCount=1
    private var tagsList :ArrayList<String> = ArrayList<String>()
    private var set: HashSet<String> = HashSet<String>()
    private lateinit var productName:String
    private lateinit var productDesciption:String
    private lateinit var productPrice:String


    @DelicateCoroutinesApi
    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       tagsList.add("Clothes")
        tagsList.add("Accessories")
        tagsList.add("OnSale")
        tagsList.add("fashion")
        tagsList.add("instagood")
        tagsList.add("plants")
        tagsList.add("for home")
        tagsList.add("for office")
        tagsList.add("limited time only")
        tagsList.add("Comfortable")
        tagsList.add("Unique")
        tagsList.add("Save")
        tagsList.add("Games")
        setContent {
            MyApplicationTheme() {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.AddProduct.route) {
                    composable(
                        route = Screen.AddProduct.route
                    ) {
                        AddProduct(navController)
                    }
                    composable(
                        route = Screen.AddTags.route
                    ) {
                        AddTags(navController)
                    }
                    composable(
                        route = Screen.SellerHomePage.route
                    ) {
                        SellerHomePage(navController)
                    }


                }
            }
        }
    }
@Composable
    fun AddTags(navController: NavHostController) {
    val state = rememberScrollState()

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(state)

            ,
            ) {
            Text(text = "Add Tags to your product (max 5)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
                )
            for(tagslist in tagsList)
                   MultipleCheckBox(tagslist)

        Button(onClick = {addProductToDb(navController)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Add Product",
                fontSize = 20.sp,
            )
        }
            }


    }

    private fun addProductToDb(navController: NavHostController) {
        val auth=FirebaseAuth.getInstance()
        val lol =   FirebaseDatabase.getInstance().getReference("User").child(auth.currentUser!!.uid).get().addOnSuccessListener {
           val shop_id = it.child("shop_id").value.toString()
            var firebaseDatabase = FirebaseDatabase.getInstance()
            var dbReference = firebaseDatabase.getReference("Product")
            val productId = dbReference.push().key.toString()
            //  val tag= Tag(tagId,name)
            var listOfTags = ArrayList<String>()
            for (item in set) {
                listOfTags.add(item)
            }
            val date=getCurrentDate()
            val product = Product(
                productName,
                productPrice,
                productDesciption,
                imagesList,
                shop_id,
                productId,
                listOfTags,
           date
            )
            dbReference.child(productId).setValue(product)
            val message: StringBuilder = StringBuilder()
            message.append("Name: ${productName}  \n")
            message.append("Price: ${productPrice}  \n")
            message.append("Description: ${productDesciption} ")
             firebaseDatabase= FirebaseDatabase.getInstance()
              dbReference=firebaseDatabase.getReference("ShopChat")
            val shopChat_id = dbReference.push().key.toString()
            val chat: ShopChat = ShopChat(message.toString(),shopChat_id,date,imagesList,listOfTags,
                shop_id)
            dbReference.child(shopChat_id).setValue(chat)
            finish()
            navController.navigate(Screen.SellerHomePage.route)

        }
    }

    @Composable
    fun MultipleCheckBox(text:String){
     Row(
         modifier = Modifier.fillMaxWidth()
             ,
         verticalAlignment = Alignment.Top,
         horizontalArrangement = Arrangement.Start
     ) {
         val checkedState = remember { mutableStateOf(false) }
         if(set.size>=5&&checkedState.value){
             checkedState.value=false
         Toast.makeText(LocalContext.current,"Max is 5 tags",Toast.LENGTH_SHORT).show()
         }
         else if(checkedState.value){
             set.add(text)
         }
         else{
             set.remove(text)
         }
         Checkbox(
             checked = checkedState.value,
             modifier = Modifier.padding(16.dp),
             onCheckedChange = { checkedState.value = it },
         )
         Text(text = text, modifier = Modifier.padding(16.dp)
         ,fontSize = 20.sp,
             fontWeight = FontWeight.Bold
         )
     }

    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionCAMERA =
            ContextCompat.checkSelfPermission(this@AddProductActivity, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(
            this@AddProductActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val listPermissionsNeeded = java.util.ArrayList<String>()
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this@AddProductActivity,
                listPermissionsNeeded.toTypedArray<String>(),
                MY_PERMISSION_CODE
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permReqLuncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    when (requestCode) {
                        MY_PERMISSION_CODE -> {
                            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                onGalleryClick()
                            }
                        }
                    }
                }
            }
    }


    fun onGalleryClick() {
        imagesList.clear()
        imagesCount=1
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"

        resultLauncher.launch(intent)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if(result.data!!.clipData!=null) {
                    var count = result.data!!.clipData!!.itemCount
                    imagesCount = count
                    for (i in 0 until count) {
                        var imageUri: Uri = result.data!!.clipData!!.getItemAt(i).uri
                        //                          images!!.add(imageUri)
                        uploadImageToFirebase(imageUri)
                        //    imageViewAddItem.setImageURI(imageUri)
                    }
                }
                else{
                    var imageUri: Uri = result.data!!.data!!
                    uploadImageToFirebase(imageUri)
                }

            }
        }


    private fun uploadImageToFirebase(fileUri: Uri) {

        if (fileUri != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"

            val database = FirebaseDatabase.getInstance()
            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it
                            imagesList.add(imageUrl.toString())
                        }
                    })

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }
    }
    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        val currentDate = sdf.format(Date())
         return currentDate
    }

    @DelicateCoroutinesApi
    @ExperimentalCoilApi
    @Composable
    fun AddProduct(navController: NavController) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            var name by remember { mutableStateOf(TextFieldValue("")) }
            var price by remember { mutableStateOf(TextFieldValue("")) }
            var description by remember { mutableStateOf(TextFieldValue("")) }
            val pickedImages:MutableList<String?> by remember { mutableStateOf(mutableListOf()) }
            var compose:Int by remember { mutableStateOf(0) }
            var pickedImage: MutableState<String?> =remember { mutableStateOf(pickedImages.getOrNull(0))}
            Spacer(modifier = Modifier.height(16.dp))
            productName=name.text
            productDesciption=description.text
            productPrice=price.text

            Row(modifier= Modifier
                .fillMaxWidth()
                .height(150.dp)
            ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly


            ) {

                Image(painterResource(id = R.drawable.ic_previous) , contentDescription = ""
                ,modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .clickable(onClick = {
                            currentImage--
                            if (currentImage < 0) {
                                currentImage = 0
                            }
                            pickedImage.value = imagesList.getOrNull(currentImage)
                        }
                        )

                          )
                Image(
                   painter = pickedImage.value?.let {
                       rememberImagePainter(it)
                   } ?: painterResource(id = R.drawable.ic_plus),
                   "...",alignment = Alignment.Center,
                   modifier = Modifier
                       .clickable(onClick = {
                           if (checkAndRequestPermissions()) {
                               val value = GlobalScope.async { // creates worker thread
                                   val res = withContext(Dispatchers.Default) {
                                       onGalleryClick()
                                       while (imagesList.size < imagesCount) {
                                           delay(1000)
                                       }
                                       pickedImage.value = imagesList[0]
                                       pickedImages.addAll(imagesList)

                                   }
                               }
                           }
                       })
                       .height(150.dp)
                       .width(150.dp),
                   contentScale = ContentScale.FillBounds
               )
                Image(painterResource(id = R.drawable.ic_next_button) , contentDescription = ""
                    ,modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .clickable(onClick = {
                            currentImage++
                            if (currentImage >= imagesList.size) {
                                currentImage = imagesList.size - 1
                            }
                            pickedImage.value = imagesList.getOrNull(currentImage)
                        }
                        )


                )
           }









            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Add product photo(s)")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Enter Name")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(), label = {
                    Text("Enter Description")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Enter price")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {navController.navigate(Screen.AddTags.route)}
            ,modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Next",
                    fontSize = 20.sp
                )

        }


        }
    }
}