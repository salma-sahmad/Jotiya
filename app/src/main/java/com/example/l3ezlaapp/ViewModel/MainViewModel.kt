package com.example.l3ezlaapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.Model.SliderModel
import com.example.l3ezlaapp.activity.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel : ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _popular = MutableLiveData<MutableList<ItemModel>>()

    val banners: LiveData<List<SliderModel>> = _banner
    val popular: LiveData<MutableList<ItemModel>> = _popular

    fun loadBanners() {
        val ref = firebaseDatabase.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Firebase", "Data changed: $snapshot")
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to load banners", error.toException())
            }
        })
    }

    fun loadPopular(): LiveData<List<ItemModel>> {
        val ref = firebaseDatabase.getReference("Items")
        val popularItems = MutableLiveData<List<ItemModel>>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemModel>()
                for (childSnapshot in snapshot.children) {
                    val itemModel = childSnapshot.getValue(ItemModel::class.java)
                    if (itemModel != null) {
                        lists.add(itemModel)
                    }
                }
                popularItems.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to load popular items", error.toException())
            }
        })

        return popularItems
    }

    fun addProductToPopular(product: ItemModel) {
        // Associate the product with the user ID before adding it to the popular list
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        if (userId != null) {
            val currentList = _popular.value ?: mutableListOf()
            val itemModel = ItemModel(
                title = product.title,
                description = product.description,
                picUrl = product.picUrl,
                price = product.price,
                userID = userId // Associate the product with the user ID
            )
            currentList.add(itemModel)
            _popular.value = currentList
        } else {
            Log.e("AddProductToPopular", "Current user is null")
        }
    }
//    fun loadPopularForUser(userId: String): LiveData<MutableList<ItemModel>> {
//        val userPopularItems = MutableLiveData<MutableList<ItemModel>>()
//        val Ref = firebaseDatabase.getReference("Items")
//        Ref.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val lists = mutableListOf<ItemModel>()
//                for (childSnapshot in snapshot.children) {
//                    val list = childSnapshot.getValue(ItemModel::class.java)
//                    if (list != null) {
//                        lists.add(list)
//                    }
//                }
//                userPopularItems.value = lists
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("Firebase", "Error loading user-specific popular items", error.toException())
//            }
//        })
//        return userPopularItems
//    }

}
