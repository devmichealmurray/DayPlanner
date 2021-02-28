package com.devmmurray.dayplanner.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.databinding.FragmentTodoBinding
import com.devmmurray.dayplanner.ui.adapter.DayPlannerRecyclerView
import com.devmmurray.dayplanner.ui.viewmodel.TodoViewModel
import com.devmmurray.dayplanner.util.ListFlags
import org.jetbrains.anko.support.v4.alert

private const val TAG = "To Do Fragment"

class TodoFragment : Fragment() {

    private val todoViewModel: TodoViewModel by viewModels()
    private lateinit var toDoBinding: FragmentTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toDoBinding = FragmentTodoBinding.inflate(inflater, container, false)
        todoViewModel.getTasksFromDB()

        todoViewModel.todoTaskList.observe(viewLifecycleOwner, { list ->
            val taskList = list.map { it.toTodoTaskObject() }
            toDoBinding.todoRecycler.apply {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = DayPlannerRecyclerView(taskList, ListFlags.TODO)
                visibility = View.VISIBLE
            }
        })

        toDoBinding.returnButton.setOnClickListener { addTask() }
        return toDoBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoViewModel.todoErrorMessage.observe(viewLifecycleOwner, { errorMessage ->
                alert {
                    title = getString(R.string.error_alert_dialog)
                    message = errorMessage
                    isCancelable = false
                    positiveButton(getString(R.string.error_alert_okay)) { dialog ->
                        dialog.dismiss()
                    }
                }.show()
            })
    }

    private fun addTask() {
        Log.d(TAG, "* * * * * Add Task Called * * * * * ")
        todoViewModel.prepareTask(toDoBinding.addTaskEdittext.text.toString())
        toDoBinding.addTaskEdittext.text.clear()
        toDoBinding.addTaskEdittext.clearFocus()
        hideKeyboard()
    }


    private fun hideKeyboard() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


}