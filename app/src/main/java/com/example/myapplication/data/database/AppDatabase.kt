package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.dao.GroupDao
import com.example.myapplication.data.dao.LapDao
import com.example.myapplication.data.dao.PersonDao
import com.example.myapplication.data.dao.RecordDao
import com.example.myapplication.data.entity.GroupEntity
import com.example.myapplication.data.entity.GroupPersonEntity
import com.example.myapplication.data.entity.LapEntity
import com.example.myapplication.data.entity.LapGroupEntity
import com.example.myapplication.data.entity.PersonEntity
import com.example.myapplication.data.entity.RecordEntity

@Database(
    entities = [
        PersonEntity::class,
        GroupEntity::class,
        GroupPersonEntity::class,
        LapEntity::class,
        LapGroupEntity::class,
        RecordEntity::class
    ],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun groupDao(): GroupDao
    abstract fun lapDao(): LapDao
    abstract fun recordDao(): RecordDao
}