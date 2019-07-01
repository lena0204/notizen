package com.lk.notizen2.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lk.notizen2.R
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.ViewModelFactory
import java.lang.ClassCastException

/**
 * Erstellt von Lena am 05/05/2019.
 */
class PasswordSetDialog: DialogFragment() {

    private val TAG = "PasswordSetDialog"

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var builder: AlertDialog.Builder
    private lateinit var listener: DialogListenerPasswordSet

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        builder = AlertDialog.Builder(requireActivity(), R.style.DialogTheme)
        return buildDialog()
    }

    private fun buildDialog(): Dialog {
        val dialogLayout = requireActivity().layoutInflater.inflate(R.layout.dialog_password_set, null)
        val password1 = dialogLayout.findViewById<EditText>(R.id.et_set_password1)
        val password2 = dialogLayout.findViewById<EditText>(R.id.et_set_password2)

        builder.setTitle(R.string.dia_set_title)
        builder.setMessage(R.string.dia_set_explanation)
        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dia_request_yes ) { _, _ ->
            val pw1 = password1.text.toString()
            val pw2 = password2.text.toString()
            listener.dialogResultSetter(pw1, pw2)
        }
        builder.setNegativeButton(R.string.dialog_cancel) { _, _ ->
            listener.dialogResultSetter(SET_DIALOG_CANCELLED, "")
            dialog?.cancel()
        }
        return builder.create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            listener = activity as DialogListenerPasswordSet
        } catch (ex: ClassCastException) {
            Log.e(TAG, "Couldn't cast as listener", ex)
            dialog?.dismiss()
        }
    }

    interface DialogListenerPasswordSet {
        fun dialogResultSetter(password1: String, password2: String)
    }

    companion object {
        const val SET_DIALOG_CANCELLED = "set_dialog_cancelled"
    }

}