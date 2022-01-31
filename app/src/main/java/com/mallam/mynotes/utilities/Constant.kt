package com.mallam.mynotes.utilities

object Constant {

    /** Room **/
    const val DATABASE_NOTES = "notes_db"
    const val TABLE_NOTES = "notes_table"
    const val COLUMN_TITLE = "title"
    const val COLUMN_DATE = "date"
    const val COLUMN_TEXT = "description"
    const val COLUMN_IMG_PATH = "img_path"
    const val COLUMN_WEB_LINK = "web_link"
    const val COLUMN_COLOR = "color"

    /** Activity **/
    const val REQUEST_CODE_APP_UPDATE = 101
    const val REQUEST_CODE_READ_STORAGE = 123

    /** Data **/

    val colorByName = hashMapOf(
        Colors.Blue to "#3369FF",
        Colors.Yellow to "#FFDA47",
        Colors.Purple to "#AE3B76",
        Colors.Green to "#0AEBAF",
        Colors.Orange to "#FF7746",
        Colors.Black to "#171C26",
        Colors.Red to "#A82B29",
    )
}