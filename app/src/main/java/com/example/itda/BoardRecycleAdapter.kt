package com.example.itda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BoardRecycleAdapter : RecyclerView.Adapter<BoardRecycleAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_recycle, parent, false)
        return Holder(view)
    }
    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
//            itemView.buttonDelete.setOnClickListener {
//                helper?.deleteMemo(mMemo!!)
//                listData.remove(mMemo)
//                notifyDataSetChanged()
            }
        }

    }

