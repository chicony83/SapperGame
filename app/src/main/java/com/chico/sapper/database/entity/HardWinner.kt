package com.chico.sapper.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hard_table")
data class HardWinner(
    @ColumnInfo(name = "player_name")
    val name: String,

    @ColumnInfo(name = "time")
    val time: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}