package com.example.to_do.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.activityViewModels
import com.example.to_do.R
import com.example.to_do.models.TaskViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class CompletedFragment: BaseFragment() {

    private val taskViewModel: TaskViewModel by activityViewModels()
    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_completed, container, false)

        val completedTaskListView: ListView = view.findViewById(R.id.completed_tasks_list)
        val completedTaskListAdapter = ArrayAdapter<String>(requireContext(), R.layout.task_view, mutableListOf())

        val userId = auth.currentUser?.uid

        if(userId != null){
            taskViewModel.setTasksOfUser(userId)
            observeTaskList(view, R.id.completed_tasks_list, completedTaskListAdapter, taskViewModel.allCompletedTasks)
        }

        completedTaskListView.setOnItemLongClickListener{_, _, position, _ ->
            val taskToDelete = taskViewModel.allCompletedTasks.value?.get(position)
            if (taskToDelete != null){
                val dialogFragment = TaskDialogFragment.newInstance(taskToDelete.id, taskToDelete.name)
                dialogFragment.show(childFragmentManager, "task_dialog")
            }
            true
        }

        return view
    }
}