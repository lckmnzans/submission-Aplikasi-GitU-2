package com.dicoding.gitu.room.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/*
* Interface ini adalah untuk operasi CRUD
* ini nantinya akan diimplementasikan untuk melakukan operasi CRUD
* */
@Dao
interface UserFavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserFav)

    @Update
    fun update(user: UserFav)

    @Delete
    fun delete(user: UserFav)

    @Query("SELECT * FROM UserFav ORDER BY username ASC")
    fun getAll(): LiveData<List<UserFav>>
}