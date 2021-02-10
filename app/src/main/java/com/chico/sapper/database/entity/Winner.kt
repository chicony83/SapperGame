package com.chico.sapper.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chico.sapper.dto.enums.LevelGame

@Entity(tableName = "winner_table")
data class Winner(
    @ColumnInfo(name = "level_game")
    val level:Int,

    @ColumnInfo(name = "player_name")
    val name: String,

    @ColumnInfo(name = "time")
    val time: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}