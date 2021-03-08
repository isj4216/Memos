package com.example.memos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoEntity(@PrimaryKey(autoGenerate = true)
                      var id : Int?,
                      var memo : String = "")