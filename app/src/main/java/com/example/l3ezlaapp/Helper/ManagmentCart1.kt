package com.example.l3ezlaapp.Helper


//*** ne marche pas prblm de gradle**

//import android.content.Context
//import android.widget.Toast
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.asLiveData
//import com.example.l3ezlaapp.database.AppDatabase
//import com.example.l3ezlaapp.Model.ItemsModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class ManagmentCart1(context: Context) {
//    private val db = AppDatabase.getDatabase(context)
//    private val cartDao = db.cartDao()
//
//    fun addItemsToCart(item: ItemsModel, userId: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val existingItem = cartDao.getItemsByUserId(userId).find { it.title == item.title }
//
//            if (existingItem != null) {
//                existingItem.numberInCart += 1
//                cartDao.updateItem(existingItem)
//            } else {
//                item.numberInCart = 1
//                cartDao.insertItem(item)
//            }
//            CoroutineScope(Dispatchers.Main).launch {
//                Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    fun getListCart(userId: String): LiveData<List<ItemsModel>> {
//        return cartDao.getItemsByUserId(userId).asLiveData()
//    }
//
//    fun minusItem(item: ItemsModel, listener: ChangeNumberItemsListener) {
//        CoroutineScope(Dispatchers.IO).launch {
//            if (item.numberInCart > 1) {
//                item.numberInCart -= 1
//                cartDao.updateItem(item)
//            } else {
//                cartDao.deleteItem(item)
//            }
//            CoroutineScope(Dispatchers.Main).launch {
//                listener.onChanged()
//            }
//        }
//    }
//
//    fun plusItem(item: ItemsModel, listener: ChangeNumberItemsListener) {
//        CoroutineScope(Dispatchers.IO).launch {
//            item.numberInCart += 1
//            cartDao.updateItem(item)
//            CoroutineScope(Dispatchers.Main).launch {
//                listener.onChanged()
//            }
//        }
//    }
//
//    fun getTotalFee(userId: String): LiveData<Double> {
//        return cartDao.getItemsByUserId(userId).map { list ->
//            list.sumOf { it.price * it.numberInCart }
//        }.asLiveData()
//    }
//}
