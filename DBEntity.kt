package org.techtown.testrecyclerview.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "real_nutri")
data class FoodEntity(@PrimaryKey val idx: Int, val name: String, val kcal: Int, val cab: Int, val Pro: Int, val priority: Int)
