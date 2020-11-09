package aochab.songbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    private val songsCollectionRef = Firebase.firestore.collection("songs")
    private lateinit var adapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView();
    }

    private fun setUpRecyclerView() {
        val options = FirestoreRecyclerOptions.Builder<Song>()
            .setQuery(songsCollectionRef, Song::class.java)
            .build()

        adapter = SongAdapter(options)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    /*
            fun removeItem(view: View) {
                val index = Random.nextInt(8)

                exampleList.removeAt(index)
                adapter.notifyItemRemoved(index)
            }*/
/*
    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = exampleList[position]
        clickedItem.title = "Clicked"
        adapter.notifyItemChanged(position)
    }
*/
/*
    private fun subscribeToRealtimeUpdates() {
        songsCollectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val songTempList = ArrayList<Song>()
                for (document in it) {
                    Log.d(TAG, "Receive song: ${document.id} ${document.data}")
                    val song = document.toObject<Song>()
                    songTempList.add(song)
                    song.imageResource = R.drawable.ic_launcher_foreground
                }
                songList = songTempList
            }
        }
    }

    private fun retrieveSongs() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = songsCollectionRef.get().await()
            val songTempList = ArrayList<Song>()
            for (document in querySnapshot.documents) {
                Log.d(TAG, "Receive song: ${document.id} ${document.data}")
                val song = document.toObject<Song>()
                if (song != null) {
                    songTempList.add(song)
                    song.imageResource = R.drawable.ic_launcher_foreground
                }
            }
            withContext(Dispatchers.Main) {
                songList = songTempList
            }
        } catch (exception: Exception) {
            Log.w(TAG, "Error getting song documents.", exception)
        }
    }

*/
/*
    private fun getSongList(): ArrayList<Song> {
        val songList = ArrayList<Song>()
        val query = songsCollectionRef.get()

        query
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, "Receive song: ${document.id} ${document.data}")

                        val song = document.toObject<Song>()
                        song.imageResource = R.drawable.ic_launcher_foreground
                        songList += song
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting song documents.", exception)
            }

        return songList
    }*/
    /* val list = ArrayList<Song>()

     for (i in 0 until size) {
         val drawable = when (i % 3) {
             0 -> R.drawable.ic_launcher_foreground
             1 -> R.drawable.ic_launcher_background
             else -> R.drawable.abc_ic_ab_back_material
         }

         val item = Song(drawable, "Item $i", "Line 2")
         list += item
     }        return list
    }
*/


}

