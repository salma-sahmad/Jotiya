package com.example.l3ezlaapp.Helper

import android.content.Context
import android.widget.Toast
import com.example.l3ezlaapp.Model.ItemModel
import com.google.firebase.firestore.FirebaseFirestore

class ManagmentCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun addItemsToCart(item: ItemModel, userId: String) {
        val listFood = getListCart()
        val existingItem = listFood.find { it.title == item.title }

        if (existingItem != null) {
            existingItem.numberInCart += 1
        } else {
            item.numberInCart = 1
            listFood.add(item)
        }

        tinyDB.putListObject("CartList", listFood)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()

        // Add the item to the Firestore database
//        val db = FirebaseFirestore.getInstance()
//        val cartRef = db.collection("users").document(userId).collection("Cart")
//        cartRef.document(item.userID).set(item)
    }




    fun getListCart(): ArrayList<ItemModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listFood: ArrayList<ItemModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listFood[position].numberInCart > 1) {
            listFood[position].numberInCart -= 1
        } else {
            listFood.removeAt(position)
        }
        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

    fun plusItem(listFood: ArrayList<ItemModel>, position: Int, listener: ChangeNumberItemsListener) {
        listFood[position].numberInCart += 1
        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listFood = getListCart()
        var fee = 0.0
        for (item in listFood) {
            fee += item.price * item.numberInCart
        }
        return fee
    }
}
