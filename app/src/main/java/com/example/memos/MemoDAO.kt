package com.example.memos

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface MemoDAO {
    @Insert(onConflict = REPLACE)
    fun insert(memo : MemoEntity)
    // onConflict = REPLACE
    // Insert시 Primary key값이 똑같으면 덮어씌움

    @Query("SELECT * FROM memo")
    fun getAll() : List<MemoEntity>

    @Delete
    fun delete(memo : MemoEntity)

    @Update
    fun update(memo : MemoEntity)

//    @Query("SELECT * FROM memo WHERE id = memo.id")
//    fun getData(memo : MemoEntity) : MemoEntity
//    //id로 데이터 찾기

//    @Query("UPDATE memo SET memo = memo.memo WHERE id = memo.id")
//    fun update(memo : MemoEntity)

}