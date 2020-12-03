package com.example.itda

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.itda.boardfolder.SearchBoardDetail
import com.example.itda.neologismfolder.NeologismData
import com.example.itda.neologismfolder.NeologismRecycleAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.alarm_recycle.view.*
import kotlinx.android.synthetic.main.alarm_recycle.view.reply_text
import kotlinx.android.synthetic.main.board_recycle.view.*
import java.util.ArrayList

class AlarmRecyclerViewAdapter : RecyclerView.Adapter<AlarmRecyclerViewAdapter.Holder>() {
    var listData = mutableListOf<AlarmDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alarm_recycle, parent, false)
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
        var inputData: AlarmDTO? = null
        init {
            itemView.alarm_box.setOnClickListener {
                val intent = Intent(itemView.context, SearchBoardDetail::class.java)
                intent.putExtra("BoardPage", listData.get(adapterPosition).boardName.toString())
                intent.putExtra("WriterUid", listData.get(adapterPosition).writerUid.toString())
                intent.putExtra("WriteTime", listData.get(adapterPosition).postDate.toString())
                intent.putExtra("Date", listData.get(adapterPosition).postDate.toString())
                intent.putExtra("Email", listData.get(adapterPosition).destinationEmail.toString())
                itemView.context.startActivity(intent)
            }
        }
        fun setData(data: AlarmDTO) {
            itemView.reply_email.text = data.userEmail!!.substring(0,1)+"**"+"  left a comment on your post"
            itemView.reply_board.text = "Ask a "+data.boardName
            itemView.reply_text.text = data.message
            itemView.reply_date.text = data.replyDate.toString().subSequence(2, 4).toString()+"."+data.replyDate.toString().subSequence(
                4,
                6
            ).toString()+"."+data.replyDate.toString().subSequence(6, 8).toString()
            this.inputData =data
        }
    }



}