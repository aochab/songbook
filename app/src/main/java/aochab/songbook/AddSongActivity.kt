package aochab.songbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_song.*

class AddSongActivity : AppCompatActivity() {

    companion object {
        val TAG = "AddSongActivity"
    }

    private var firestoreDB = Firebase.firestore
    private var firestoreListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_song)
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
            "lyrics" to lyrics.replace("\n","\\n"),
            "chords" to chords.replace("\n","\\n")
        )

        if( numLyricLines == numChordsLines ) {
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
            val toast = Toast.makeText(this, "Tekst i akordy musza mieć tyle samo lini!", Toast.LENGTH_LONG)
            toast.show()
        }


    }
}