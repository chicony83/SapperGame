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
    suspend fun deleteWinner(winner: Winner)

    @Query("SELECT * FROM winner_table WHERE level_game = (:i)")
    suspend fun getWinners(i: Int): List<Winner>
}