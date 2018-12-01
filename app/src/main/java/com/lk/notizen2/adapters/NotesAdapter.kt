package com.lk.notizen2.adapters

import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.main.Themer
import com.lk.notizen2.models.Category
import com.lk.notizen2.utils.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NotesAdapter(private val dataset: List<NoteEntity>,
                   private val activity: FragmentActivity): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

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
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_cardview_2, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentHolder = holder
        // 0 -> Id, 1 -> Priority, 2 -> Title, 3 -> Text, 4 -> Farbe, 5 -> Datum
        // passenden Array aus der Liste holen
        val data = dataset[position]
        val priority = data.getPriorityAsEnum()
        val strText = data.content
        val strColor = data.getCategoryAsEnum()
        holder.tvId.text = data.id.toString()
        holder.tvTitle.text = data.title
        printTime(data.date)
        if (isProtected(priority)) {
            printProtectedNote()
        } else {
            printNormalNote(strColor, priority, strText)
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
            var datum = timeString.substring(0, indexSpace - 1)
            val s = datum.split("/".toRegex())
            datum = s[2] + "." + s[1] + "." + s[0]
            "$datum $time"
        } else {
            timeString
        }
    }

    private fun isProtected(priority: Priority) =
        priority == Priority.REMINDER_LOCKED || priority == Priority.URGENT_LOCKED

    private fun printProtectedNote(){
        // Schloss Icon setzen, -1 setzt das Schloss Icon
        // currentHolder.ivPriority.setImageDrawable(Themer.getStatusImage(Priority.URGENT_LOCKED, activity))
        currentHolder.tvPriority.setText(R.string.status_protected)
        currentHolder.ivPriority.isChecked = false
        currentHolder.tbProtected.isChecked = true
        /*currentHolder.tvColor.background =
                Category.createDrawableForColor(Categories.WHITE.color, activity.resources)*/
        currentHolder.cvNote.setCardBackgroundColor(activity.resources.getColor(Categories.WHITE.color))
        resetViewsForProtected()
    }

    private fun resetViewsForProtected(){
        currentHolder.tvText.text = ""
        currentHolder.tvText.setLines(0)
        currentHolder.tvCategory.text = ""
        currentHolder.tvDate.setLines(0)
    }

    private fun printNormalNote(category: Category, priority: Priority, text: String){
        // currentHolder.ivPriority.setImageDrawable()
        /*currentHolder.tvColor.background =
                Category.createDrawableForColor(category.color, activity.resources)*/
        currentHolder.cvNote.setCardBackgroundColor(activity.resources.getColor(category.color))
        currentHolder.tvText.text = text
        if (priority == Priority.REMINDER) {
            currentHolder.tvPriority.setText(R.string.reminder)
            currentHolder.ivPriority.isChecked = false
        } else {
            currentHolder.tvPriority.setText(R.string.urgent)
            currentHolder.ivPriority.isChecked = true
        }
        currentHolder.tvText.maxLines = 2
        currentHolder.tvCategory.text = "Preview"
        // limitLinesForColor(category.number)
        // printColourCategory(category.number)
    }

   /* private fun limitLinesForColor(colorId: Int){
        if (checkLinesLimitForColour(colorId) == -2) {
            if (checkLinesLimitForColour(7) == -2) {
                currentHolder.tvText.maxLines = 0
            } else if (checkLinesLimitForColour(7) != -1) {
                currentHolder.tvText.maxLines = checkLinesLimitForColour(7)
            }
        } else if (checkLinesLimitForColour(colorId) != -1) {
            currentHolder.tvText.maxLines = checkLinesLimitForColour(colorId)
        }
    }

    private fun checkLinesLimitForColour(colourId: Int): Int {
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
        val lines = sp.getString(Constants.ARRAY_PREF_LINES[colourId], "-2") as String
        return Integer.parseInt(lines)
    }

    private fun printColourCategory(iFarbe: Int) {
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
        val name = sp.getString(Constants.ARRAY_PREF_TITLES[iFarbe], "")
        if (name != "") {
            currentHolder.tvCategory.text = name
        }
    }*/

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnCreateContextMenuListener {

        // val tvColor = v.findViewById<View>(R.id.label_color) as TextView
        val tvId = v.findViewById<View>(R.id.tv_list_id) as TextView
        val tvPriority = v.findViewById<View>(R.id.tv_list_priority) as TextView
        val tvCategory = v.findViewById<View>(R.id.tv_list_category) as TextView
        val tvTitle = v.findViewById<View>(R.id.tv_list_title) as TextView
        val tvText = v.findViewById<View>(R.id.tv_list_content) as TextView
        val tvDate = v.findViewById<View>(R.id.tv_list_time) as TextView
        val ivPriority = v.findViewById<View>(R.id.iv_list_priority) as ToggleButton
        val tbProtected = v.findViewById<View>(R.id.iv_list_protected) as ToggleButton
        val cvNote = v.findViewById<View>(R.id.cv_show_note) as CardView

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