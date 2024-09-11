package br.com.alexsander.leitor.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Code::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun codeDAO(): CodeDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this)
            {
                instance ?: Room.databaseBuilder(context, AppDatabase::class.java, "leitor-db")
                    .enableMultiInstanceInvalidation()
                    .build().also { instance = it }
            }
        }
    }
}