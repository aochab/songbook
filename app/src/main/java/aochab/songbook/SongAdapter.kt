package aochab.songbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.song_item.view.*

class SongAdapter(private val songs: List<Song>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.song_item,
            parent, false
        )
        return SongViewHolder(itemView)
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.image_view
        val songTitle: TextView = itemView.song_title
        val songwriter: TextView = itemView.songwriter

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.imageView.setImageResource(song.imageResource)
        holder.songTitle.text = song.title
        holder.songwriter.text = song.songwriter
    }

    override fun getItemCount() = songs.size
}
/*
class SongAdapter(
    private val songList: List<Song>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.song_item,
            parent, false
        )

        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentItem = songList[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.songTitle.text = currentItem.title
        holder.songwriter.text = currentItem.songwriter
    }

    override fun getItemCount() = songList.size

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener { b
        val imageView: ImageView = itemView.image_view
        val songTitle: TextView = itemView.song_title
        val songwriter: TextView = itemView.songwriter

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
*/