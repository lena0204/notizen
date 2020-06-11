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
import android.util.Log
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
    private var checkedItem: Int = 0

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
        if(currentFilter.isEmpty()) {
            checkedItem = Categories.ALL.id
        } else {
            for (value in currentFilter) {
                checkedItem = value.toInt()
            }
        }
        /*Log.d(TAG, "readChecked: currentFilter=$currentFilter")
        for (i in choices.indices) {
            if(currentFilter.contains(i.toString())) {
                checked[i] = true
            }
        }*/
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.dialog_categoryfilter_title)
        /*builder.setMultiChoiceItems(choices, checked.toBooleanArray()) { _, index, isChecked ->
            checked[index] = isChecked
            resetCheckedIfAllSelected(index)
        }*/
        builder.setSingleChoiceItems(choices, checkedItem) { _, index ->
            checkedItem = index
            writeChecked()
            dialog?.cancel()
        }
        /*builder.setPositiveButton(R.string.dia_request_yes) { _, which ->
            writeChecked()
        }
        builder.setNegativeButton(R.string.dialog_cancel) { _, _ ->
            dialog?.cancel()
        }*/
        return builder.create()
    }

    private fun resetCheckedIfAllSelected(index: Int) {
        if(index == checked.size - 1) {
            for(i in 0 until checked.size - 1) {
                checked[i] = false
            }
        }
        Log.d(TAG, "resetCheckedIfAllSelected: $checked")
    }

    private fun writeChecked() {
        val newFilter = mutableSetOf<String>()
        /*for (i in checked.indices) {
            if(checked[i]) {
                newFilter.add(i.toString())
            }
        }
        if(newFilter.size == 0) {
            newFilter.add(Categories.ALL.id.toString()) // add all as filter
        }*/
        val spw = SharedPrefWrapper(requireContext())
        newFilter.add(checkedItem.toString())
        spw.writeSet(Constants.PREF_FILTER_CATEGORIES, newFilter)
        Log.d(TAG, "writeChecked: newFilter= $newFilter")
        notesViewModel.setFilteredCategories(Categories.transformToCategoryList(newFilter))
    }

}