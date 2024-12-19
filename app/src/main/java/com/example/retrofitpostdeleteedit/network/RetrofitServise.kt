package com.example.retrofitpostdeleteedit.network

import retrofit2.Call
import retrofit2.http.GET
import com.example.retrofitpostdeleteedit.models.MyTodo
import com.example.retrofitpostdeleteedit.models.MyTodoPostRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("rejalar")//read
    fun getAllTodo(): Call<List<MyTodo>>

    @POST("rejalar/")// add
    fun addTodo(@Body myTodoPostRequest: MyTodoPostRequest):Call<MyTodo>

    @DELETE("rejalar/{id}/")
    fun deleteTodo(@Path("id") id:Int):Call<Any>

    @PUT("rejalar/{id}/")
    fun updateTodo(@Path("id") id :Int, @Body myTodoPostRequest: MyTodoPostRequest) : Call<MyTodo>


}
