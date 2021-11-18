package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val mPriorities = MutableLiveData<List<PriorityModel>>()
    private val mValidation = MutableLiveData<ValidationListener>()
    private val mTask = MutableLiveData<TaskModel>()

    var priorities: LiveData<List<PriorityModel>> = mPriorities
    var validation: LiveData<ValidationListener> = mValidation
    var task: LiveData<TaskModel> = mTask


    fun list(){
        mPriorities.value = mPriorityRepository.list()
    }

    fun save(task: TaskModel){
        if (task.id == 0) {
            mTaskRepository.create(task, object : APIListener<Boolean>{
                override fun onSucess(value: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mValidation.value = ValidationListener(message)
                }
            })
        } else {
            mTaskRepository.update(task, object : APIListener<Boolean>{
                override fun onSucess(value: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mValidation.value = ValidationListener(message)
                }
            })
        }
    }

    fun load(id: Int) {
        mTaskRepository.load(id, object : APIListener<TaskModel>{
            override fun onSucess(value: TaskModel) {
                mTask.value = value
            }

            override fun onFailure(message: String) {
                TODO("Not yet implemented")
            }

        })
    }

}