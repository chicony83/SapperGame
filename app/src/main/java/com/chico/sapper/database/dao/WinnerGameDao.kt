package com.chico.sapper.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.chico.sapper.database.entity.Winner

@Dao
interface WinnerGameDao {

    @Insert
    suspend fun addWinner(winner: Winner)

    @Delete
    suspend fun deleteEasyWinner(winner: Winner)

    @Query("SELECT * FROM winner_table")
    suspend fun getAllEasyWinners(): List<Winner>
}