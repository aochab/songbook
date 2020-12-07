package aochab.songbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_song.*

class EditSongActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val TAG = "EditSongActivity"
    }

    private var firestoreDB = Firebase.firestore
    private lateinit var oldSongTitle: String
    private lateinit var oldSongSongwriter: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_song)

        image_save_song.setOnClickListener { saveSong() }
        image_menu_add_song.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        navigation_view_add_song.setNavigationItemSelectedListener(this)

        val bundle = intent.getBundleExtra("Bundle")
        val song = bundle?.getParcelable<Song>("song") as Song
        supportActionBar?.title = song.title + "\n" + song.songwriter

        val lyric = song.lyrics.replace("\\n", "\n")
        val chords = song.chords.replace("\\n", "\n")
        oldSongTitle = song.title
        oldSongSongwriter = song.songwriter

        inputSongTitle.setText(song.title)
        inputSongwriter.setText(song.songwriter)
        inputSongLyrics.setText(lyric)
        inputSongChords.setText(chords)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_song, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save_song -> {
                saveSong()
            }
        }
        return super.onOptionsItemSelected(item)
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
                .collection("song").document("$oldSongTitle - $oldSongSongwriter")
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Document successfully deleted!")

                    firestoreDB.collection("users").document(Firebase.auth.currentUser!!.uid)
                        .collection("song").document("$songTitle - $songwriter")
                        .set(song)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                            val toast =
                                Toast.makeText(this, "Zapisano piosenkę!", Toast.LENGTH_LONG)
                            toast.show()

                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error writing document", e)
                            val toast = Toast.makeText(this, "Błąd zapisu!", Toast.LENGTH_LONG)
                            toast.show()
                        }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error deleting document", e)
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
                // intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_public_songs -> {
                val intent = Intent(this, SonglistActivity::class.java)
                //  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                //zrobic sprawdzenie czy to aktualny intent, jak tak to nie starujemy nowego

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
}