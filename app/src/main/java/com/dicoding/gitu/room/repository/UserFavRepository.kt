package com.dicoding.gitu.room.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.gitu.room.database.UserFav
import com.dicoding.gitu.room.database.UserFavDao
import com.dicoding.gitu.room.database.UserFavRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/*
* Kelas ini
* */
class UserFavRepository(application: Application) {
    private val mUserFavDao: UserFavDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserFavRoomDatabase.getDatabase(application)
        mUserFavDao = db.userFavDao()
    }

    fun getAll(): LiveData<List<UserFav>> = mUserFavDao.getAll()

    fun insert(user: UserFav) {
        executorService.execute { mUserFavDao.insert(user) }
    }

    fun delete(user: UserFav) {
        executorService.execute { mUserFavDao.delete(user) }
    }

    fun update(user: UserFav) {
        executorService.execute { mUserFavDao.update(user) }
    }
}