package aochab.songbook

import android.annotation.SuppressLint
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
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_song_detail.*
import java.lang.Math.pow
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class SongDetailActivity : AppCompatActivity() {

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

        val lyricByLines = lyric.lines()
        //  lyricByLines.setSpan(ForegroundColorSpan(Color.BLACK), 0, lyricByLines.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val chordsByLines = chords.lines()
        // chordsByLines.setSpan(ForegroundColorSpan(Color.RED), 0, chordsByLines.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        // chordsByLines.setSpan(StyleSpan(Typeface.BOLD), 0, chordsByLines.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        var lyricWithChords = SpannableStringBuilder()
        for (i in lyricByLines.indices) {
            val lyricLineWithFormat = SpannableString(lyricByLines[i])
            lyricLineWithFormat.setSpan(
                ForegroundColorSpan(Color.BLACK),
                0,
                lyricLineWithFormat.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val chordsLineWithFormat = SpannableString(chordsByLines[i])
            chordsLineWithFormat.setSpan(
                ForegroundColorSpan(Color.RED),
                0,
                chordsLineWithFormat.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            chordsLineWithFormat.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                chordsLineWithFormat.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            lyricWithChords.append(
                TextUtils.concat(
                    lyricLineWithFormat,
                    "  ",
                    chordsLineWithFormat,
                    "\n"
                )
            )

        }
        lyricView.text = lyricWithChords

        lyricView.setOnTouchListener(PinchZoomOnTouchTextViewListener(lyricView))
    }

}