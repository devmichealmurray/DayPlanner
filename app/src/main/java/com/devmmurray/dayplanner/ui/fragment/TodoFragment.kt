package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.model.local.TodoTask
import com.devmmurray.dayplanner.databinding.FragmentTodoBinding
import com.devmmurray.dayplanner.ui.adapter.DayPlannerRecyclerView
import com.devmmurray.dayplanner.ui.viewmodel.TodoViewModel
import com.devmmurray.dayplanner.util.flags.ListFlags
import com.devmmurray.dayplanner.util.Utils
import com.google.android.material.transition.MaterialElevationScale
import org.jetbrains.anko.support.v4.alert

private const val TAG = "To Do Fragment"

class TodoFragment : Fragment() {

    private val todoViewModel: TodoViewModel by viewModels()
    private lateinit var toDoBinding: FragmentTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Motion Transitions
        enterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toDoBinding = FragmentTodoBinding.inflate(inflater, container, false)
        return toDoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toDoBinding.setVariable(BR.todoFragment, this)

        todoViewModel.apply {
            getTasksFromDB()
            todoTaskList.observe(viewLifecycleOwner, taskListObserver)
            todoErrorMessage.observe(viewLifecycleOwner, errorMessageObserver)
        }
    }


    /**
     *  Observers
     */

    private val taskListObserver = Observer<List<TodoTask>> { list ->
        if (list.isNotEmpty()) {
            toDoBinding.apply {
                noTasks.visibility = View.INVISIBLE
                todoRecycler.apply {
                    adapter = DayPlannerRecyclerView(list, ListFlags.TODO_TASK)
                    visibility = View.VISIBLE
                }
            }
        } else {
            toDoBinding.apply {
                todoRecycler.visibility = View.INVISIBLE
                noTasks.visibility = View.VISIBLE
            }
        }
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        alert {
            title = getString(R.string.error_alert_dialog)
            message = errorMessage
            isCancelable = false
            positiveButton(getString(R.string.error_alert_okay)) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }


    /**
     *  Button Click Functionality -- addTask
     */

    fun addTask() {
        todoViewModel.prepareTask(toDoBinding.addTaskEdittext.text.toString())
        toDoBinding.addTaskEdittext.text.clear()
        toDoBinding.addTaskEdittext.clearFocus()
        context?.let { view?.let { it1 -> Utils.hideKeyboard(it, it1) } }
    }


}