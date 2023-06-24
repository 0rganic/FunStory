package com.example.funstory.Ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.funstory.databinding.LoadingBinding

class LoadingPagingAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingPagingAdapter.LoadingPagingViewHolder>()  {
    class LoadingPagingViewHolder(private val binding: LoadingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnReload.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState) {

            binding.loading.isVisible = loadState is LoadState.Loading
            binding.btnReload.isVisible = loadState is LoadState.Error

        }
    }
    override fun onBindViewHolder(
        holder: LoadingPagingViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingPagingViewHolder{
        val binding = LoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingPagingViewHolder(binding, retry)
    }
}