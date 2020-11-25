package com.example.itda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.neologism_recycle.view.*
import kotlinx.android.synthetic.main.post_recycle.view.*

class ReplyRecycleAdapter : RecyclerView.Adapter<ReplyRecycleAdapter.Holder>() {
    var listData = mutableListOf<String?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_recycle, parent, false)
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
        var inputData:String? = null

        //BoardDTO(var boardName:String?=null, var date:Long?=null, var email:String?=null, var like:Long?=null,
        // var reply:Map<String,String>?=null, var text:String?=null, var title:String?=null)

        fun setData(data: String?) {
            if (!data.isNullOrEmpty()) {
                var date_text= data!!.substring(0, data.indexOf("_"))
                var title_text= data!!.substring(data.indexOf("_")+1, data.indexOf("※"))
                var text_text= data!!.substring(data.indexOf("※")+1, data.length)
                itemView.reply_date.text = date_text.substring(0,4)+"."+date_text.substring(4,6)+"."+date_text.substring(6,8)+" "+date_text.substring(8,10)+":"+date_text.substring(10,12)
                itemView.reply_title.text = title_text
                itemView.reply_text.text = text_text

                this.inputData = data
            }
        }
    }
}

