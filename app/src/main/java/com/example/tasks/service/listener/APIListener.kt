package com.example.tasks.service.listener

import com.example.tasks.service.model.HeaderModel

interface APIListener<T> {

    fun onSucess(value: T)
    fun onFailure(message: String)
}