package com.example.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpager.databinding.ItemRecyclerBinding
import com.google.android.material.transition.Hold
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat




class CustomAdapter: RecyclerView.Adapter<Holder>() {
    var listData = mutableListOf<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}
class Holder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setMemo(memo: Memo){
        binding.name.text = "${memo.name}"
        binding.phoneNumber.text = "${memo.phoneNumber}"
        //var sdf = SimpleDateFormat("yyyy/MM/dd")
        //var formattedDate = sdf.format(memo.timestamp)
        //binding.date.text = formattedDate
        binding.date.text = "${memo.timestamp}"
    }
}