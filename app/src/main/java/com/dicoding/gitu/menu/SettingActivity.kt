package com.dicoding.gitu.menu

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.gitu.databinding.ActivitySettingBinding
import com.dicoding.gitu.helper.SettingPreference
import com.dicoding.gitu.helper.SViewModelFactory
import com.dicoding.gitu.viewModel.SettingViewModel

/*
* Dibuat instance DataStore yg terkait dg Preferences dari sebuah Context
* dilakukan pembuatan instance dg delegate function dimana preferencesDataStore() akan mengembalikan
* nilai DataStore dan meneruskannya dg 'by' ke Context.dataStore
* dengan ini Context dapat memanggil SettingPreferences secara langsung tanpa harus membuat instance kelas
*/
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Setting"
        val switchTheme = binding.switchTheme

        val pref = SettingPreference.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SViewModelFactory(pref)).get(SettingViewModel::class.java)
        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }
}
