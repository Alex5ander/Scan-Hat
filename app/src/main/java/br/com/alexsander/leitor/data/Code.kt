package br.com.alexsander.leitor.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Code(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "value") val value: String
)
