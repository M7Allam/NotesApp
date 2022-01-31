package com.mallam.mynotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mallam.mynotes.dao.NotesDao
import com.mallam.mynotes.data.Note
import com.mallam.mynotes.utilities.Constant

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NotesDao

    companion object {
        @Volatile
        private var instance: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context,
                        NotesDatabase::class.java,
                        Constant.DATABASE_NOTES
                    ).build()
                }
            }
            return instance!!
        }
    }

}