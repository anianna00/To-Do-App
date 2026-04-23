package com.example.to_do.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.activityViewModels
import com.example.to_do.R
import com.example.to_do.models.HomeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeFragment: BaseFragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val taskListView: ListView = view.findViewById(R.id.task_list_view)
        val taskListAdapter = ArrayAdapter<String>(requireContext(), R.layout.task_view, mutableListOf())

//        val token = auth.currentUser.getIdToken(false).result
        val userId = auth.currentUser?.uid
        if (userId != null){
            homeViewModel.setTasksOfUser(userId)
            observeTaskList(view, R.id.task_list_view, taskListAdapter, homeViewModel.allUncompletedTasks)
        }

        val taskEditText: EditText = view.findViewById(R.id.task_title_edit_text)
        val addNewTaskButton: Button = view.findViewById(R.id.button_add_task)

        addNewTaskButton.setOnClickListener {
            val taskToAdd = taskEditText.text.toString()
            if (taskToAdd.isNotEmpty()) {
                if (taskToAdd.contains(";")){
                    homeViewModel.makeToast(requireContext(), "Nazwa nie może zawierać średnika")
                } else {
                    if (userId != null) {
                        homeViewModel.addTask(taskToAdd, userId)
                    }
                    homeViewModel.makeToast(requireContext(), "Dodano nowy task")
                    taskEditText.text.clear()
                }
            }
        }

        // COMPLETED TASK
        taskListView.setOnItemClickListener { _, _, position, _ ->
            val taskCompleted = homeViewModel.allUncompletedTasks.value?.get(position)
            if (taskCompleted != null){
                homeViewModel.moveToCompleted(taskCompleted)
                homeViewModel.makeToast(requireContext(), "Task ukończony")
            }
        }

        // UPDATE TASK ALERT DIALOG
        taskListView.setOnItemLongClickListener { _, _, position, _ ->
            val taskToUpdate = homeViewModel.allUncompletedTasks.value?.get(position)
            if (taskToUpdate != null) {
                val dialogFragment = TaskDialogFragment.newInstance(taskToUpdate.id, taskToUpdate.name)
                dialogFragment.show(childFragmentManager, "task_dialog")
            }
            true
        }

        return view
    }

//    auth.currentUser?.getIdToken(false)?.addOnCompleteListener { task ->
//        if (task.isSuccessful) {
//            val token = task.result?.token
//            if (token != null) {
//                homeViewModel.setTasksOfUser(token)
//                observeTaskList(view, R.id.task_list_view, taskListAdapter, homeViewModel.allUncompletedTasks)
//            }
//        } else {
//            homeViewModel.makeToast(requireContext(), "Failed to get token")
//            Log.e("HomeFragment", "Token retrieval failed", task.exception)
//        }
//    }


}