package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)

    private val mTasks = MutableLiveData<List<TaskModel>>()
    private val mValidation = MutableLiveData<ValidationListener>()

    var tasks: LiveData<List<TaskModel>> = mTasks
    var validation: LiveData<ValidationListener> = mValidation

    private var mTaskFilter = 0

    fun list(taskFilter: Int){
        mTaskFilter = taskFilter

        val listener = object : APIListener<List<TaskModel>>{
            override fun onSucess(value: List<TaskModel>) {
                mTasks.value = value
            }

            override fun onFailure(message: String) {
                mTasks.value = arrayListOf()
                mValidation.value = ValidationListener(message)
            }
        }

        if (mTaskFilter == TaskConstants.FILTER.ALL) {
            mTaskRepository.all(listener)
        } else if (mTaskFilter == TaskConstants.FILTER.NEXT) {
            mTaskRepository.nextWeek(listener)
        } else {
            mTaskRepository.overdue(listener)
        }
    }

    fun delete(id: Int){
        mTaskRepository.delete(id, object : APIListener<Boolean>{
            override fun onSucess(value: Boolean) {
                list(mTaskFilter)
                mValidation.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationListener(message)
            }
        })
    }

    fun complete(id: Int){
        updateStatus(id, true)
    }

    fun undo(id: Int){
        updateStatus(id, false)
    }

    private fun updateStatus(id: Int, complete: Boolean){
        mTaskRepository.updateStatus(id, complete, object : APIListener<Boolean>{
            override fun onSucess(value: Boolean) {
                list(mTaskFilter)
            }

            override fun onFailure(message: String) { }
        })
    }
}