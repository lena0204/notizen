package com.lk.notizen2.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lk.notizen2.R
import com.lk.notizen2.models.Category
import com.lk.notizen2.utils.*

/**
 * Erstellt von Lena am 2019-08-05.
 */
class PersonalizationDialog: DialogFragment() {

    private val TAG = "PersonalizationDialog"
    private lateinit var categories: List<Category>
    private var spinnerPosition = -1

    private lateinit var editTextName: EditText
    private lateinit var editTextLinesNo: EditText
    private lateinit var spinner: Spinner
    private lateinit var dialogLayout: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        categories = Categories.getAllCategories()
        return requireActivity().let{
            initializeLayout()

            val builder = AlertDialog.Builder(it, R.style.DialogTheme)
            builder.setView(dialogLayout)
            builder.setPositiveButton(R.string.dia_request_yes ) { _, _ ->
                saveOldValues()
                storeValues()
            }
            builder.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
            builder.create()
        }
    }


    private fun initializeLayout() {
        dialogLayout = requireActivity().layoutInflater.inflate(R.layout.dialog_category_customization, null)
        spinner = dialogLayout.findViewById(R.id.sp_dialog_custom_category)
        editTextName = dialogLayout.findViewById(R.id.et_dialog_custom_name)
        editTextLinesNo = dialogLayout.findViewById(R.id.et_dialog_custom_rowcount)

        spinner.adapter = createSpinnerAdapter()
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
                saveOldValues()
                updateNewValues(pos)
            }

        }
    }

    private fun createSpinnerAdapter() : SpinnerAdapter {
        val defaultValue = requireContext().resources.getString(R.string.dialog_default_spinner_selection)
        val spinnerEntries = mutableListOf<String>(defaultValue)
        spinnerEntries.addAll(Categories.getCategoryNameArray())
        spinnerEntries.remove(Categories.ALL.name)
        return ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinnerEntries)
    }

    private fun saveOldValues() {
        if(spinnerPosition > 0) {
            val name = editTextName.text.toString()
            val lineNumber = editTextLinesNo.text.toString()
            if(name != "") {
                categories[spinnerPosition - 1].name = name
            }
            if(lineNumber != "") {
                categories[spinnerPosition - 1].lineNumber = lineNumber.toInt()
            }
        }
    }

    private fun updateNewValues(pos: Int) {
        spinnerPosition = pos
        var name = ""
        var lineNumber = ""
        if(spinnerPosition > 0) {
            name = categories[spinnerPosition - 1].name
            lineNumber = categories[spinnerPosition - 1].lineNumber.toString()
        }
        editTextName.setText(name, TextView.BufferType.EDITABLE)
        editTextLinesNo.setText(lineNumber, TextView.BufferType.EDITABLE)
    }

    private fun storeValues() {
        val set = mutableSetOf<String>()
        for(category in categories){
            set.add(category.flatToString())
        }
        val spw = SharedPrefWrapper(requireContext())
        spw.writeSet(Constants.PREF_PERSONALISATION, set)

        Categories.readCurrentDataFromPreferences(spw)
    }

}