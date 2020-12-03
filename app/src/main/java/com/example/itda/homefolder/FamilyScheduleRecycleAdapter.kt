package com.example.itda.homefolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itda.R
import kotlinx.android.synthetic.main.schedule_recycle.view.*

class FamilyScheduleRecycleAdapter : RecyclerView.Adapter<FamilyScheduleRecycleAdapter.Holder>() {
    var listData = mutableListOf<String?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.schedule_recycle, parent, false)
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
        var inputData: String? = null
        init {
//            itemView.buttonDelete.setOnClickListener {
//                helper?.deleteMemo(mMemo!!)
//                listData.remove(mMemo)
//                notifyDataSetChanged()
//            }
        }
        fun setData(contents: String?) {
            var date = contents?.substring(10,contents.indexOf("&"))
            itemView.date.text = date.toString().subSequence(0, 4).toString()+"."+date.toString().subSequence(4, 6).toString()+"."+date.toString().subSequence(6, 8).toString()
            itemView.schedule.text = contents?.substring(contents.indexOf("&")+1,contents.length-1)
            this.inputData =contents
        }
    }
}
