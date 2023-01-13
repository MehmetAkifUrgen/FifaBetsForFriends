package com.example.fifabet.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "bahis_table")
data class Bahis(
    @PrimaryKey(autoGenerate = true)
    @Expose
    @SerializedName("id")
    @NotNull
    val id:Int = 0,
    @Expose
    @SerializedName("bet")
    @NotNull
    var bet:String = "",
    @Expose
    @SerializedName("odd")
    @NotNull
    var odd:Float=0f,

    @Expose
    @SerializedName("active")
    @NotNull
    var active: String ="false"
)