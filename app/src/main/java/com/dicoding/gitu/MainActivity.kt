package com.dicoding.gitu

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.gitu.databinding.ActivityMainBinding
import com.dicoding.gitu.user.User
import com.dicoding.gitu.user.UserAdapter
import com.dicoding.gitu.viewModel.MainViewModel

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

        activityMainBinding.rvUsers.layoutManager = LinearLayoutManager(this)
        viewModel.listUser.observe(this, { items -> setUserData(items) })
        viewModel.isLoading.observe(this, { showLoading(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                viewModel.updateQuery(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
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
                toDetail.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
                startActivity(toDetail)
            }
        })
    }
}