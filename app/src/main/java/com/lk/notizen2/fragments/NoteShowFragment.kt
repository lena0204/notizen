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

package com.lk.notizen2.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.models.*
import com.lk.notizen2.utils.*
import kotlinx.android.synthetic.main.fragment_show.*
import kotlinx.android.synthetic.main.fragment_show.view.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NoteShowFragment: Fragment(), Observer<Any> {

    private val TAG = "NoteShowFragment"

    private lateinit var viewModel: NotesViewModel
    private lateinit var currentNote: NoteEntity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_show, container, false)
        root.tv_show_content.setOnClickListener {
            Log.d(TAG, "Clicked")
            viewModel.doAction(NavigationActions.EDIT_NOTE, currentNote)
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        viewModel.observeSelectedCategory(this, this)
    }

    private fun printNote(note: NoteEntity){
        currentNote = note
        tv_show_id.text = currentNote.id.toString()
        tv_show_title.text = currentNote.title
        tv_show_content.text = currentNote.content
        tb_show_protected.isChecked = currentNote.isProtected()
        tv_show_category.text = currentNote.getCategoryAsEnum().name
        iv_show_category.setImageResource(currentNote.getCategoryAsEnum().color)
    }

    override fun onChanged(selectedNote: Any) {
        if(selectedNote is NoteEntity){
            printNote(selectedNote)
        }
    }

    override fun onResume() {
        super.onResume()
        printNote(viewModel.getSelectedNote())
    }
}