package com.lk.notizen2.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lk.notizen2.R
import com.lk.notizen2.main.MainActivity
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.Categories
import com.lk.notizen2.utils.ViewModelFactory

/**
 * Erstellt von Lena am 26/12/2018.
 */
class CategoryDialog: DialogFragment() {

    private val TAG = "CategoryDialog"
    private lateinit var notesViewModel: NotesViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let{
            val builder = AlertDialog.Builder(it, R.style.DialogTheme)
            builder.setTitle(R.string.category_dialog_title)
            builder.setItems(R.array.category_items) { _, which ->
                notesViewModel.selectedCategory.value = Categories.getCategory(which)
            }
             builder.create()
        }
    }

}