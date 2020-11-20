package aochab.songbook

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

class ConcatenateLyricAndChords {

    fun concatenate(lyric: String, chords: String) : SpannableStringBuilder {
        val lyricByLines = lyric.lines()
        val chordsByLines = chords.lines()

        val lyricWithChords = SpannableStringBuilder()

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
        return lyricWithChords
    }
}