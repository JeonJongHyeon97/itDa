package com.example.itda

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.board_recycle.view.*
import java.util.ArrayList

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
            itemView.detail_post.setOnClickListener {
                val intent = Intent(itemView.context,BoardDetail::class.java)
                intent.putExtra("BoardPage", listData.get(adapterPosition).boardName.toString())
                intent.putExtra("Title", listData.get(adapterPosition).title.toString())
                intent.putExtra("Date", listData.get(adapterPosition).date.toString())
                intent.putExtra("Email", listData.get(adapterPosition).email.toString())
                intent.putExtra("Like", listData.get(adapterPosition).like.toString())
                if (listData.get(adapterPosition).replies?.toList().isNullOrEmpty()){
                    intent.putExtra("Reply", charArrayOf())
                }else{
                    intent.putStringArrayListExtra("Reply", listData.get(adapterPosition).replies?.toList() as ArrayList<String?>?)
                }

                intent.putExtra("Text", listData.get(adapterPosition).text.toString())

                Log.d("QQ1","Reply : ${listData.get(adapterPosition).replies?.toMutableList()}")
                itemView.context.startActivity(intent)
            }
        }
        //BoardDTO(var boardName:String?=null, var date:Long?=null, var email:String?=null, var like:Long?=null,
        // var reply:Map<String,String>?=null, var text:String?=null, var title:String?=null)

        fun setData(data: BoardDTO) {
            itemView.reply_title.text = data.title
            itemView.board_email.text = data.email!!.substring(0,data.email!!.indexOf("@"))
            itemView.reply_text.text = data.text
            itemView.board_date.text = data.date.toString().subSequence(2,4).toString()+"."+data.date.toString().subSequence(4,6).toString()+"."+data.date.toString().subSequence(6,8).toString()
            itemView.reply_likes.text = data.like.toString()+" likes"
            itemView.board_replies.text = if(data.replies?.toMutableList().isNullOrEmpty()){
                "0 replies"}else{
                data.replies?.size.toString()+" replies"
            }
            this.inputData =data
        }
    }
}