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
import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity
import com.devmmurray.dayplanner.databinding.FragmentTodoBinding
import com.devmmurray.dayplanner.ui.adapter.DayPlannerRecyclerView
import com.devmmurray.dayplanner.ui.viewmodel.TodoViewModel
import com.devmmurray.dayplanner.util.ListFlags
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import java.util.*

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

        toDoBinding.returnButton.onClick { addTask() }
        return toDoBinding.root
    }

    private fun addTask() {
        if (toDoBinding.addTaskEdittext.text.isNotEmpty()) {
            val task = toDoBinding.addTaskEdittext.text.toString().capitalize(Locale.ROOT)
            val date: Long = System.currentTimeMillis()
            val taskEntity = TodoTaskEntity(
                title = task,
                dateAdded = date.let { TimeStampProcessing.transformSystemTime(it, TimeFlags.FULL) }
            )
            toDoBinding.addTaskEdittext.text.clear()
            toDoBinding.addTaskEdittext.clearFocus()
            hideKeyboard()
            todoViewModel.addTaskToDb(taskEntity)
        }
    }

    fun removeTask(id: Long) {
        Log.d(TAG, "* * * * *  Remove Task Called with $id * * * * * ")
        todoViewModel.removeTask(id)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoViewModel.apply {
            refreshFragment.observe(viewLifecycleOwner, {
                toDoBinding.todoRecycler.adapter?.notifyDataSetChanged()
            })

            progress.observe(viewLifecycleOwner, {
                if (!it) toDoBinding.todoRecyclerProgressbar.visibility = View.INVISIBLE
            })

            todoError.observe(viewLifecycleOwner, { errorMessage ->
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

    }

    private fun hideKeyboard() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


}