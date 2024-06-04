//package com.example.l3ezlaapp.data
//
//import androidx.room.*
//import com.example.l3ezlaapp.model.ItemsModel
//
//@Dao
//interface CartDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertItem(item: ItemsModel)
//
//    @Query("SELECT * FROM cart_items WHERE userId = :userId")
//    suspend fun getItemsByUserId(userId: String): List<ItemsModel>
//
//    @Update
//    suspend fun updateItem(item: ItemsModel)
//
//    @Delete
//    suspend fun deleteItem(item: ItemsModel)
//
//    @Query("DELETE FROM cart_items")
//    suspend fun clearCart()
//}
