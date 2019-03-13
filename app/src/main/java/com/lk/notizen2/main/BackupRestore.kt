package com.lk.notizen2.main

import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.ViewModelFactory
import java.io.*

/**
 * Erstellt von Lena am 13/03/2019.
 */
object BackupRestore {

    // TODO Toast anzeigen, ob erfolgreich oder fehlerhaft

    private const val TAG = "BackupRestore"
    private const val path = "//NotesBackup.txt"
    private lateinit var notesVM: NotesViewModel

    fun backupNotes(notes: List<NoteEntity>): Boolean {
        val notesCSVStr = readNotesFromDatabase(notes)
        return writeNotesToFile(notesCSVStr)
    }

    private fun readNotesFromDatabase(notes: List<NoteEntity>): String {
        var notesString = ""
        for (note in notes){
            notesString += readNote(note) + "#"
        }
        return notesString
    }

    private fun readNote(note: NoteEntity): String {
        var noteString = "\""
        noteString += note.id.toString() + "\";\""
        noteString += note.priority.toString() + "\";\""
        noteString += note.category.toString() + "\";\""
        noteString += note.locked.toString() + "\";\""
        noteString += note.title + "\";\""
        noteString += note.content + "\";\""
        noteString += note.date + "\""
        return noteString
    }

    private fun writeNotesToFile(notesCSVStr: String): Boolean{
        try {
            val sdcard = Environment.getExternalStorageDirectory()
            if(sdcard.canWrite()){
                val doc = File(sdcard, path)
                val writer = FileWriter(doc)
                writer.write(notesCSVStr)
                writer.close()
                Log.v(TAG, "Datei wurde hoffentlich geschrieben.")
            } else {
                Log.v(TAG, "SD-Karte kann nicht beschrieben werden.")
            }
            return true
        } catch (ex: Exception){
            Log.e(TAG, ex.message)
            return false
        }
    }

    fun restoreNotes(activity: FragmentActivity): Boolean{
        notesVM = ViewModelFactory.getNotesViewModel(activity)
        return readNotesFromFile()
    }

    private fun readNotesFromFile(): Boolean {
        try {
            val sdcard = Environment.getExternalStorageDirectory()
            if(sdcard.canRead()){
                val doc = File(sdcard, path)
                val reader = FileReader(doc)
                val text = reader.readText()
                readText(text)
                reader.close()
                Log.v(TAG, "Datei wurde hoffentlich gelesen.")
            } else {
                Log.v(TAG, "SD-Karte kann nicht gelesen werden.")
            }
            return true
        } catch (ex: Exception){
            Log.e(TAG, ex.message)
            return false
        }
    }

    private fun readText(text: String){
        val notesList = text.split("\"#\"")
        for(noteText in notesList){
            writeNoteToDatabase(noteText)
        }
    }

    private fun writeNoteToDatabase(line: String){
        val attributeList = line.split("\";\"")
        val note = readNote(attributeList)
        if(note != null)
            notesVM.insertNote(note)
    }

    private fun readNote(attrList: List<String>): NoteEntity?{
        return if(attrList.size == 7) {
            val note = NoteEntity()
            // don't read -> unique constraint can fail note.id = attrList[0].trim('\"').toInt()
            note.priority = attrList[1].toInt()
            note.category = attrList[2].trim('\"').toInt()
            note.locked = attrList[3].trim('\"').toInt()
            note.title = attrList[4]
            note.content = attrList[5]
            note.date = attrList[6]
            Log.v(TAG, note.toString())
            note
        } else {
            Log.e(TAG, "Die Zeile war fehlerhaft: " + attrList.toString())
            null
        }
    }
}