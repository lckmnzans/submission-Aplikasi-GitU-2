package com.dicoding.gitu.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.gitu.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(_activityFavoriteBinding?.root)

    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }
}