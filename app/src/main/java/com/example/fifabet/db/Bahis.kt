package com.example.fifabet.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "bahis_table")
data class Bahis(
    @PrimaryKey(autoGenerate = true)
    @Expose
    @SerializedName("id")
    val id:Int = 0,
    @Expose
    @SerializedName("bet")
    var bet:String = "",
    @Expose
    @SerializedName("odd")
    var odd:Float=0f
)