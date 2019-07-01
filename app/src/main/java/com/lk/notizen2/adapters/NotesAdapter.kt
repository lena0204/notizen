package com.lk.notizen2.adapters

import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.models.Category

/**
 * Erstellt von Lena am 06.10.18.
 */
class NotesAdapter(private val dataset: List<NoteEntity>):
        RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val TAG = "NotesAdapter"

    private lateinit var currentHolder: ViewHolder
    private lateinit var currentNote: NoteEntity

    var selectedNoteId: Int = 0
        private set

    companion object {
        var listener: OnClickListener? = null
    }

    interface OnClickListener {
        fun onShowNote(note: Int)
    }

    fun setListener(cl: OnClickListener) {
        listener = cl
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_notes_cardview,
            parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentHolder = holder
        currentNote = dataset[position]
        if (currentNote.isProtected()) {
            printProtectedNote()
        } else {
            printOpenNote()
        }
        setLongClickListener(currentNote.id)
    }

    private fun printBasicNoteData(){
        currentHolder.tvId.text = currentNote.id.toString()
        currentHolder.tvTitle.text = currentNote.title
        currentHolder.tvDate.text = getFormattedDate(currentNote.date)
    }

    private fun getFormattedDate(dateTime: String): String {
        val indexOfSpace = dateTime.indexOf(' ')
        val time = dateTime.substring( indexOfSpace + 1)
        var date = dateTime.substring(0, indexOfSpace)
        val dateParts = date.split("/".toRegex())
        date = dateParts[2] + "." + dateParts[1] + "." + dateParts[0]
        return "$date $time"
    }

    private fun printProtectedNote() {
        printBasicNoteData()
        currentHolder.tbProtected.isChecked = true
        setCategoryBar(currentNote.getCategoryAsEnum())
        resetViewsForProtected()
    }

    private fun resetViewsForProtected(){
        currentHolder.tvText.visibility = View.GONE
        currentHolder.tvDate.visibility = View.GONE
    }

    private fun printOpenNote(){
        printBasicNoteData()
        val category = currentNote.getCategoryAsEnum()
        setCategoryBar(category)

        currentHolder.tvText.text = currentNote.content
        currentHolder.tbProtected.visibility = View.GONE
        currentHolder.tvText.maxLines = category.lineNumber
    }

    private fun setCategoryBar(category: Category){
        currentHolder.ivCategory.setImageResource(category.color)
    }

    private fun setLongClickListener(id: Int){
        currentHolder.itemView.setOnLongClickListener {
            selectedNoteId = id
            false
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnCreateContextMenuListener {

        val tvId = v.findViewById<View>(R.id.tv_list_id) as TextView
        val tvTitle = v.findViewById<View>(R.id.tv_list_title) as TextView
        val tvText = v.findViewById<View>(R.id.tv_list_content) as TextView
        val tvDate = v.findViewById<View>(R.id.tv_list_time) as TextView
        val tbProtected = v.findViewById<View>(R.id.iv_list_protected) as ToggleButton
        val ivCategory = v.findViewById<View>(R.id.iv_list_category) as ImageView

        init {
            v.setOnClickListener {
                // IDEA_ adapterposition verweist den Index im dataset
                val strId = tvId.text.toString()
                val id = Integer.parseInt(strId)
                listener!!.onShowNote(id)
            }
            v.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(contextMenu: ContextMenu,
                                         view: View,
                                         contextMenuInfo: ContextMenu.ContextMenuInfo?) {
            contextMenu.add(0, R.id.menu_delete, 0, R.string.action_delete)
        }


    }
}