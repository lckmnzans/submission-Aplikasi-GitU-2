package com.dicoding.gitu.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.gitu.helper.SettingPreference
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreference): ViewModel() {
    /*
    * Kelas ini bertanggungjawab sebagai ViewModel untuk SettingActivity
    * ada dua metode dalam viewModel ini, yaitu:
    * - getThemeSetting() utk mengambil pengaturan tema yg sedang berlaku
    * - setThemeSetting() utk mengatur/apply pengaturan tema
    * */
    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        //metode ini akan dijalankan secara async utk mengeksekusi metode suspend lainnya
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}