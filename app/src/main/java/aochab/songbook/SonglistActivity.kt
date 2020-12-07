package aochab.songbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_songlist.*


class SonglistActivity : AppCompatActivity(), SongAdapter.OnItemClickListener,
    SongAdapter.OnContextMenuListener, NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val TAG = "SonglistActivity"
    }

    private lateinit var adapter: SongAdapter
    private var firestoreDB = Firebase.firestore
    private var firestoreListener: ListenerRegistration? = null
    private var songsList = mutableListOf<Song>()
    private var publicSongList = mutableListOf<Song>()
    private var userSongList = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        imageMenu.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        navigation_view.setNavigationItemSelectedListener(this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        loadSongsList()

        firestoreListener = firestoreDB.collection("songs")
            .addSnapshotListener { documentSnapshots, exception ->
                if (exception != null) {
                    Log.e(TAG, "Listen failed!", exception);
                    return@addSnapshotListener
                }
                songsList.clear()
                publicSongList.clear()

                for (doc in documentSnapshots!!) {
                    val song = doc.toObject(Song::class.java)
                    publicSongList.add(song)
                }
                songsList.addAll(publicSongList)
                songsList.addAll(userSongList)
                songsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.title }))
                adapter = SongAdapter(songsList, this, this)

                recycler_view.adapter = adapter
            }

        firestoreDB.collection("users").document(Firebase.auth.currentUser!!.uid)
            .collection("song")
            .addSnapshotListener { documentSnapshots, exception ->
                if (exception != null) {
                    Log.e(TAG, "Listen failed!", exception);
                    return@addSnapshotListener
                }

                songsList.clear()
                userSongList.clear()
                for (doc in documentSnapshots!!) {
                    val song = doc.toObject(Song::class.java)
                    userSongList.add(song)
                }
                songsList.addAll(publicSongList)
                songsList.addAll(userSongList)
                songsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.title }))
                adapter = SongAdapter(songsList, this, this)

                recycler_view.adapter = adapter
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        firestoreListener!!.remove()
    }

    private fun loadSongsList() {
        val songsList = ArrayList<Song>()
        firestoreDB.collection("songs")
            .get()
            .addOnCompleteListener { task ->
                songsList.clear()
                publicSongList.clear()
                if (task.isSuccessful) {
                    for (doc in task.result!!) {
                        val song = doc.toObject(Song::class.java)
                        publicSongList.add(song)
                    }
                    songsList.addAll(publicSongList)
                    songsList.addAll(userSongList)
                    songsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.title }))
                    adapter = SongAdapter(songsList, this, this)
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

        firestoreDB.collection("users").document(Firebase.auth.currentUser!!.uid)
            .collection("song")
            .get()
            .addOnCompleteListener { task ->
                songsList.clear()
                userSongList.clear()
                if (task.isSuccessful) {
                    for (doc in task.result!!) {
                        val song = doc.toObject(Song::class.java)
                        userSongList.add(song)
                    }
                    songsList.addAll(userSongList)
                    songsList.addAll(publicSongList)
                    songsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.title }))
                    adapter = SongAdapter(songsList, this, this)

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

    override fun onItemClick(position: Int) {
        val intent = Intent(this, SongDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("song", songsList[position])
        intent.putExtra("Bundle", bundle)
        startActivity(intent)
    }

    override fun onContextMenu(position: Int, menu: ContextMenu?) {
        val edit = menu?.add(Menu.NONE, 1, 1, "Edytuj");
        val delete = menu?.add(Menu.NONE, 2, 2, "Usuń")
        edit?.setOnMenuItemClickListener(onClickContextMenu(position))
        delete?.setOnMenuItemClickListener(onClickContextMenu(position))
    }

    private fun onClickContextMenu(position: Int): MenuItem.OnMenuItemClickListener =
        MenuItem.OnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> {
                    val intent = Intent(this, EditSongActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable("song", songsList[position])
                    intent.putExtra("Bundle", bundle)
                    startActivity(intent)
                }
                2 -> {
                    val songToDelete = songsList[position]
                    if (songToDelete in userSongList) {
                        firestoreDB.collection("users").document(Firebase.auth.currentUser!!.uid)
                            .collection("song")
                            .document("${songToDelete.title} - ${songToDelete.songwriter}")
                            .delete()
                            .addOnSuccessListener {
                                val toast =
                                    Toast.makeText(this, "Usunięto piosenkę", Toast.LENGTH_LONG)
                                toast.show()
                            }
                            .addOnFailureListener { e ->
                                Log.w(EditSongActivity.TAG, "Error deleting document", e)
                                val toast = Toast.makeText(
                                    this,
                                    "Błąd usunięcia piosenki!",
                                    Toast.LENGTH_LONG
                                )
                                toast.show()
                            }
                    } else {
                        val toast = Toast.makeText(
                            this,
                            "Nie możesz usunąć publicznej piosenki!",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
            }
            true
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

