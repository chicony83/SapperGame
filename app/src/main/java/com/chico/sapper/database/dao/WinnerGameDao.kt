package com.chico.sapper.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.chico.sapper.database.entity.EasyWinner

@Dao
interface WinnerGameDao {

    @Insert
    suspend fun addEasyWinner(easyWinner: EasyWinner)

    @Delete
    suspend fun deleteEasyWinner(easyWinner: EasyWinner)

    @Query("SELECT * FROM easy_winner_table")
    suspend fun getAllEasyWinners(): List<EasyWinner>
}