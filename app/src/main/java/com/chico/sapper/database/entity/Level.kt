package com.chico.sapper.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "level")
data class Level(
    @ColumnInfo(name = "level")
    val level: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}