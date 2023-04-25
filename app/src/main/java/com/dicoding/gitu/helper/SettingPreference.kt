package com.dicoding.gitu.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreference private constructor(private val dataStore: DataStore<Preferences>) {
    /*
    * Kelas ini bertanggungjawab utk menyimpan dan mengambil data pengaturan tema
    * ada dua metode dalam viewModel ini, yaitu:
    * - getThemeSetting() utk mengambil pengaturan tema yg sedang berlaku
    * - setThemeSetting() utk mengatur/apply pengaturan tema
    * */
    private val THEME_KEY = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> {
        /*
        * metode ini mengembalikan Flow<Boolean> dengan mengambil data dari key THEME_KEY di dalam dataStore.data
        * objek dataStore.data dilakukan mapping
        * */
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        /*
        * metode ini menerima parameter dan mengedit dataStore ke dalam key THEME_KEY
        * fungsi ini dijalankan dengan suspend, sehingga nantinya perlu dieksekusi secara async dengan method launch()
        * */
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreference {
            /*
            * getInstance() adalah sebuah fungsi yang menggunakan pattern Singleton
            * untuk mendapatkan instance dari SettingPreference. Fungsi ini menggunakan synchronized agar hanya satu thread
            * yang bisa mengakses fungsi ini pada satu waktu dan mencegah terjadinya race condition pada objek INSTANCE.
            * */
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}