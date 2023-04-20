package com.dicoding.gitu

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.gitu.databinding.ActivityDetailBinding
import com.dicoding.gitu.follows.SectionsPageAdapter
import com.dicoding.gitu.user.User
import com.dicoding.gitu.viewModel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var activityDetailBinding: ActivityDetailBinding
    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
    }
    companion object {
        const val EXTRA_USER = "extra_user"
        var EXTRA_USERNAME = "extra_username"

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

        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_USER, User::class.java)
        } else {
            @Suppress("DEPRECATED")
            intent.getParcelableExtra(EXTRA_USER)
        }

        supportActionBar!!.hide()

        if (user != null) {
            EXTRA_USERNAME = user.username
            DetailViewModel.USERNAME = user.username
            viewModel.userDetail.observe(this, { userDetail -> setUserDetail(userDetail)})
            viewModel.isLoading.observe(this, { showLoading(it) })
        }

        val sectionsPageAdapter = SectionsPageAdapter(this)
        val viewPager: ViewPager2 = activityDetailBinding.viewPager
        viewPager.adapter = sectionsPageAdapter
        val tabs: TabLayout = activityDetailBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

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
        val name = user.name
        val uname = user.login
        val followers = user.followers
        val following = user.following
        Glide.with(this@DetailActivity).load(user.avatarUrl).into(activityDetailBinding.imgUserProfile)
        if (name != null) {
            activityDetailBinding.tvUserName.text = name.toString()
        } else {
            activityDetailBinding.tvUserName.text = uname.toString()
        }
        activityDetailBinding.tvUserUsername.text = uname.toString()
        activityDetailBinding.tvFollowersCount.text = followers.toString()
        activityDetailBinding.tvFollowingCount.text = following.toString()
    }
}