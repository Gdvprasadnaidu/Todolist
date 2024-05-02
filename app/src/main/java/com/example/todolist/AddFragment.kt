package com.example.todolist

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar


class AddFragment : Fragment() {

    private lateinit var taskNameField: EditText
    private lateinit var taskReminderField: EditText
    private lateinit var calenderImageButton: ImageButton
    private lateinit var alarmImageButton: ImageButton
    private lateinit var saveButton: Button
    private lateinit var showTasksButton: Button
    private lateinit var contactImageButton: ImageButton

    private lateinit var sharedPreferences: SharedPreferences
    private val tasks = mutableListOf<Pair<String, String>>()

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        taskNameField = view.findViewById(R.id.taskname_field)
        taskReminderField = view.findViewById(R.id.taskreminder_field)
        calenderImageButton = view.findViewById(R.id.calenderimageButton)
        alarmImageButton = view.findViewById(R.id.alarmimageButton)
        saveButton = view.findViewById(R.id.saveButton)
        showTasksButton = view.findViewById(R.id.showtasks)
        contactImageButton = view.findViewById(R.id.contactimageButton)

        taskReminderField.isEnabled = false

        sharedPreferences = requireActivity().getSharedPreferences("Tasks", Context.MODE_PRIVATE)
        loadTasks()

        calenderImageButton.setOnClickListener {
            showDatePickerDialog()
        }

        alarmImageButton.setOnClickListener {
            showTimePickerDialog()
        }

        saveButton.setOnClickListener {
            saveTask()
        }

        showTasksButton.setOnClickListener {
            showTasks()
        }

        contactImageButton.setOnClickListener {
            showContactInfoDialog()
        }


        return view
    }

    private fun showContactInfoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_contact_info, null)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            selectedDate = "$day/${month + 1}/$year"
            updateTaskReminderField()
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(requireContext(), { _, hour, minute ->
            selectedTime = "$hour:$minute"
            updateTaskReminderField()
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }

    private fun updateTaskReminderField() {
        val taskReminderFormat = getString(R.string.task_reminder_format)
        val taskReminderText = String.format(taskReminderFormat, selectedDate, selectedTime)
        taskReminderField.setText(taskReminderText)
    }

    private fun saveTask() {
        val taskName = taskNameField.text.toString().trim()
        val taskReminderText = taskReminderField.text.toString().trim()

        if (taskName.isEmpty()) {
            taskNameField.error = "Task name cannot be empty"
            Toast.makeText(requireContext(), "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (taskReminderText.isEmpty()) {
            taskReminderField.error = "Task reminder cannot be empty"
            Toast.makeText(requireContext(), "Task reminder cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val task = Pair(taskName, taskReminderText)
        tasks.add(task)
        saveTasks()

        taskNameField.text.clear()
        taskReminderField.text.clear()
    }


    private fun saveTasks() {
        val editor = sharedPreferences.edit()
        val tasksSet = tasks.map { "${it.first}: ${it.second}" }.toSet()
        editor.putStringSet("tasks", tasksSet)
        editor.apply()
    }

    private fun loadTasks() {
        val taskSet = sharedPreferences.getStringSet("tasks", emptySet())
        tasks.addAll(taskSet?.map {
            val split = it.split(":")
            Pair(split[0], split[1])
        } ?: emptyList())
    }

    private fun showTasks() {
        val tasksAdapter = TasksAdapter(requireContext(), tasks)
        val tasksDialog = AlertDialog.Builder(requireContext())
            .setTitle("Tasks")
            .setAdapter(tasksAdapter, null)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("Delete") { _, _ ->
                deleteSelectedTasks()
            }
            .setNeutralButton("Clear All") { _, _ ->
                clearAllTasks()
            }
            .create()

        tasksDialog.show()
    }

    private fun deleteSelectedTasks() {
        val checkedItems = BooleanArray(tasks.size)
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Selected Tasks")
            .setMultiChoiceItems(tasks.map { it.first }.toTypedArray(), checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            .setPositiveButton("Delete") { _, _ ->
                val iterator = tasks.iterator()
                var index = 0
                while (iterator.hasNext()) {
                    iterator.next()
                    if (checkedItems[index]) {
                        iterator.remove()
                    }
                    index++
                }
                saveTasks()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun clearAllTasks() {
        AlertDialog.Builder(requireContext())
            .setTitle("Clear All Tasks")
            .setMessage("Are you sure you want to clear all tasks?")
            .setPositiveButton("Clear") { _, _ ->
                tasks.clear()
                saveTasks()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    class TasksAdapter(private val context: Context, private val tasks: List<Pair<String, String>>) :
        BaseAdapter() {

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItem(position: Int): Any {
            return tasks[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
                holder = ViewHolder()
                holder.taskNameTextView = view.findViewById(R.id.taskNameTextView)
                holder.taskReminderTextView = view.findViewById(R.id.taskReminderTextView)
                holder.taskCheckBox = view.findViewById(R.id.taskCheckBox)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }

            val task = tasks[position]
            holder.taskNameTextView.text = task.first
            holder.taskReminderTextView.text = task.second

            holder.taskCheckBox.setOnCheckedChangeListener(null)
            holder.taskCheckBox.isChecked = false

            holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->

            }

            return view
        }

        private class ViewHolder {
            lateinit var taskNameTextView: TextView
            lateinit var taskReminderTextView: TextView
            lateinit var taskCheckBox: CheckBox
        }
    }
}
