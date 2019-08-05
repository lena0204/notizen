package com.lk.notizen2.main

import com.lk.notizen2.dialogs.PasswordSetDialog

/**
 * Erstellt von Lena am 13/05/2019.
 */
object PasswordChecker {

    fun checkNewPasswords(password1: String, password2: String) : Boolean {
        if(password1 == PasswordSetDialog.SET_DIALOG_CANCELLED) {
            return false
        }
        return (password1 == password2) && password1.length >= 4
    }

    fun isInputPasswordCorrect(inputPassword: String, correct: String) : Boolean {
        return (inputPassword == correct)
    }


}