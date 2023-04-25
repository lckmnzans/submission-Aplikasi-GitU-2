package com.dicoding.gitu.room.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/*
* Kelas ini utk menentukan atribut-atribut tabel
* Tabel ini dibuat dengan annotation @Entity dan diimplementasikan Parceable juga
* */
@Entity
@Parcelize
data class UserFav (
    @PrimaryKey(autoGenerate = false)
    var avatarUrl: String? = null,
    var username: String = "",
): Parcelable