package com.example.retrofitpostdeleteedit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofitpostdeleteedit.adapter.RvAdapter
import com.example.retrofitpostdeleteedit.databinding.ActivityMainBinding
import com.example.retrofitpostdeleteedit.databinding.CustomDialogBinding
import com.example.retrofitpostdeleteedit.models.MyTodo
import com.example.retrofitpostdeleteedit.models.MyTodoPostRequest
import com.example.retrofitpostdeleteedit.network.MyApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), RvAdapter.RvAction {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var rvAdapter: RvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnAdd.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val customDialogBinding = CustomDialogBinding.inflate(layoutInflater)

            customDialogBinding.apply {
                btnSave.setOnClickListener {
                    val myTodoPostRequest = MyTodoPostRequest(
                        false,
                        edtIzoh.text.toString(),
                        edtTitle.text.toString()
                    )
                    MyApiClient.getApiService()
                        .addTodo(myTodoPostRequest)
                        .enqueue(object :Callback<MyTodo>{
                            override fun onResponse(
                                call: Call<MyTodo>,
                                response: Response<MyTodo>
                            ) {
                                if(response.isSuccessful){
                                    Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_SHORT).show()
                                    onResume()
                                    dialog.cancel()
                                }
                            }

                            override fun onFailure(call: Call<MyTodo>, t: Throwable) {
                                Toast.makeText(this@MainActivity, "Xatolik yuz berdi", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            }

            dialog.setView(customDialogBinding.root)
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.myProgress.visibility = View.VISIBLE

        MyApiClient.getApiService()
            .getAllTodo().enqueue(object : Callback<List<MyTodo>> {
                override fun onResponse(
                    call: Call<List<MyTodo>>,
                    response: Response<List<MyTodo>>
                ) {
                    if (response.isSuccessful) {
                        val list = response.body()
                        if (list != null) {
                            if (!::rvAdapter.isInitialized) {
                                rvAdapter = RvAdapter(list.toMutableList(), this@MainActivity)
                                binding.rv.adapter = rvAdapter
                            } else {
                                rvAdapter.setList(list)
                            }
                            binding.myProgress.visibility = View.INVISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<List<MyTodo>>, t: Throwable) {
                    binding.myProgress.visibility = View.INVISIBLE
                    Toast.makeText(this@MainActivity, "Xatolik yuz berdi", Toast.LENGTH_SHORT).show()
                }
            })
    }


    override fun edit(myTodo: MyTodo) {
        val dialog = AlertDialog.Builder(this).create()
        val customDialogBinding = CustomDialogBinding.inflate(layoutInflater)

        customDialogBinding.apply {
            edtTitle.setText(myTodo.sarlavha)
            edtIzoh.setText(myTodo.izoh)
            switchBajarildi.isChecked = myTodo.bajarildi

            btnSave.setOnClickListener {
                val myTodoPostRequest = MyTodoPostRequest(
                    switchBajarildi.isChecked,
                    edtIzoh.text.toString(),
                    edtTitle.text.toString()
                )
                MyApiClient.getApiService().updateTodo(myTodo.id, myTodoPostRequest)
                    .enqueue(object : Callback<MyTodo>{
                        override fun onResponse(call: Call<MyTodo>, response: Response<MyTodo>) {
                            if (response.isSuccessful){
                                Toast.makeText(this@MainActivity, "Edited", Toast.LENGTH_SHORT).show()
                                val updatedTodo = response.body()
                                if (updatedTodo != null) {
                                    // Yangilangan elementni adapter orqali yangilash
                                    val position = rvAdapter.updateItem(updatedTodo)
                                    rvAdapter.notifyItemChanged(position)
                                }

                                onResume()
                                dialog.cancel()
                            }
                        }

                        override fun onFailure(call: Call<MyTodo>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }

        dialog.setView(customDialogBinding.root)
        dialog.show()
    }

    override fun delete(myTodo: MyTodo) {
        MyApiClient.getApiService().deleteTodo(myTodo.id)
            .
                enqueue(object :Callback<Any>{
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        if(response.isSuccessful){
                            Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT).show()
                            onResume()

                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
    }
}