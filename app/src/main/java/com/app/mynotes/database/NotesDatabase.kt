package com.app.mynotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.mynotes.dao.NotesDao
import com.app.mynotes.data.Note
import com.app.mynotes.utilities.Constant

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NotesDao

    companion object {
        private var instance: NotesDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NotesDatabase {
            if(instance == null){
                instance = Room.databaseBuilder(
                    context,
                    NotesDatabase::class.java,
                    Constant.DATABASE_NOTES
                ).build()
            }
            return instance!!
        }
    }

}