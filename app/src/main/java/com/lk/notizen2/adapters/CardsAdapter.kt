package com.lk.notizen2.adapters

import android.view.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity

/**
 * Erstellt von Lena am 01/12/2018.
 */
class CardsAdapter(private val dataset: List<NoteEntity>,
                   private val activity: FragmentActivity
): RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    private val TAG = "NotesAdapter"

    private lateinit var currentHolder: ViewHolder
    var selectedNoteId: Int = 0
        private set

    companion object {
        var listener: onClickListener? = null
    }

    interface onClickListener {
        fun onShowNote(noteId: Int)
    }

    fun setListener(cl: onClickListener) {
        listener = cl
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentHolder = holder
        val data = dataset[position]
        holder.tvTitle.text = data.title
        holder.tvText.text = data.content
        holder.cv.setCardBackgroundColor(
            activity.resources.getColor(data.getCategoryAsEnum().color)
        )
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnCreateContextMenuListener {

        val tvTitle = v.findViewById<View>(R.id.tv_list_title) as TextView
        val tvText = v.findViewById<View>(R.id.tv_list_content) as TextView
        val cv = v.findViewById<View>(R.id.cv_show_note) as CardView

        init {
            v.setOnClickListener {}
            v.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(contextMenu: ContextMenu, view: View,
                                         contextMenuInfo: ContextMenu.ContextMenuInfo?) {
            contextMenu.add(0, R.id.menu_delete, 0, R.string.action_delete)
        }
    }


}