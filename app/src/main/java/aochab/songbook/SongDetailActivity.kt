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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_song_detail.*
import java.lang.Math.pow
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class SongDetailActivity : AppCompatActivity() {

    lateinit var originalChords: String
    lateinit var originalLyric: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_detail)
        val bundle = intent.getBundleExtra("Bundle")
        val song = bundle?.getParcelable<Song>("song") as Song
        supportActionBar?.title = song.title + "\n" + song.songwriter

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_song_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_transposition -> {
                val transposeChordsPopUp = TransposeChordsPopUp(originalLyric, originalChords)
                transposeChordsPopUp.showPopUp(this.window.decorView)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}