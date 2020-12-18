package aochab.songbook.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
    var imageResource: Int = 0,
    var chords: String = "chords",
    var title: String = "title",
    var songwriter: String = "songwriter",
    var lyrics: String = "lyrics"
) : Parcelable