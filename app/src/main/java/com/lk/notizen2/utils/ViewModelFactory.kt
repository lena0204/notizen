package com.lk.notizen2.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.lk.notizen2.models.ActionViewModel
import com.lk.notizen2.models.NotesViewModel

/**
 * Erstellt von Lena am 09/12/2018.
 */
object ViewModelFactory {

    fun getActionViewModel(activity: FragmentActivity): ActionViewModel{
        return ViewModelProviders.of(activity).get(ActionViewModel::class.java)
    }

    fun getNotesViewModel(activity: FragmentActivity): NotesViewModel{
        return ViewModelProviders.of(activity).get(NotesViewModel::class.java)
    }

}