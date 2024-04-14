package com.ifs21023.lostandfound.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ifs21023.lostandfound.data.remote.response.LostfoundsItemResponse
import com.ifs21023.lostandfound.databinding.ItemRowLostfoundBinding
class LostfoundAdapter :
    ListAdapter<LostfoundsItemResponse,
            LostfoundAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private var originalData = mutableListOf<LostfoundAdapter>()
    private var filteredData = mutableListOf<LostfoundAdapter>()
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowLostfoundBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = originalData[originalData.indexOf(getItem(position))]

        holder.binding.cbItemLostfoundCompleted.setOnCheckedChangeListener(null)
        holder.binding.cbItemLostfoundCompleted.setOnLongClickListener(null)
        holder.bind(data)

        holder.binding.cbItemLostfoundCompleted.setOnCheckedChangeListener { _, isChecked ->
            data.isCompleted = if (isChecked) 1 else 0
            holder.bind(data)
            onItemClickCallback.onCheckedChangeListener(data, isChecked)
        }

        holder.binding.ivItemLFDetail.setOnClickListener {
            onItemClickCallback.onClickDetailListener(data.id)
        }
    }

    class MyViewHolder(val binding: ItemRowLostfoundBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LostfoundsItemResponse) {
            binding.apply {
                tvItemLostfoundTitle.text = data.title
                bvStatus.text = data.status
                cbItemLostfoundCompleted.isChecked = data.isCompleted == 1
            }
        }
    }

    fun submitOriginalList(list: List<LostfoundsItemResponse>) {
        originalData = list.toMutableList()
        filteredData = list.toMutableList()

        submitList(originalData)
    }

    fun filter(query: String) {
        filteredData = if (query.isEmpty()) {
            originalData
        } else {
            originalData.filter {
                (it.title.contains(query, ignoreCase = true))
            }.toMutableList()
        }
        submitList(filteredData)
    }

    interface OnItemClickCallback {
        fun onCheckedChangeListener(lostfound: LostfoundsItemResponse, isChecked: Boolean)
        fun onClickDetailListener(lostfoundId: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LostfoundsItemResponse>() {
            override fun areItemsTheSame(
                oldItem: LostfoundsItemResponse,
                newItem: LostfoundsItemResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: LostfoundsItemResponse,
                newItem: LostfoundsItemResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}