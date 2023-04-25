package com.dicoding.gitu.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.gitu.viewModel.DetailViewModel
import com.dicoding.gitu.viewModel.FavoriteViewModel

class RViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: RViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): RViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RViewModelFactory::class.java) {
                    INSTANCE = RViewModelFactory(application)
                }
            }
            return INSTANCE as RViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}