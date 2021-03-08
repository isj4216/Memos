package com.example.memos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Entity 수정 시 version 업그레이드
@Database(entities = arrayOf(MemoEntity::class), version = 1)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDAO() : MemoDAO

    companion object {
        //singleton 패턴
        var INSTANCE : MemoDatabase? = null

        fun getInstance(context : Context) : MemoDatabase?{
            if(INSTANCE == null){
                synchronized(MemoDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, MemoDatabase::class.java, "memo.db")
                               .fallbackToDestructiveMigration()//version 업그레이드시 drop후 진행
                               .build()
                }
            }
            return INSTANCE
        }
    }
}