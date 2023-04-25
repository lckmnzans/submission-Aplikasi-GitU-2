package com.dicoding.gitu.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserFav::class], version = 1)
abstract class UserFavRoomDatabase: RoomDatabase() {
    abstract fun userFavDao(): UserFavDao

    companion object {
        @Volatile
        private var INSTANCE: UserFavRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserFavRoomDatabase {
            if (INSTANCE == null) {
                synchronized(UserFavRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserFavRoomDatabase::class.java, "room_database")
                        .build()
                }
            }
            return INSTANCE as UserFavRoomDatabase
        }
    }
}