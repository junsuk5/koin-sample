package com.example.koinsample.repository

import com.example.koinsample.model.Todo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

class Controller(val service: BusinessService)
class BusinessService {
    val name = "서비스"
}

interface HelloRepository {
    fun giveHello(): String
}

class HelloRepositoryImpl : HelloRepository {
    override fun giveHello() = "Hello Koin"
}

interface Webservice {
    @GET("todos/{id}")
    fun getTodo(@Path("id") userId: Int): Single<Todo>
}
