package com.dicoding.gitu

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.gitu.databinding.ActivityDetailBinding
import com.dicoding.gitu.follows.SectionsPageAdapter
import com.dicoding.gitu.helper.RoomViewModelFactory
import com.dicoding.gitu.response.UserDetailResponse
import com.dicoding.gitu.room.database.UserFav
import com.dicoding.gitu.user.User
import com.dicoding.gitu.viewModel.DetailViewModel
import com.dicoding.gitu.viewModel.FavoriteViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var activityDetailBinding: ActivityDetailBinding
    private lateinit var sectionsPageAdapter: SectionsPageAdapter
    private val detailViewModel: DetailViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
    }
    private val roomViewModel by viewModels<FavoriteViewModel>() {
        RoomViewModelFactory.getInstance(application)
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        supportActionBar!!.hide()

        val user = getUserDetail()
        if (user != null) {
            sectionsPageAdapter = SectionsPageAdapter(this, user.username)
            DetailViewModel.username = user.username
            detailViewModel.userDetail.observe(this, { userDetail -> setUserDetail(userDetail)})
            detailViewModel.isLoading.observe(this, { showLoading(it) })
        }

        val viewPager: ViewPager2 = activityDetailBinding.viewPager
        viewPager.adapter = sectionsPageAdapter
        val tabs: TabLayout = activityDetailBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        activityDetailBinding.fabAddToFav.setOnClickListener {
            val userFav = UserFav()
            userFav.let { userFavorite ->
                userFavorite.avatarUrl = user!!.photo
                userFavorite.username = user.username
                userFavorite.isFavorite = true
                roomViewModel.insert(userFav)

                activityDetailBinding.fabAddToFav.setImageResource(R.drawable.ic_favorite)
                Toast.makeText(this, "Berhasil ditambahkan ke daftar favorit", Toast.LENGTH_SHORT).show()
            }
        }
        supportActionBar?.elevation = 0f
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            activityDetailBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityDetailBinding.progressBar.visibility = View.GONE
        }
    }

    private fun setUserDetail(user: UserDetailResponse) {
        Glide.with(this@DetailActivity).load(user.avatarUrl).into(activityDetailBinding.imgUserProfile)
        if (user.name != null) {
            activityDetailBinding.tvUserName.text = user.name.toString()
        } else {
            activityDetailBinding.tvUserName.text = user.login.toString()
        }
        activityDetailBinding.tvUserUsername.text = user.login.toString()
        activityDetailBinding.tvFollowersCount.text = user.followers.toString()
        activityDetailBinding.tvFollowingCount.text = user.following.toString()
        val userdata = roomViewModel.getByUsername(user.login.toString())
        userdata.observe(this, { userFav ->
            if (userFav.isFavorite) {
                activityDetailBinding.fabAddToFav.setImageResource(R.drawable.ic_favorite)
            } else {
                activityDetailBinding.fabAddToFav.setImageResource(R.drawable.ic_favorite_bordered)
            }
        })
    }

    private fun getUserDetail(): User? {
        if (Build.VERSION.SDK_INT >= 33) {
            return intent.getParcelableExtra(EXTRA_USER, User::class.java)
        } else {
            @Suppress("DEPRECATED")
            return intent.getParcelableExtra(EXTRA_USER)
        }
    }
}