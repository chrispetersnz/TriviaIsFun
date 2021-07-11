package com.chrispetersnz.triviaisfun.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chrispetersnz.triviaisfun.R
import com.chrispetersnz.triviaisfun.network.TriviaDBService

class CategoryRecyclerViewAdapter(val checkBoxHandler: (Int, Boolean) -> Unit) :
    RecyclerView.Adapter<ViewHolder>() {

    private val categories = mutableListOf<TriviaDBService.TriviaCategory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = (categories[position].name)
        holder.checkBox.setOnClickListener {
            checkBoxHandler.invoke(position, holder.checkBox.isChecked)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun updateCategories(updatedCategories: List<TriviaDBService.TriviaCategory>) {
        categories.clear()
        categories.addAll(updatedCategories)
        notifyDataSetChanged()
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nameView: TextView = view.findViewById(R.id.categoryName)
    val checkBox: CheckBox = view.findViewById(R.id.checkbox)
}