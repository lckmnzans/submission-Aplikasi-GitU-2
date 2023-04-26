package com.dicoding.gitu.menu

import android.content.Context
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.gitu.R
import com.dicoding.gitu.databinding.ActivitySettingBinding
import com.dicoding.gitu.helper.SettingPreference
import com.dicoding.gitu.helper.SettingViewModelFactory
import com.dicoding.gitu.viewModel.SettingViewModel

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
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.switchThumbChecked))
                switchTheme.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.switchTrackChecked))
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.switchThumbUnchecked))
                switchTheme.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.switchTrackUnchecked))
                switchTheme.isChecked = false
            }
        }
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }
}
