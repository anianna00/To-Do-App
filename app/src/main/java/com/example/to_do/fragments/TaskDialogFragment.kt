package com.example.to_do.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.to_do.R
import com.example.to_do.models.TaskDialogViewModel

class TaskDialogFragment: DialogFragment() {

    private val taskDialogViewModel: TaskDialogViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_dialog, container, false)

        val taskId = arguments?.getInt("task_id")
        val taskName = arguments?.getString("task_name")

        val updateButton: Button = view.findViewById(R.id.button_dialog_update)
        val deleteButton: Button = view.findViewById(R.id.button_dialog_delete)
        val cancelButton: Button = view.findViewById(R.id.button_dialog_cancel)

        val updateTaskText: EditText = view.findViewById(R.id.alter_dialog_edit_name)
        updateTaskText.setText(taskName)

        updateButton.setOnClickListener {
            val updatedTaskName = updateTaskText.text.toString()
            if(updatedTaskName.isNotEmpty()){
                if (taskId != null) {
                    taskDialogViewModel.updateTaskName(taskId, updatedTaskName)
                }
                dismiss()
            }
        }

        deleteButton.setOnClickListener {
            if (taskId != null) {
                taskDialogViewModel.deleteTaskById(taskId)
            }
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    companion object{
        // dzieki companion object mozna wywolywac metode bez tworzenia instancji TaskDialogFragment
        fun newInstance(taskId: Int, taskName: String): TaskDialogFragment{
            val fragment = TaskDialogFragment()
            val args = Bundle().apply {
                putInt("task_id", taskId)
                putString("task_name", taskName)
            }
            fragment.arguments = args
            return fragment
        }
    }

}