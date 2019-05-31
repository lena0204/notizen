package com.lk.notizen2.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lk.notizen2.R
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.Categories
import com.lk.notizen2.utils.ViewModelFactory

/**
 * Erstellt von Lena am 05/05/2019.
 */
class ProtectionDialog: DialogFragment() {

    private val TAG = "ProtectionDialog"

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var builder: AlertDialog.Builder
    private lateinit var listener: DialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        builder = AlertDialog.Builder(requireActivity(), R.style.DialogTheme)
        listener = requireActivity() as DialogListener
        return buildDialog()
    }

    private fun buildDialog(): Dialog {
        val dialogLayout = requireActivity().layoutInflater.inflate(R.layout.dialog_protection, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.et_dialog_protection)

        builder.setTitle(R.string.dia_request_title)
        builder.setMessage(R.string.dia_request_explanation)
        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dia_request_yes ) { _, _ ->
            val password = editText.text.toString()
            listener.dialogResult(password)
        }
        builder.setNegativeButton(R.string.dialog_cancel) { _, _ ->
            listener.dialogResult(LOCK_DIALOG_CANCELLED)
            dialog?.cancel()
        }
        return builder.create()
    }

    interface DialogListener {
        fun dialogResult(value: String)
    }

    companion object {
        const val LOCK_DIALOG_CANCELLED = "lock_dialog_cancelled"
    }

}