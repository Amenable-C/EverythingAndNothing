package org.techtown.testrecyclerview.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.techtown.testrecyclerview.search.FoodData


@Database(entities = arrayOf(FoodEntity::class), version = 1)
abstract class FoodDatabase : RoomDatabase(){
    abstract  fun foodDAO() : FoodDAO

    companion object {
        var INSTANCE : FoodDatabase? = null

        fun getInstance(context : Context) : FoodDatabase?{
            if(INSTANCE == null){
                synchronized(FoodDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    FoodDatabase::class.java, "real_nutri.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}