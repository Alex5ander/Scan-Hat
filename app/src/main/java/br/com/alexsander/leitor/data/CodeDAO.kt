package br.com.alexsander.leitor.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeDAO {
    @Query("SELECT * FROM code")
    fun getAll(): Flow<List<Code>>

    @Insert
    suspend fun insert(code: Code)

    @Delete
    suspend fun delete(code: Code)
}