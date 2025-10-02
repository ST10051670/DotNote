package com.example.dotnote

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dotnote.adapter.NotesAdapter
import com.example.dotnote.model.Note
import androidx.appcompat.widget.Toolbar

class NotesList : AppCompatActivity() {

    private var focusModeEnabled = false
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        val notes = listOf(
            Note("First Note", "This is the content of the first note."),
            Note("Second Note", "This is another note.")
        )

        val prefs = getSharedPreferences("DotNotePrefs", MODE_PRIVATE)
        val textSize = prefs.getString("textSize", "Medium") ?: "Medium"
        val fontStyle = prefs.getString("fontStyle", "Default") ?: "Default"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewNotes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter(notes, textSize, fontStyle)
        recyclerView.adapter = adapter


        applyFocusMode()
    }

    override fun onResume() {
        super.onResume()

        val prefs = getSharedPreferences("DotNotePrefs", MODE_PRIVATE)
        focusModeEnabled = prefs.getBoolean("focusMode", false)
        toggleFocusMode(focusModeEnabled)

        val textSize = prefs.getString("textSize", "Medium") ?: "Medium"
        val fontStyle = prefs.getString("fontStyle", "Default") ?: "Default"
        adapter.updateTextSize(textSize)
        adapter.updateFontStyle(fontStyle)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_list_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, Settings::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun applyFocusMode() {
        val prefs = getSharedPreferences("DotNotePrefs", MODE_PRIVATE)
        val isFocusMode = prefs.getBoolean("focusMode", false)

        if (isFocusMode) {
            supportActionBar?.title = ""
        } else {
            supportActionBar?.title = "Your Notes"
        }
    }

    private fun toggleFocusMode(enabled: Boolean) {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        if (enabled) {
            toolbar.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            supportActionBar?.title = ""
            toolbar.elevation = 0f
        } else {
            toolbar.setBackgroundColor(getColor(R.color.purple_500))
            supportActionBar?.title = "Your Notes"
            toolbar.elevation = 4f
        }
    }
}
