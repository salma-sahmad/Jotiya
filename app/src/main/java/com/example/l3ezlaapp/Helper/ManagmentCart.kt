package com.example.l3ezlaapp.Helper

import android.content.Context
import android.widget.Toast
import com.example.l3ezlaapp.Model.ItemModel


class ManagmentCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertFood(item: ItemModel) {
        var listFood = getListCart()
//        val existAlready = listFood.any { it.title == item.title }
//        val index = listFood.indexOfFirst { it.title == item.title }


        tinyDB.putListObject("CartList", listFood)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<ItemModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listFood: ArrayList<ItemModel>, position: Int, listener: ChangeNumberItemsListener) {

        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

    fun plusItem(listFood: ArrayList<ItemModel>, position: Int, listener: ChangeNumberItemsListener) {
        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listFood = getListCart()
        var fee = 0.0
        for (item in listFood) {
            fee += item.price
        }
        return fee
    }
}