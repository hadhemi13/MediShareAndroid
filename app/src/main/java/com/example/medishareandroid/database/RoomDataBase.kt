package com.example.medishareandroid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medishareandroid.models.User

@Database(entities = [User::class], version = 1)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao


    companion object {

        //Singleton
        private var instance: RoomDataBase? = null
        fun getDatabase(context: Context): RoomDataBase {
            if (instance != null)
                return instance!!

            instance = Room.databaseBuilder(context, RoomDataBase::class.java, "users")
                .allowMainThreadQueries().build()
            return instance!!
        }
    }
}