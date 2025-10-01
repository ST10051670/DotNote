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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        // Set up the toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Your Notes"

        // Example notes (replace later with Firestore data)
        val notes = listOf(
            Note("First Note", "This is the content of the first note."),
            Note("Second Note", "This is another note.")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewNotes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NotesAdapter(notes)
    }

    // Inflate the menu (gear icon)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_list_menu, menu)
        return true
    }

    // Handle gear icon click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, Settings::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
