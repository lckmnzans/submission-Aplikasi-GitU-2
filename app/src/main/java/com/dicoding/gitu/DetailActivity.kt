package com.dicoding.gitu

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
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
    private lateinit var username: String
    private lateinit var avatarUrl: String

    private val detailViewModel: DetailViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]
    }
    private val roomViewModel by viewModels<FavoriteViewModel> {
        RoomViewModelFactory.getInstance(application)
    }

    companion object {
        const val EXTRA_USER = "extra_user"

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

        val user = getParceableData()
        if (user != null) {
            username = user.username
            avatarUrl = user.photo
            DetailViewModel.username = user.username
            detailViewModel.userDetail.observe(this) { userDetail ->
                setUserDetail(userDetail)
            }
            detailViewModel.isLoading.observe(this) { showLoading(it) }
            sectionsPageAdapter = SectionsPageAdapter(this, user.username)
        }
        roomViewModel.getByUsername(username).observe(this) { a_user ->
            if ( a_user != null ) {
                activityDetailBinding.fabAddToFav.setImageResource(R.drawable.ic_favorite)
                activityDetailBinding.fabAddToFav.setOnClickListener {
                    roomViewModel.delete(a_user)
                    Toast.makeText(this, "Berhasil dihapus dari daftar favorit", Toast.LENGTH_SHORT).show()
                    activityDetailBinding.fabAddToFav.setImageResource(R.drawable.ic_favorite_bordered)
                }
            } else {
                activityDetailBinding.fabAddToFav.setImageResource(R.drawable.ic_favorite_bordered)
                activityDetailBinding.fabAddToFav.setOnClickListener {
                    val userFav = UserFav(username, avatarUrl)
                    roomViewModel.insert(userFav)
                    activityDetailBinding.fabAddToFav.setImageResource(R.drawable.ic_favorite)
                    Toast.makeText(this, "Berhasil ditambahkan ke daftar favorit", Toast.LENGTH_SHORT).show()
                }
            }
        }

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
        Glide.with(this@DetailActivity).load(user.avatarUrl).into(activityDetailBinding.imgUserProfile)
        if (user.name != null) {
            activityDetailBinding.tvUserName.text = user.name.toString()
        } else {
            activityDetailBinding.tvUserName.text = user.login.toString()
        }
        activityDetailBinding.tvUserUsername.text = user.login.toString()
        activityDetailBinding.tvFollowersCount.text = user.followers.toString()
        activityDetailBinding.tvFollowingCount.text = user.following.toString()
    }

    private fun getParceableData(): User? {
        if (Build.VERSION.SDK_INT >= 33) {
            return intent.getParcelableExtra(EXTRA_USER, User::class.java)
        } else {
            @Suppress("DEPRECATED")
            return intent.getParcelableExtra(EXTRA_USER)
        }
    }
}