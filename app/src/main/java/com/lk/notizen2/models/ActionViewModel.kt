package com.lk.notizen2.models

import android.app.Application
import androidx.lifecycle.*
import com.lk.notizen2.utils.NotesAction

/**
 * Erstellt von Lena am 09/12/2018.
 */
class ActionViewModel(application: Application): AndroidViewModel(application) {

    private val TAG = "ActionObserver"

    private var notesAction = MutableLiveData<NotesAction>()

    init {
        notesAction.value = NotesAction.NONE
    }

    fun setObserver(owner: LifecycleOwner, observer: Observer<NotesAction>){
        notesAction.observe(owner, observer)
    }

    fun setAction(action: NotesAction){
        notesAction.value = action
    }

    fun getActionValue(): NotesAction{
        return notesAction.value ?: NotesAction.NONE
    }

}