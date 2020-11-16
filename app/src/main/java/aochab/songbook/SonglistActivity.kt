package aochab.songbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_songlist.*
import java.io.Serializable


class SonglistActivity : AppCompatActivity(), SongAdapter.OnItemClickListener {

    companion object {
        val TAG = "SonglistActivity"
    }

    private lateinit var adapter: SongAdapter
    private var firestoreDB = Firebase.firestore
    private var firestoreListener: ListenerRegistration? = null
    private var songsList = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        loadSongsList()

        firestoreListener = firestoreDB!!.collection("songs")
            .addSnapshotListener { documentSnapshots, exception ->
                if (exception != null) {
                    Log.e(TAG, "Listen failed!", exception);
                    return@addSnapshotListener
                }

                for (doc in documentSnapshots!!) {
                    val song = doc.toObject(Song::class.java)
                    if (!songsList.contains(song)) {
                        songsList.add(song)
                    }
                }
                songsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, {it.title}))
                adapter = SongAdapter(songsList, this)

                recycler_view.adapter = adapter
            }

        firestoreDB!!.collection("users").document(Firebase.auth.currentUser!!.uid)
            .collection("song")
            .addSnapshotListener { documentSnapshots, exception ->
                if (exception != null) {
                    Log.e(TAG, "Listen failed!", exception);
                    return@addSnapshotListener
                }

                for (doc in documentSnapshots!!) {
                    val song = doc.toObject(Song::class.java)
                    if (!songsList.contains(song)) {
                        songsList.add(song)
                    }
                }

                songsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, {it.title}))
                adapter = SongAdapter(songsList, this)

                recycler_view.adapter = adapter
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        firestoreListener!!.remove()
    }

    private fun loadSongsList() {
        val songsList = ArrayList<Song>()
        firestoreDB!!.collection("songs")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (doc in task.result!!) {
                        val song = doc.toObject(Song::class.java)
                        // TODO sprawdzanie czy piosenka  dodana po treści
                        if (!songsList.contains(song)) {
                            songsList.add(song)
                        }
                    }
                    songsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, {it.title}))
                    adapter = SongAdapter(songsList, this)
                    recycler_view!!.itemAnimator = DefaultItemAnimator()
                    recycler_view!!.adapter = adapter
                } else {
                    Log.d(
                        MainActivity.TAG,
                        "Error getting documents: ",
                        task.exception
                    )
                }
            }

        firestoreDB!!.collection("users").document(Firebase.auth.currentUser!!.uid)
            .collection("song")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (doc in task.result!!) {
                        val song = doc.toObject(Song::class.java)
                        if (!songsList.contains(song)) {
                            songsList.add(song)
                        }
                    }
                    songsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, {it.title}))
                    adapter = SongAdapter(songsList, this)

                    recycler_view!!.itemAnimator = DefaultItemAnimator()
                    recycler_view!!.adapter = adapter
                } else {
                    Log.d(
                        MainActivity.TAG,
                        "Error getting documents: ",
                        task.exception
                    )
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_sign_out -> {
                Firebase.auth.signOut()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, SongDetailActivity::class.java)
        val bundle = Bundle()
        for (song in songsList) {
            Log.d(TAG, "$position ${song.title}")
        }
        bundle.putParcelable("song", songsList[position])
        intent.putExtra("Bundle", bundle)
        startActivity(intent)
    }
}

