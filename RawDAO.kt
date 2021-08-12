package org.techtown.testrecyclerview.database

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
// 수정 필요
import com.example.skku_food.data.ResSimpleData
import com.example.skku_food.data.ResFullData

interface RawDAO {
    // 수정 필요
    @RawQuery
    fun getJustNamePhone(query: SupportSQLiteQuery): List<ResSimpleData>

    @RawQuery
    fun getFullResInfo(query: SupportSQLiteQuery): ResFullData
}