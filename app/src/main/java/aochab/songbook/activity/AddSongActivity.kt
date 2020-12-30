package aochab.songbook.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import aochab.songbook.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_song.*
import kotlinx.android.synthetic.main.activity_add_song.drawer_layout

class AddSongActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val TAG = "AddSongActivity"
    }

    private var firestoreDB = Firebase.firestore
    private var firestoreListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_song)

        setSupportActionBar(findViewById(R.id.appToolbar))

        image_menu_add_song.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        navigation_view_add_song.setNavigationItemSelectedListener(this)
    }

    private fun saveSong() {
        val songTitle = inputSongTitle.text.toString()
        val songwriter = inputSongwriter.text.toString()
        val lyrics = inputSongLyrics.text.toString()
        val chords = inputSongChords.text.toString()
        val numLyricLines = lyrics.lines().count()
        val numChordsLines = chords.lines().count()

        val song = hashMapOf(
            "title" to songTitle,
            "songwriter" to songwriter,
            "lyrics" to lyrics.replace("\n", "\\n"),
            "chords" to chords.replace("\n", "\\n")
        )

        if (numLyricLines == numChordsLines) {
            firestoreDB.collection("users").document(Firebase.auth.currentUser!!.uid)
                .collection("song").document("$songTitle - $songwriter")
                .set(song)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    val toast = Toast.makeText(this, "Zapisano piosenkę!", Toast.LENGTH_LONG)
                    toast.show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error writing document", e)
                    val toast = Toast.makeText(this, "Błąd zapisu!", Toast.LENGTH_LONG)
                    toast.show()
                }
        } else {
            val toast =
                Toast.makeText(this, "Tekst i akordy musza mieć tyle samo lini!", Toast.LENGTH_LONG)
            toast.show()
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                Firebase.auth.signOut()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_add_song -> {
                val intent = Intent(this, AddSongActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_public_songs -> {
                val intent = Intent(this, SonglistActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            R.id.menu_user_songs -> {
                val intent = Intent(this, SonglistActivity::class.java)
                val onlyUserSongs = true
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("OnlyUserSongs", onlyUserSongs)
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save_song -> {
                saveSong()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_song, menu)
        return true
    }
}