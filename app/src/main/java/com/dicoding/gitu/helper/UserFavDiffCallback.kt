package com.dicoding.gitu.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.gitu.room.database.UserFav

class UserFavDiffCallback(private val oldUserList: List<UserFav>, private val newUserList: List<UserFav>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldUserList.size
    override fun getNewListSize(): Int = newUserList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUserList[oldItemPosition].username == newUserList[newItemPosition].username
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldUserList[oldItemPosition]
        val newUser = newUserList[newItemPosition]
        return oldUser.username == newUser.username && oldUser.avatarUrl == newUser.avatarUrl
    }
}