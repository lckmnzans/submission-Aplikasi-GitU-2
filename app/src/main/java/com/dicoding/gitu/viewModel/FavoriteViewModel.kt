package com.dicoding.gitu.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.gitu.room.database.UserFav
import com.dicoding.gitu.room.repository.UserFavRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mUserFavRepository: UserFavRepository = UserFavRepository(application)

    fun getAll(): LiveData<List<UserFav>> = mUserFavRepository.getAll()

    fun insert(user: UserFav) {
        mUserFavRepository.insert(user)
    }

    fun update(user: UserFav) {
        mUserFavRepository.update(user)
    }

    fun delete(user: UserFav) {
        mUserFavRepository.delete(user)
    }
}