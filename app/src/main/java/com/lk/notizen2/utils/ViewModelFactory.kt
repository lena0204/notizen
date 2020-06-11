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

package com.lk.notizen2.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.lk.notizen2.models.NotesViewModel

/**
 * Erstellt von Lena am 09/12/2018.
 */
object ViewModelFactory {

    fun getNotesViewModel(activity: FragmentActivity): NotesViewModel{
        return ViewModelProviders.of(activity).get(NotesViewModel::class.java)
    }

}