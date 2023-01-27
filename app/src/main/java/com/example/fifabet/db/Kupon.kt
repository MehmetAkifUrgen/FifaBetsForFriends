package com.example.fifabet.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "kupon_table")
data class Kupon(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @NotNull
    val id:Int = 0,
    @SerializedName("kuponlar")
    var kupons: List<Bahis>?,

)