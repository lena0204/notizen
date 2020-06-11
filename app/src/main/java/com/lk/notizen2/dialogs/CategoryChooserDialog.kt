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
import com.lk.notizen2.utils.Categories
import com.lk.notizen2.utils.ViewModelFactory

/**
 * Erstellt von Lena am 26/12/2018.
 */
class CategoryChooserDialog: DialogFragment() {

    private val TAG = "CategoryDialog"
    private lateinit var notesViewModel: NotesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let{
            val builder = AlertDialog.Builder(it, R.style.DialogTheme)
            builder.setTitle(R.string.dialog_categoryfilter_title)
            builder.setItems(Categories.getCategoryNameArray()) { _, which ->
                notesViewModel.setSelectedCategory(Categories.getCategory(which))
            }
            builder.create()
        }
    }

}