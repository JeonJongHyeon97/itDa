package com.example.itda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.board_recycle.view.*
import kotlinx.android.synthetic.main.neologism_recycle.view.*
import kotlinx.android.synthetic.main.neologism_recycle.view.neologism_name

class BoardRecycleAdapter : RecyclerView.Adapter<BoardRecycleAdapter.Holder>() {
    var listData = mutableListOf<BoardDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_recycle, parent, false)
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
        var inputData:BoardDTO? = null
        init {
//            itemView.buttonDelete.setOnClickListener {
//                helper?.deleteMemo(mMemo!!)
//                listData.remove(mMemo)
//                notifyDataSetChanged()
//            }
        }
        fun setData(data: BoardDTO) {
            itemView.reply_title.text = data.title
            itemView.board_email.text = data.email!!.substring(0,data.email!!.indexOf("@"))
            itemView.reply_text.text = data.text
            itemView.board_date.text = data.date.toString().subSequence(2,4).toString()+"."+data.date.toString().subSequence(4,6).toString()+"."+data.date.toString().subSequence(6,8).toString()
            itemView.reply_likes.text = data.like.toString()+" likes"
            itemView.board_replies.text = if(data.reply.isNullOrEmpty()){
                "0 replies"}else{
                data.reply?.size.toString()+" replies"
            }
            this.inputData =data
        }
    }
}