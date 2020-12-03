package com.example.itda.neologismfolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itda.R
import kotlinx.android.synthetic.main.neologism_recycle.view.*


class NeologismRecycleAdapter : RecyclerView.Adapter<NeologismRecycleAdapter.Holder>() {
    var listData = mutableListOf<NeologismData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.neologism_recycle, parent, false)
        return Holder(view)
    }
    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = listData.get(position)
        holder.setData(data)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var inputData: NeologismData? = null
        init {
//            itemView.buttonDelete.setOnClickListener {
//                helper?.deleteMemo(mMemo!!)
//                listData.remove(mMemo)
//                notifyDataSetChanged()
//            }
        }
        fun setData(data: NeologismData) {
            itemView.neologism_name.text = data.name
            itemView.neologism_definition.text = data.definition
            this.inputData =data
        }
    }
}

