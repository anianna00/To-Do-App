package com.example.to_do.fragments

import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.to_do.database.Task

open class BaseFragment: Fragment() {

    private fun createListView(view: View, listViewId: Int, adapter: ArrayAdapter<String>, tasks: List<Task>){
        val listView: ListView = view.findViewById(listViewId)
        val tasksName = tasks.map{it.name}
        listView.adapter = adapter
        adapter.clear()
        adapter.addAll(tasksName)
        adapter.notifyDataSetChanged()
    }

    fun observeTaskList(view: View, listViewId: Int, adapter: ArrayAdapter<String>, tasksLiveData: LiveData<List<Task>>){
        tasksLiveData.observe(viewLifecycleOwner){ tasks ->
            createListView(view, listViewId, adapter, tasks)
        }
    }
}