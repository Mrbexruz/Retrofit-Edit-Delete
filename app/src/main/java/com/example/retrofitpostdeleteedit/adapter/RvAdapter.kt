package com.example.retrofitpostdeleteedit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitpostdeleteedit.databinding.ItemRvBinding
import com.example.retrofitpostdeleteedit.models.MyTodo

class RvAdapter(
    private val list: MutableList<MyTodo> = ArrayList(),
    private val rvAction: RvAction
) : RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(val itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(myTodo: MyTodo) {
            itemRvBinding.apply {
                tvId.text = myTodo.id.toString()
                tvName.text = myTodo.sarlavha
                tvIzoh.text = myTodo.izoh
                tvSana.text = myTodo.sana
                tvSarlavha.text = if (myTodo.bajarildi) "Bajarildi" else "Endi bajariladi"

                edit.setOnClickListener {
                    rvAction.edit(myTodo)
                }
                delete.setOnClickListener {
                    rvAction.delete(myTodo)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    // Ro‘yxatni to‘liq yangilash
    fun setList(newList: List<MyTodo>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    // Elementni ID orqali yangilash
    fun updateItem(updatedTodo: MyTodo): Int {
        val index = list.indexOfFirst { it.id == updatedTodo.id }
        if (index != -1) {
            list[index] = updatedTodo
            notifyItemChanged(index)
        }
        return index
    }

    // Elementni ID orqali o‘chirish
    fun removeItem(todoId: Int): Int {
        val index = list.indexOfFirst { it.id == todoId }
        if (index != -1) {
            list.removeAt(index)
            notifyItemRemoved(index)
        }
        return index
    }

    interface RvAction {
        fun edit(myTodo: MyTodo)
        fun delete(myTodo: MyTodo)
    }
}
