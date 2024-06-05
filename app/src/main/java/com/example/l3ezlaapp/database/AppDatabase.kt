package com.example.l3ezlaapp.database
//*** ne marche pas prblm de gradle**

//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import android.content.Context
//import com.example.l3ezlaapp.Model.ItemsModel
//import com.example.l3ezlaapp.data.CartDao
//
//@Database(entities = [ItemsModel::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun cartDao(): CartDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "app_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
