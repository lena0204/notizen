/*
 * Copyright (c) 2020 Lena Kociemba
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lk.notizen2.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
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
class ProtectionDialog: DialogFragment() {

    private val TAG = "ProtectionDialog"

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var builder: AlertDialog.Builder
    private lateinit var listener: DialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        builder = AlertDialog.Builder(requireActivity(), R.style.DialogTheme)
        return buildDialog()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            listener = activity as DialogListener
        } catch (ex: ClassCastException) {
            Log.e(TAG, "Couldn't cast as listener", ex)
            dialog?.dismiss()
        }
    }

    private fun buildDialog(): Dialog {
        val dialogLayout = requireActivity().layoutInflater.inflate(R.layout.dialog_protection, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.et_dialog_protection)

        editText.setOnEditorActionListener { _, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_GO) {
                val password = editText.text.toString()
                listener.dialogResultProtection(password)
                dialog?.cancel()
            }
            true
        }

        builder.setTitle(R.string.dia_request_title)
        builder.setMessage(R.string.dia_request_explanation)
        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dia_request_yes ) { _, _ ->
            val password = editText.text.toString()
            listener.dialogResultProtection(password)
        }
        builder.setNegativeButton(R.string.dialog_cancel) { _, _ ->
            listener.dialogResultProtection(LOCK_DIALOG_CANCELLED)
            dialog?.cancel()
        }
        return builder.create()
    }

    interface DialogListener {
        fun dialogResultProtection(value: String)
    }

    companion object {
        const val LOCK_DIALOG_CANCELLED = "lock_dialog_cancelled"
    }

}