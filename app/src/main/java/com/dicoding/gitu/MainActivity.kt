package com.dicoding.gitu

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.gitu.databinding.ActivityMainBinding
import com.dicoding.gitu.helper.SettingPreference
import com.dicoding.gitu.helper.SettingViewModelFactory
import com.dicoding.gitu.menu.FavoriteActivity
import com.dicoding.gitu.menu.SettingActivity
import com.dicoding.gitu.response.Items
import com.dicoding.gitu.user.User
import com.dicoding.gitu.user.UserAdapter
import com.dicoding.gitu.viewModel.MainViewModel
import com.dicoding.gitu.viewModel.SettingViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
       ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        supportActionBar?.title = "Github User"

        val pref = SettingPreference.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(SettingViewModel::class.java)
        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        activityMainBinding.rvUsers.layoutManager = LinearLayoutManager(this)
        viewModel.listUser.observe(this, { items -> setUserData(items) })
        viewModel.isLoading.observe(this, { showLoading(it) })

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = activityMainBinding.searchField

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                viewModel.updateQuery(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            activityMainBinding.progressBar.visibility = View.VISIBLE
            activityMainBinding.rvUsers.visibility = View.GONE
        } else {
            activityMainBinding.progressBar.visibility = View.GONE
            activityMainBinding.rvUsers.visibility = View.VISIBLE
        }
    }

    private fun setUserData(users: List<Items>) {
        val list = ArrayList<User>()
        for (user in users) {
            val userData = User(user.avatarUrl.toString(), user.login.toString())
            list.add(userData)
        }
        val listUser = UserAdapter(list)
        activityMainBinding.rvUsers.adapter = listUser

        listUser.setOnUserClickCallback(object: UserAdapter.OnUserClickCallback {
            override fun onUserClicked(data: User) {
                val userDetail = User(data.photo, data.username)
                val toDetail = Intent(this@MainActivity, DetailActivity::class.java)
                toDetail.putExtra(DetailActivity.EXTRA_USER, userDetail)
                startActivity(toDetail)
            }
        })
    }
}