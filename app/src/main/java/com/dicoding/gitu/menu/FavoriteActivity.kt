package com.dicoding.gitu.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dicoding.gitu.databinding.ActivityFavoriteBinding
import com.dicoding.gitu.helper.RViewModelFactory
import com.dicoding.gitu.room.database.UserFav
import com.dicoding.gitu.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding

    private var isRemoved = false
    private var user: UserFav? = null

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(_activityFavoriteBinding?.root)

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)

       user = intent.getParcelableExtra(EXTRA_USER)
        if (user != null) {
            isRemoved = true
        } else {
            user = UserFav()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = RViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
}