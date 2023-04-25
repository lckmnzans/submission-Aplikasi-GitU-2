package com.dicoding.gitu.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.gitu.viewModel.SettingViewModel

class SettingViewModelFactory(private val pref: SettingPreference): ViewModelProvider.NewInstanceFactory() {
    /*
    * Kelas ini bertanggung jawab utk membuat instance dari kelas ViewModel
    * metode create() akan otomatis terpanggil ketika memulai inisialisasi utk membuat instance ViewModel
    * */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //jika kelas ViewModel yg menginisialisasi instance adalah SettingViewModel, maka diizinkan utk membuat instance
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}