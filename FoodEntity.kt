package org.techtown.testrecyclerview.database


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "real_nutri")
data class FoodEntity(
    @PrimaryKey (autoGenerate = true)
    var idx: Long?,
    var name: String,
    var kcal: Long?,
    var cab: Long?,
    var Pro: Long?,
    var priority: Long?)