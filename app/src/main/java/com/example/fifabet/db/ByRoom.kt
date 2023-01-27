package com.example.fifabet.db

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "byroom_table")
data class ByRoom(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @NotNull
    var id:Int = 0,
    @SerializedName("betsData")
    var betsData: List<Bahis>?,
    @SerializedName("roomCode")
    @NotNull
    var roomCode: String=""
){

}