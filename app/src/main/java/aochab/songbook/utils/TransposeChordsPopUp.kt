package aochab.songbook.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import aochab.songbook.R
import kotlinx.android.synthetic.main.activity_song_detail.view.*
import kotlinx.android.synthetic.main.window_chords_transposition.view.*
import kotlin.collections.ArrayList

class TransposeChordsPopUp(private val lyric: String, private val chords: String) {

    val listOfMajorChordsToTranspose: ArrayList<String>
        get() = ArrayList(
            listOf(
                "E",
                "F",
                "Fis",
                "G",
                "Gis",
                "A",
                "B",
                "H",
                "C",
                "Cis",
                "D",
                "Dis"
            )
        )

    val listOfMinorChordsToTranspose: ArrayList<String>
        get() = ArrayList(
            listOf(
                "e",
                "f",
                "fis",
                "g",
                "gis",
                "a",
                "b",
                "h",
                "c",
                "cis",
                "d",
                "dis"
            )
        )

    @SuppressLint("ClickableViewAccessibility")
    fun showPopUp(view: View) {
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.window_chords_transposition, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        val focusable = true

        val transposeChordsPopUp = PopupWindow(popupView, width, height, focusable)
        transposeChordsPopUp.animationStyle = R.style.Animation
        val scrollView = popupView.findViewById<ScrollView>(R.id.transpose_chords_scroll_view)

        transposeChordsPopUp.showAtLocation(view, Gravity.CENTER, 0, 0)
        scrollView.post {
            val scrollViewCenterY = (scrollView.top + scrollView.bottom) / 2
            scrollView.scrollTo(0, scrollViewCenterY)
        }

        setOnClickListenersOnButtons(view, popupView, transposeChordsPopUp)

        popupView.setOnTouchListener { v, event ->
            transposeChordsPopUp.dismiss()
            true
        }
    }

    private fun setOnClickListenersOnButtons(
        activityView: View,
        popupView: View,
        transposeChordsPopUp: PopupWindow
    ) {
        val button0 = popupView.transpose_0_button
        button0.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button 0 original transposition chords clicked")
            val transposedChords = transposeChords(0, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonPlus1 = popupView.transpose_plus_1_button
        buttonPlus1.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button +1 transpose chords clicked")
            val transposedChords = transposeChords(1, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonPlus2 = popupView.transpose_plus_2_button
        buttonPlus2.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button +2 transpose chords clicked")
            val transposedChords = transposeChords(2, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonPlus3 = popupView.transpose_plus_3_button
        buttonPlus3.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button +3 transpose chords clicked")
            val transposedChords = transposeChords(3, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonPlus4 = popupView.transpose_plus_4_button
        buttonPlus4.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button +4 transpose chords clicked")
            val transposedChords = transposeChords(4, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonPlus5 = popupView.transpose_plus_5_button
        buttonPlus5.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button +5 transpose chords clicked")
            val transposedChords = transposeChords(5, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonPlus6 = popupView.transpose_plus_6_button
        buttonPlus6.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button +6 transpose chords clicked")
            val transposedChords = transposeChords(6, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonMinus1 = popupView.transpose_minus_1_button
        buttonMinus1.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button -1 transpose chords clicked")
            val transposedChords = transposeChords(-1, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonMinus2 = popupView.transpose_minus_2_button
        buttonMinus2.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button -1 transpose chords clicked")
            val transposedChords = transposeChords(-2, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonMinus3 = popupView.transpose_minus_3_button
        buttonMinus3.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button -3 transpose chords clicked")
            val transposedChords = transposeChords(-3, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonMinus4 = popupView.transpose_minus_4_button
        buttonMinus4.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button -4 transpose chords clicked")
            val transposedChords = transposeChords(-4, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonMinus5 = popupView.transpose_minus_5_button
        buttonMinus5.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button -5 transpose chords clicked")
            val transposedChords = transposeChords(-5, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonMinus6 = popupView.transpose_minus_6_button
        buttonMinus6.setOnClickListener {
            Log.d("TransposeChordsPopUp", "Button -6 transpose chords clicked")
            val transposedChords = transposeChords(-6, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }
    }

    private fun transposeChords(numOfSemitones: Int, chordsToTranspose: String): String {
        var transposedChords = ""

        var i = 0
        while (i < chordsToTranspose.length) {
            if (chordsToTranspose[i].isLetter()) {
                var chordLength = 1
                if (i + chordLength < chordsToTranspose.length) {
                    while (!chordsToTranspose[i + chordLength].toString().isBlank()) {
                        Log.d("TAG", chordLength.toString())
                        chordLength++
                    }
                }
                var chord = chordsToTranspose.substring(i, i + chordLength)
                i += chordLength

                val numInChord = chord.filter { it.isDigit() }
                val regexToDeleteNumFromChord = Regex("[0-9]")
                chord = regexToDeleteNumFromChord.replace(chord, "")

                transposedChords += if (chord.first().isLowerCase()) {
                    getNewChord(listOfMinorChordsToTranspose, chord, numOfSemitones)
                } else {
                    getNewChord(listOfMajorChordsToTranspose, chord, numOfSemitones)
                }
                transposedChords += numInChord
            } else {
                transposedChords += chordsToTranspose[i]
                i++
            }
        }
        return transposedChords
    }

    private fun getNewChord(
        listOfChordsToTransport: List<String>,
        chord: String,
        numOfSemitones: Int
    ): String {
        var transposedChord = ""
        for (j in listOfChordsToTransport.indices) {
            if (chord == listOfChordsToTransport[j]) {
                transposedChord += when {
                    ((j + numOfSemitones) > listOfChordsToTransport.lastIndex) -> {
                        val correctIndexAfterTranspose =
                            (j + numOfSemitones) - listOfChordsToTransport.size
                        listOfChordsToTransport[correctIndexAfterTranspose]
                    }
                    (j + numOfSemitones) < 0 -> {
                        val correctIndexAfterTranspose =
                            (j + numOfSemitones) + listOfChordsToTransport.size
                        listOfChordsToTransport[correctIndexAfterTranspose]
                    }
                    else -> {
                        listOfChordsToTransport[j + numOfSemitones]
                    }
                }
            }
        }
        return transposedChord
    }
}


