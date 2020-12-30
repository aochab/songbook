package aochab.songbook.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import aochab.songbook.R
import aochab.songbook.model.Song
import aochab.songbook.utils.SongAdapter
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
    private lateinit var searchView: SearchView
    private var loadAllSongs = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        setSupportActionBar(findViewById(R.id.appToolbar))

        imageMenu.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        val extras = intent.extras
        if (extras != null) {
            val onlyUserSongs = extras.getBoolean("OnlyUserSongs")
            if (onlyUserSongs) {
                loadAllSongs = false
            }
        }

        navigation_view.setNavigationItemSelectedListener(this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        loadSongsList()

        if (loadAllSongs) {
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
        firestoreListener?.remove()
    }

    private fun loadSongsList() {
        val songsList = ArrayList<Song>()
        if (loadAllSongs) {
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
        bundle.putParcelable("song", adapter.songs[position])
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
                    bundle.putParcelable("song", adapter.songs[position])
                    intent.putExtra("Bundle", bundle)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(intent)
                }
                2 -> {
                    val songToDelete = adapter.songs[position]
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
                startActivity(intent)
            }
            R.id.menu_public_songs -> {
                val intent = Intent(this, SonglistActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            R.id.menu_user_songs -> {
                val intent = Intent(this, SonglistActivity::class.java)
                val onlyUserSongs = true
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("OnlyUserSongs", onlyUserSongs)
                startActivity(intent)
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
        if (!searchView.isIconified) {
            searchView.setIconifiedByDefault(true)
            searchView.onActionViewCollapsed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_songlist, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                searchView.clearFocus()
                return true;
            }

            override fun onQueryTextChange(text: String?): Boolean {
                adapter.filter.filter(text)
                return false
            }
        })
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchView.setQuery("", false)
        searchView.setIconifiedByDefault(true)
        invalidateOptionsMenu()
    }
}

