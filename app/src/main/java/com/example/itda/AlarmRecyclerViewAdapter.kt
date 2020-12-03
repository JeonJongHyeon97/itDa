package com.example.itda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.itda.neologismfolder.NeologismData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.neologism_recycle.view.*
import java.util.ArrayList

class AlarmRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listData = mutableListOf<NeologismData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alarm_recycle, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val commentTextView = holder.itemView.commentviewitem_textview_profile

        FirebaseFirestore.getInstance().collection("profileImages")
            .document(alarmDTOList[position].uid!!).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val url = task.result["image"]
                    Glide.with(activity)
                        .load(url)
                        .apply(RequestOptions().circleCrop())
                        .into(profileImage)
                }
            }

        when (alarmDTOList[position].kind) {
            0 -> {
                val str_0 =
                    alarmDTOList[position].userId + getString(R.string.alarm_favorite)
                commentTextView.text = str_0
            }

            1 -> {
                val str_1 =
                    alarmDTOList[position].userId + getString(R.string.alarm_who) + alarmDTOList[position].message + getString(
                        R.string.alarm_comment
                    )
                commentTextView.text = str_1
            }

            2 -> {
                val str_2 = alarmDTOList[position].userId + getString(R.string.alarm_follow)
                commentTextView.text = str_2
            }
        }
    }

    override fun getItemCount(): Int {

        return listData.size
    }
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var inputData: NeologismData? = null
        init {

        }
        fun setData(data: NeologismData) {
            itemView.neologism_name.text = data.name
            itemView.neologism_definition.text = data.definition
            this.inputData =data
        }
    }



}