package org.techtown.testrecyclerview.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy.REPLACE


@Dao
interface FoodDAO {
    @Insert(onConflict = REPLACE)
    fun insert(real_nutri : FoodEntity)

    @Query("SELECT * FROM real_nutri")
    fun getAll() : List<FoodEntity>

    @Delete
    fun delete(memo : FoodEntity)
}