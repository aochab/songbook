package aochab.songbook.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import aochab.songbook.utils.ConcatenateLyricAndChords
import aochab.songbook.utils.PinchZoomOnTouchTextViewListener
import aochab.songbook.R
import aochab.songbook.utils.TransposeChordsPopUp
import aochab.songbook.model.Song
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_song_detail.*

class SongDetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var originalChords: String
    lateinit var originalLyric: String
    lateinit var song: Song

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_detail)
        setSupportActionBar(findViewById(R.id.appToolbar))

        val toolbar = findViewById<Toolbar>(R.id.appToolbar)
        toolbar.overflowIcon?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(this, R.color.colorAccent),
            PorterDuff.Mode.SRC_ATOP
        )

        val bundle = intent.getBundleExtra("Bundle")
        song = bundle?.getParcelable<Song>("song") as Song
        text_bar_songwriter.text = song.title + "\n" + song.songwriter

        image_menu_song_detail.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        navigation_view_song_detail.setNavigationItemSelectedListener(this)

        val lyricView = song_lyrics
        val lyric = song.lyrics.replace("\\n", "\n")
        val chords = song.chords.replace("\\n", "\n")
        originalChords = chords
        originalLyric = lyric

        val concatenateLyricAndChords = ConcatenateLyricAndChords()
        val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, chords)

        lyricView.text = lyricWithChords

        lyricView.setOnTouchListener(PinchZoomOnTouchTextViewListener(lyricView))
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_transposition -> {
                val transposeChordsPopUp = TransposeChordsPopUp(originalLyric, originalChords)
                transposeChordsPopUp.showPopUp(this.window.decorView)
            }
            R.id.menu_edit_song -> {
                val intent = Intent(this, EditSongActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("song", song)
                intent.putExtra("Bundle", bundle)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_song_details, menu)
        return true
    }

}