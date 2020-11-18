package aochab.songbook

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
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
        val buttonPlus1 = popupView.transpose_plus_1_button
        buttonPlus1.setOnClickListener { v ->
            Log.d("TransposeChordsPopUp", "Button +1 transpose chords clicked")
            val transposedChords = transposeChords(1, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonPlus2 = popupView.transpose_plus_2_button
        buttonPlus2.setOnClickListener { v ->
            Log.d("TransposeChordsPopUp", "Button +2 transpose chords clicked")
            val transposedChords = transposeChords(2, chords)
            val concatenateLyricAndChords = ConcatenateLyricAndChords()
            val lyricWithChords = concatenateLyricAndChords.concatenate(lyric, transposedChords)
            activityView.song_lyrics.text = lyricWithChords
            transposeChordsPopUp.dismiss()
        }

        val buttonPlus3 = popupView.transpose_plus_3_button
        buttonPlus3.setOnClickListener { v ->
            Log.d("TransposeChordsPopUp", "Button +3 transpose chords clicked")
            val transposedChords = transposeChords(3, chords)
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
                val chord = chordsToTranspose.substring(i, i + chordLength)
                Log.d("TAG", chord)
                i += chordLength
                if (chord.first().isLowerCase()) {
                    for (j in listOfMinorChordsToTranspose.indices) {
                        if (chord == listOfMinorChordsToTranspose[j]) {
                            transposedChords += if ((j + numOfSemitones) <= listOfMinorChordsToTranspose.lastIndex) {
                                listOfMinorChordsToTranspose[j + numOfSemitones]
                            } else {
                                val correctIndexAfterTranspose =
                                    (j + numOfSemitones) - listOfMinorChordsToTranspose.size
                                listOfMinorChordsToTranspose[correctIndexAfterTranspose]
                            }
                        }
                    }
                } else {
                    for (j in listOfMajorChordsToTranspose.indices) {
                        if (chord == listOfMajorChordsToTranspose[j]) {
                            transposedChords += if ((j + numOfSemitones) <= listOfMajorChordsToTranspose.lastIndex) {
                                listOfMajorChordsToTranspose[j + numOfSemitones]
                            } else {
                                val correctIndexAfterTranspose =
                                    (j + numOfSemitones) - listOfMajorChordsToTranspose.size
                                listOfMajorChordsToTranspose[correctIndexAfterTranspose]
                            }
                        }
                    }
                }
            } else {
                transposedChords += chordsToTranspose[i]
                i++
            }
        }
        return transposedChords
    }
}
/*
        for (i in 0 until chordsToTranspose.length) {
            var chord = chordsToTranspose[i].toString()
            val temp = 0
            while ((temp < chordsToTranspose.length) and !chord.isBlank()) {
                i += 1
            }

            if ((i + 1) < chordsToTranspose.lastIndex) { // TODO poprawić ten kod, zeby odczytywa inne C9 itd.
                if (chordsToTranspose[i + 1] == 'i') {
                    chord += "is"
                }
            }


            if (chord.first().isLetter()) {
                if (chord.first().isLowerCase()) {
                    for (j in listOfMinorChordsToTranspose.indices) {
                        if (chord == listOfMinorChordsToTranspose[j]) {
                            if (j == listOfMinorChordsToTranspose.lastIndex) {
                                transposedChords += listOfMinorChordsToTranspose[0]
                            }
                            transposedChords += listOfMinorChordsToTranspose[j + numOfSemitones]
                        }
                    }
                } else {
                    for (j in listOfMajorChordsToTranspose.indices) {
                        if (chord == listOfMajorChordsToTranspose[j]) {
                            if (j == listOfMajorChordsToTranspose.lastIndex) {
                                transposedChords += listOfMajorChordsToTranspose[0]
                            }
                            transposedChords += listOfMajorChordsToTranspose[j + numOfSemitones]
                        }
                    }
                }
            } else {
                transposedChords += chordsToTranspose[i]
            }
        }
        return transposedChords
    }

    var chordsByLines = chordsToTranspose.lines()
    for (line in chordsByLines) {
        val splittedChords = line.split(' ')
        for (chord in splittedChords) {
            Log.d("TAG", "Tu: $chord")
            if (chord.isEmpty()) {
                transposedChords += "\n"
                continue
            }
            if (chord.first().isLowerCase()) {
                for (j in listOfMinorChordsToTranspose.indices) {
                    if (chord == listOfMinorChordsToTranspose[j]) {
                        if (j == listOfMinorChordsToTranspose.lastIndex) {
                            transposedChords += listOfMinorChordsToTranspose[0] + " "
                        }
                        transposedChords += listOfMinorChordsToTranspose[j + numOfSemitones] + " "
                    }
                }
            } else {
                for (j in listOfMajorChordsToTranspose.indices) {
                    if (chord == listOfMajorChordsToTranspose[j]) {
                        if (j == listOfMajorChordsToTranspose.lastIndex) {
                            transposedChords += listOfMajorChordsToTranspose[0] + " "
                        }
                        transposedChords += listOfMajorChordsToTranspose[j + numOfSemitones] + " "
                    }
                }
            }
        }
    }
    return transposedChords
}

      for (chord in splittedChords) {
          if (chord != "\n") {
              if (chord.first().isLowerCase()) {
                  for (j in listOfMinorChordsToTranspose.indices) {
                      if (chord == listOfMinorChordsToTranspose[j]) {
                          if (j == listOfMinorChordsToTranspose.lastIndex) {
                              transposedChords += listOfMinorChordsToTranspose[0]
                          }
                          transposedChords += listOfMinorChordsToTranspose[j + numOfSemitones]
                      }
                  }
              } else {
                  for (j in listOfMajorChordsToTranspose.indices) {
                      if (chord == listOfMajorChordsToTranspose[j]) {
                          if (j == listOfMajorChordsToTranspose.lastIndex) {
                              transposedChords += listOfMajorChordsToTranspose[0]
                          }
                          transposedChords += listOfMajorChordsToTranspose[j + numOfSemitones]
                      }
                  }
              }
          } else {
              transposedChords += "\n"
          }
      }
        for (i in 0 until chordsToTranspose.length) {

            if (!chordsToTranspose[i].isLetter()) {
                transposedChords += chordsToTranspose[i]
                continue
            }

            var originalChord = ""
            var temp = 0
            while(chordsToTranspose[i].isLetter()) {
                originalChord += chordsToTranspose[i]
                temp++
            }
            if (!chordsToTranspose[i].isLetter()) {
                transposedChords += chordsToTranspose[i]
                continue
            }
            val originalChord = chordsToTranspose[i]

            if (originalChord.isLowerCase()) {
                for (j in listOfMinorChordsToTranspose.indices) {
                    if (originalChord.toString() == listOfMinorChordsToTranspose[j]) {
                        if (j == listOfMinorChordsToTranspose.lastIndex) {
                            transposedChords += listOfMinorChordsToTranspose[0]
                        }
                        transposedChords += listOfMinorChordsToTranspose[j + numOfSemitones]
                    }
                }
            } else {
                for (j in listOfMajorChordsToTranspose.indices) {
                    if (originalChord.toString() == listOfMajorChordsToTranspose[j]) {
                        if (j == listOfMajorChordsToTranspose.lastIndex) {
                            transposedChords += listOfMajorChordsToTranspose[0]
                        }
                        transposedChords += listOfMajorChordsToTranspose[j + numOfSemitones]
                    }
                }
            }
        }*/


