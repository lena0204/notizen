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
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lk.notizen2.R
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.*
import java.nio.charset.MalformedInputException

/**
 * Erstellt von Lena am 2019-08-12.
 */
class FilterDialog: DialogFragment() {

    private val TAG = "FilterDialog"

    private lateinit var choices: Array<String>
    private lateinit var checked: Array<Boolean>

    private lateinit var notesViewModel: NotesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        readChoices()
        readChecked()
        return buildDialog()
    }

    private fun readChoices() {
        choices = Categories.getCategoryNameArray()
    }

    private fun readChecked() {
        checked = Array(choices.size) { i -> false }
        val spw = SharedPrefWrapper(requireContext())
        val currentFilter = spw.readSet(Constants.PREF_FILTER_CATEGORIES, setOf())
        for (i in 0 until choices.size - 1) {
            if(currentFilter.contains(i.toString())) {
                checked[i] = true
            }
        }
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.dialog_categoryfilter_title)
        builder.setMultiChoiceItems(choices, checked.toBooleanArray()) { _, index, isChecked ->
            checked[index] = isChecked
        }
        builder.setPositiveButton(R.string.dia_request_yes) { _, which ->
            writeChecked()
        }
        builder.setNegativeButton(R.string.dialog_cancel) { _, _ ->
            dialog?.cancel()
        }
        return builder.create()
    }



    private fun writeChecked() {
        val newFilter = mutableSetOf<String>()
        for (i in 0 until checked.size - 1) {
            if(checked[i]) {
                newFilter.add(i.toString())
            }
        }
        val spw = SharedPrefWrapper(requireContext())
        spw.writeSet(Constants.PREF_FILTER_CATEGORIES, newFilter)
        notesViewModel.setFilteredCategories(Categories.transformToCategoryList(newFilter))
    }

}