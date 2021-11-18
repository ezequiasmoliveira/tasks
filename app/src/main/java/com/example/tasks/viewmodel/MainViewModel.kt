package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.local.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mShardPreferences = SecurityPreferences(application)

    private val mUserName = MutableLiveData<String>()
    private val mLogout = MutableLiveData<Boolean>()

    var userName: LiveData<String> = mUserName
    var logout: LiveData<Boolean> = mLogout

    fun loadUserName() {
        mUserName.value = mShardPreferences.get(TaskConstants.SHARED.PERSON_NAME)
    }

    fun logout() {
        mShardPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        mShardPreferences.remove(TaskConstants.SHARED.PERSON_KEY)
        mShardPreferences.remove(TaskConstants.SHARED.PERSON_NAME)

        mLogout.value = true
    }
}