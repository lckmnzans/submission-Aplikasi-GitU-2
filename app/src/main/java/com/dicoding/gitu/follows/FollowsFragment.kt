package com.dicoding.gitu.follows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.gitu.Items
import com.dicoding.gitu.api.ApiConfig
import com.dicoding.gitu.databinding.FragmentFollowsBinding
import com.dicoding.gitu.user.User
import com.dicoding.gitu.viewModel.DetailViewModel

class FollowsFragment : Fragment() {
    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "section_login"
    }
    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME).toString()

        binding.rvFollowsList.layoutManager = LinearLayoutManager(requireContext())
        if (position == 1) {
            viewModel.getUserFollowsDetail(ApiConfig.getApiService().getFollowers(username))
            viewModel.isLoading.observe(viewLifecycleOwner, { showLoading(it) })
        } else {
            viewModel.getUserFollowsDetail(ApiConfig.getApiService().getFollowing(username))
            viewModel.isLoading.observe(viewLifecycleOwner, { showLoading(it) })
        }
        viewModel.userFollowsList.observe(viewLifecycleOwner) { users -> setUsers(users) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUsers(users: List<Items>) {
        val list = ArrayList<User>()
        for (user in users) {
            val userData = User(user.avatarUrl.toString(), user.login.toString())
            list.add(userData)
        }
        val listUser = UserFollowsAdapter(list)
        binding.rvFollowsList.adapter = listUser
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}