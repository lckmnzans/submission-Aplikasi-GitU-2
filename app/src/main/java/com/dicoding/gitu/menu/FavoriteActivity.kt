package com.dicoding.gitu.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.gitu.DetailActivity
import com.dicoding.gitu.databinding.ActivityFavoriteBinding
import com.dicoding.gitu.helper.RoomViewModelFactory
import com.dicoding.gitu.room.database.UserFav
import com.dicoding.gitu.user.User
import com.dicoding.gitu.user.UserAdapter
import com.dicoding.gitu.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val roomViewModel by viewModels<FavoriteViewModel> {
        RoomViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(_activityFavoriteBinding?.root)

        supportActionBar!!.title = "Favorite Users"

        _activityFavoriteBinding?.rvUserFavorite?.layoutManager = LinearLayoutManager(this)
        roomViewModel.getAll().observe(this) { userFav ->
            setUserData(userFav)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    private fun setUserData(users: List<UserFav>) {
        val list = ArrayList<User>()
        for (user in users) {
            val userData = User(user.avatarUrl.toString(), user.username)
            list.add(userData)
        }
        val listUser = UserAdapter(list)
        _activityFavoriteBinding?.rvUserFavorite?.adapter = listUser

        listUser.setOnUserClickCallback(object: UserAdapter.OnUserClickCallback {
            override fun onUserClicked(data: User) {
                val userDetail = User(data.photo, data.username)
                val toDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                toDetail.putExtra(DetailActivity.EXTRA_USER, userDetail)
                startActivity(toDetail)
            }
        })
    }
}