package org.techtown.testrecyclerview.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FoodEntity::class], version = 2, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun rawDAO(): RawDAO

    companion object {
        @JvmField
        val MIGRATION_1_2 : Migration = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

}