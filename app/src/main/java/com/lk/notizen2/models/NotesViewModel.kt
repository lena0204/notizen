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

package com.lk.notizen2.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.database.NotesRepository
import com.lk.notizen2.utils.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NotesViewModel(application: Application): AndroidViewModel(application) {

    private val TAG = "NotesViewModel"

    private val repository = NotesRepository(application)

    private val selectedNote = MutableLiveData<NoteEntity>()
    private val selectedCategory = MutableLiveData<Category>()

    private val filteredNotes = MediatorLiveData<List<NoteEntity>>()
    private val filterCategories = MutableLiveData<List<Category>>()

    private val notes = repository.getNotesList()

    private val notesAction = MutableLiveData<NavigationActions>()

    // init: initialize LiveData values
    init {
        filterCategories.value = listOf(Categories.ALL)
        selectedCategory.value = Categories.ALL
        selectedNote.value = NoteEntity()
        notesAction.value = NavigationActions.SHOW_LIST

        filteredNotes.addSource(filterCategories) {
            filteredNotes.value = filterNotes(it)
        }
        filteredNotes.addSource(notes) {
            filteredNotes.value = filterNotes(filterCategories.value!!)
        }
    }

    private fun filterNotes(categories: List<Category>): List<NoteEntity> {
        Log.v(TAG, "filterNotes: $categories")
        return when {
            notes.value == null -> listOf()
            categories[0] == Categories.ALL -> notes.value!!
            else -> {
                notes.value!!.filter {
                    categories.contains(Categories.getCategory(it.category))
                }
            }
        }
    }

    fun doAction(action: NavigationActions, note: NoteEntity) {
        when(action) {
            NavigationActions.SHOW_LIST,
            NavigationActions.NEW_NOTE -> {
                selectedNote.value = NoteEntity()
                selectedCategory.value = Categories.WHITE
            }
            NavigationActions.SHOW_NOTE,
            NavigationActions.EDIT_NOTE -> selectedNote.value = note
            NavigationActions.SHOW_PREFERENCES -> { }
            NavigationActions.SAVE_NOTE -> saveNote(note)
            NavigationActions.DELETE_NOTE -> deleteNoteFromId(note)
        }
        notesAction.value = action
    }

    fun doAction(action: NavigationActions, noteID: Int) {
        val note = findNoteById(noteID)
        doAction(action, note)
    }

    private fun findNoteById(id: Int): NoteEntity {
        return filteredNotes.value?.find { note -> note.id == id } ?: NoteEntity()
    }

    private fun saveNote(note: NoteEntity) {
        val oldNote = findNoteById(note.id)
        if(oldNote.isEmpty()) {
            repository.insertNote(note)
        } else {
            repository.updateNote(note)
        }
        selectedNote.value = note
        // PROBLEM_ Die ID fehlt und somit wird die Notiz erneut eingetragen, bei pause/ resume -> erstmal abgeschaltet
        Log.d(TAG, selectedNote.value?.toLimitedString())
    }

    private fun deleteNoteFromId(note: NoteEntity) {
        repository.deleteNote(note)
    }

    // add Observers
    fun observeListAndActions(owner: LifecycleOwner, observer: Observer<Any>){
        filteredNotes.observe(owner, observer)
        notesAction.observe(owner, observer)
    }
    fun observeSelectedCategory(owner: LifecycleOwner, observer: Observer<Any>) {
        selectedCategory.observe(owner, observer)
    }
    fun observeSelectedNote(owner: LifecycleOwner, observer: Observer<Any>) {
        selectedNote.observe(owner, observer)
    }

    // backup & restore
    fun deleteAllNotes() {
        Log.d(TAG, "Will delete ALL notes!")
        repository.deleteAllNotes()
    }

    fun insertAllNotes(notes: List<NoteEntity>) {
        for(note in notes) {
            repository.insertNote(note)
        }
    }

    // Getter and setter for LiveData
    fun setFilteredCategories(categories: List<Category>) {
        filterCategories.value = categories
    }
    fun getFilteredNotes(): List<NoteEntity> = filteredNotes.value!!
    fun setSelectedCategory(category: Category) {
        selectedCategory.value = category
    }
    fun getSelectedNote(): NoteEntity = selectedNote.value!!
}