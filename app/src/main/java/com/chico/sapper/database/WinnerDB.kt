package com.chico.sapper.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chico.sapper.database.dao.WinnerGameDao
import com.chico.sapper.database.entity.EasyWinner
import com.chico.sapper.database.entity.HardWinner
import com.chico.sapper.database.entity.NormalWinner
import com.chico.sapper.database.entity.VeryEasyWinner

@Database(
    entities = [
        VeryEasyWinner::class,
        EasyWinner::class,
        NormalWinner::class,
        HardWinner::class
    ], version = 1)

abstract class WinnerDB : RoomDatabase(){

    abstract fun winnerGameDao():WinnerGameDao

}

object db{
    fun getDB(context: Context) =
        Room.databaseBuilder(
            context,
            WinnerDB::class.java,
            "WinnerDataBase"
        )
            .build()
}
