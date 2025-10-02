package com.example.dotnote.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dotnote.model.Note
import com.example.dotnote.R

class NotesAdapter(
    private val notes: List<Note>,
    private var textSizePref: String = "Medium",
    private var fontStylePref: String = "Default"
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.noteTitle)
        val contentText: TextView = itemView.findViewById(R.id.noteContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleText.text = note.title
        holder.contentText.text = note.content

        // 🔹 Apply text size
        val sizeSp = when (textSizePref) {
            "Small" -> 14f
            "Medium" -> 18f
            "Large" -> 22f
            else -> 18f
        }
        holder.titleText.textSize = sizeSp
        holder.contentText.textSize = sizeSp

        // 🔹 Apply font style
        val typeface = when (fontStylePref) {
            "Serif" -> Typeface.SERIF
            "Monospace" -> Typeface.MONOSPACE
            "Sans" -> Typeface.SANS_SERIF
            else -> Typeface.DEFAULT
        }
        holder.titleText.typeface = typeface
        holder.contentText.typeface = typeface
    }

    override fun getItemCount(): Int = notes.size

    fun updateTextSize(newSize: String) {
        textSizePref = newSize
        notifyDataSetChanged()
    }

    fun updateFontStyle(newStyle: String) {
        fontStylePref = newStyle
        notifyDataSetChanged()
    }
}
