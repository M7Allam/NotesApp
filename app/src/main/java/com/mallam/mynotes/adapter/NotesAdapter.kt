package com.mallam.mynotes.adapter


import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mallam.mynotes.R
import com.mallam.mynotes.data.Note
import com.mallam.mynotes.databinding.ItemNoteBinding
import com.mallam.mynotes.utilities.*
import java.util.*

class NotesAdapter(
    private var notes: List<Note>,
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private lateinit var context: Context
    lateinit var onItemClickListener: ItemNotesAdapterOnCLick<Note>
    lateinit var onWebUrlClickListener: WebUrlNotesAdapterOnCLick<Note>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemNoteBinding.inflate(LayoutInflater.from(context.applicationContext), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.setBinding(note)
        holder.onItemClick(note)
        holder.onTvWebLinkClick(note)
    }

    override fun getItemCount() = notes.size

    fun setNotesList(notesList: List<Note>) {
        notes = notesList
    }

    inner class ViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setBinding(note: Note) {
            setTextsBinding(note.title, note.description)
            setDateBinding(note.date)
            setImageBinding(note.imgPath)
            setWebLingBinding(note.webLink)
            setNoteColor(note.color)
        }

        private fun setTextsBinding(title: String?, text: String?) {
            binding.title = title
            binding.description = text
        }

        private fun setDateBinding(dateLong: Long?) {
            if (dateLong == null) {
                val date = Date()
                val pattern = getDatePattern(date)
                var dateStr = convertDateToString(date, pattern)
                if (dateStr.length == 5) {
                    val temp = dateStr
                    dateStr = "Today, $temp"
                }
                binding.date = dateStr
            } else {
                val pattern = getDatePattern(Date(dateLong))
                var dateStr = convertDateLongToString(dateLong, pattern)
                if (dateStr.length == 5) {
                    val temp = dateStr
                    dateStr = "Today, $temp"
                }
                binding.date = dateStr
            }
        }

        private fun setImageBinding(imgPath: String?) {
            if (!imgPath.isNullOrEmpty()) {
                binding.isImageNote = true
                binding.imgNote.setImageBitmap(BitmapFactory.decodeFile(imgPath))

            } else
                binding.isImageNote = false
        }

        private fun setWebLingBinding(webLink: String?) {
            if (!webLink.isNullOrEmpty()) {
                binding.tvWebLink.text = webLink
                binding.isWebLinkNote = true
            } else
                binding.isWebLinkNote = false
        }

        private fun setNoteColor(color: String?) {
            binding.cardView.setCardBackgroundColor(Color.parseColor(color))
            setTextsColorBinding(color)
        }

        private fun setTextsColorBinding(color: String?) {
            when (color) {
                Constant.colorByName[Colors.Blue],
                Constant.colorByName[Colors.Red],
                Constant.colorByName[Colors.Purple] -> {
                    binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.tvDescription.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white_light
                        )
                    )
                    binding.tvDate.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white_light_extra
                        )
                    )
                    binding.tvWebLink.setTextColor(ContextCompat.getColor(context, R.color.green))
                }

                Constant.colorByName[Colors.Orange] -> {
                    binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.tvDescription.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white_light
                        )
                    )
                    binding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.text_color))
                    binding.tvWebLink.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.url_color
                        )
                    )
                }

                Constant.colorByName[Colors.Yellow], Constant.colorByName[Colors.Green] -> {
                    binding.tvTitle.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.black_dark
                        )
                    )
                    binding.tvDescription.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.text_color
                        )
                    )
                    binding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.text_color))
                    binding.tvWebLink.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.url_color
                        )
                    )
                }

                Constant.colorByName[Colors.Black] -> {
                    binding.tvTitle.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white_light
                        )
                    )
                    binding.tvDescription.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.text_color
                        )
                    )
                    binding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.text_color))
                    binding.tvWebLink.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.url_color
                        )
                    )
                }
            }
        }

        fun onItemClick(note: Note) {
            binding.cardView.setOnClickListener {
                onItemClickListener(note, adapterPosition)
            }
        }

        fun onTvWebLinkClick(note: Note) {
            binding.tvWebLink.setOnClickListener {
                onWebUrlClickListener(note)
            }
        }


    }

}