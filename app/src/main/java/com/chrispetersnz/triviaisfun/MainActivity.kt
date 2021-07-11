package com.chrispetersnz.triviaisfun

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chrispetersnz.triviaisfun.databinding.ActivityMainBinding
import com.chrispetersnz.triviaisfun.ui.CategoryRecyclerViewAdapter
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    private val categoryAdapter = CategoryRecyclerViewAdapter(viewModel::itemSelected)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.screenCreated()

        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.categoryRecyclerView.adapter = categoryAdapter

        viewModel.categories.observe(this) { categories ->
            Timber.d("Categories updated")
            categoryAdapter.updateCategories(categories)
        }
    }
}