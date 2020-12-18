package aochab.songbook.utils

import android.view.*
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aochab.songbook.R
import aochab.songbook.model.Song
import kotlinx.android.synthetic.main.song_item.view.*

class SongAdapter(
    val songs: MutableList<Song>,
    private val listener: OnItemClickListener,
    private val listenerContextMenu: OnContextMenuListener
) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>(), Filterable {
    private var fullListOfSongs: List<Song> = ArrayList(songs)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.song_item,
            parent, false
        )
        return SongViewHolder(itemView)
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnCreateContextMenuListener {
        val songTitle: TextView = itemView.song_title
        val songwriter: TextView = itemView.songwriter

        init {
            itemView.setOnClickListener(this)
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listenerContextMenu.onContextMenu(position, menu)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnContextMenuListener {
        fun onContextMenu(position: Int, menu: ContextMenu?)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.songTitle.text = song.title
        holder.songwriter.text = song.songwriter
    }

    override fun getItemCount() = songs.size

    override fun getFilter(): Filter {
        return songFilter;
    }

    private val songFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredSongList = ArrayList<Song>()

            if(constraint == null || constraint.isEmpty()) {
                filteredSongList.addAll(fullListOfSongs)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()

                for (song in fullListOfSongs ) {
                    if(song.title.toLowerCase().contains(filterPattern) or song.songwriter.toLowerCase().contains(filterPattern)) {
                        filteredSongList.add(song)
                    }
                }
            }

            val filterResults = FilterResults()
            filterResults.values = filteredSongList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            songs.clear()
            songs.addAll(filterResults?.values as Collection<Song>)
            notifyDataSetChanged()
        }

    }
}