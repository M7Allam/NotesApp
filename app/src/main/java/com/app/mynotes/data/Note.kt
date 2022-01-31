package com.app.mynotes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.mynotes.utilities.Constant
import java.io.Serializable

@Entity(tableName = Constant.TABLE_NOTES)
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = Constant.COLUMN_TITLE)
    val title: String?,

    @ColumnInfo(name = Constant.COLUMN_DATE)
    var date: Long?,

    @ColumnInfo(name = Constant.COLUMN_TEXT)
    val description: String?,

    @ColumnInfo(name = Constant.COLUMN_IMG_PATH)
    val imgPath: String?,

    @ColumnInfo(name = Constant.COLUMN_WEB_LINK)
    val webLink: String?,

    @ColumnInfo(name = Constant.COLUMN_COLOR)
    var color: String?,

    ) : Serializable {

    override fun toString() = "$title : $date"

}
