package aochab.songbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_song_detail.*

class SongDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_detail)
        val bundle = intent.getBundleExtra("Bundle")
        val song = bundle?.getParcelable<Song>("song") as Song
        supportActionBar?.title = song.title

        val textView = song_lyrics
        textView.text = song.lyrics.replace("\\n","\n")
    }
}