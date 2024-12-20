package com.example.myandroiddevhw20.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey
    @ColumnInfo(name = "uri")
    val uri: String = "",
    @ColumnInfo(name = "date")
    val date: String
) : Parcelable

