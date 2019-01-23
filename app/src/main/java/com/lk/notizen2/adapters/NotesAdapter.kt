package com.lk.notizen2.adapters

import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.models.Category
import com.lk.notizen2.utils.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NotesAdapter(private val dataset: List<NoteEntity>,
                   private val activity: FragmentActivity): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val TAG = "NotesAdapter"

    private lateinit var currentHolder: ViewHolder
    private lateinit var currentNote: NoteEntity

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
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_notes_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentHolder = holder
        currentNote = dataset[position]

        holder.tvId.text = currentNote.id.toString()
        holder.tvTitle.text = currentNote.title
        printTime(currentNote.date)

        if (currentNote.getLockedAsEnum() == Lock.LOCKED) {
            printProtectedNote()
        } else {
            printNormalNote()
        }
        holder.itemView.setOnLongClickListener {
            selectedNoteId = Integer.parseInt(holder.tvId.text.toString())
            false
        }
    }

    private fun printTime(timeString: String) {
        currentHolder.tvDate.text = if (timeString.contains("/")) {
            val indexSpace = timeString.indexOf(' ')
            val time = timeString.substring( indexSpace + 1)
            var datum = timeString.substring(0, indexSpace)
            val s = datum.split("/".toRegex())
            datum = s[2] + "." + s[1] + "." + s[0]
            "$datum $time"
        } else {
            timeString
        }
    }

    private fun printProtectedNote() {
        currentHolder.tbProtected.isChecked = true
        currentHolder.tbPriority.visibility = View.GONE
        setCategoryBar(currentNote.getCategoryAsEnum())
        resetViewsForProtected()
    }

    private fun resetViewsForProtected(){
        currentHolder.tvText.visibility = View.GONE
        currentHolder.tvDate.visibility = View.GONE
    }

    private fun printNormalNote(){
        val category = currentNote.getCategoryAsEnum()

        currentHolder.tvText.text = currentNote.content
        currentHolder.tbPriority.isChecked = currentNote.getPriorityAsEnum() == Priority.URGENT
        currentHolder.tbProtected.visibility = View.GONE
        setCategoryBar(category)
        currentHolder.tvText.maxLines = category.lineNumber
    }

    private fun setCategoryBar(category: Category){
        currentHolder.ivCategory.setImageResource(category.color)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnCreateContextMenuListener {

        val tvId = v.findViewById<View>(R.id.tv_list_id) as TextView
        val tvTitle = v.findViewById<View>(R.id.tv_list_title) as TextView
        val tvText = v.findViewById<View>(R.id.tv_list_content) as TextView
        val tvDate = v.findViewById<View>(R.id.tv_list_time) as TextView
        val tbPriority = v.findViewById<View>(R.id.iv_list_priority) as ToggleButton
        val tbProtected = v.findViewById<View>(R.id.iv_list_protected) as ToggleButton
        val ivCategory = v.findViewById<View>(R.id.iv_list_category) as ImageView

        init {
            v.setOnClickListener {
                val strId = tvId.text.toString()
                val id = Integer.parseInt(strId)
                listener!!.onShowNote(id)
            }
            v.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(contextMenu: ContextMenu, view: View,
                                         contextMenuInfo: ContextMenu.ContextMenuInfo?) {
            contextMenu.add(0, R.id.menu_delete, 0, R.string.action_delete)
        }


    }
}