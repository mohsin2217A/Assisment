package com.app.assisment.presentation.university_listing

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.assisment.data.remote.model.UniversityResponseItem
import com.app.assisment.databinding.ItemUniversityBinding

class UniversityListingAdapter(
    private val listener: UniversityItemListener,
    private val list: ArrayList<UniversityResponseItem>
) : RecyclerView.Adapter<UniversityListingAdapter.AdapterViewHolder>() {

    interface UniversityItemListener {
        fun onClickedItem(item: UniversityResponseItem)
    }

    class AdapterViewHolder(
        private val binding: ItemUniversityBinding,
        private val listener: UniversityItemListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var universityResponseItem: UniversityResponseItem

        init {
            binding.countryItem.setOnClickListener(this)
        }

        fun bind(universityResponseItem: UniversityResponseItem) {
            this.universityResponseItem = universityResponseItem
            binding.universityName.text = universityResponseItem.name
            if (universityResponseItem.state != null) {
                binding.universityState.text = universityResponseItem.state
            } else {
                binding.universityState.text = "N/A"
            }
        }

        override fun onClick(v: View?) {
            listener.onClickedItem(universityResponseItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding =
            ItemUniversityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) =
        holder.bind(list[position])

    @SuppressLint("NotifyDataSetChanged")
    fun addData(addList: List<UniversityResponseItem>) {
        list.addAll(addList)
        notifyDataSetChanged()
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun clear() {
//        list.clear()
//        notifyDataSetChanged()
//    }

    fun clear() {
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    private fun remove(r: UniversityResponseItem) {
        val position = list.indexOf(r)
        if (position > -1) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun getItem(position: Int): UniversityResponseItem {
        return list[position]
    }
}