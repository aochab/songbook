package aochab.songbook

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_song.*
import kotlinx.android.synthetic.main.activity_song_detail.*
import kotlinx.android.synthetic.main.activity_song_detail.drawer_layout
import java.lang.Math.pow
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class SongDetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var originalChords: String
    lateinit var originalLyric: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_detail)

        val bundle = intent.getBundleExtra("Bundle")
        val song = bundle?.getParcelable<Song>("song") as Song
        supportActionBar?.title = song.title + "\n" + song.songwriter

        image_transpose_song.setOnClickListener {
            val transposeChordsPopUp = TransposeChordsPopUp(originalLyric, originalChords)
            transposeChordsPopUp.showPopUp(this.window.decorView)
        }
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

}