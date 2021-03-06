package com.example.itda.familydiaryfolder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.itda.MyApplication
import com.example.itda.R
import com.example.itda.homefolder.UserDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.bumptech.glide.Glide
import com.squareup.okhttp.OkHttpClient
import kotlinx.android.synthetic.main.family_board_recycle.view.*
import kotlinx.android.synthetic.main.fragment_familyboard.*
import kotlinx.android.synthetic.main.fragment_familyboard.view.*
import java.util.*




class FamilyboardFragment : Fragment() {
    var Useremail = MyApplication.prefs.getString("email", "no email")
    var downloadKey :String? = "null"
    var user: FirebaseUser? = null
    var firestore: FirebaseFirestore? = null
    var imagesSnapshot: ListenerRegistration? = null
    var okHttpClient: OkHttpClient? = null
    //    var fcmPush: FcmPush? = null
    var mainView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("Family","$Useremail")
        user = FirebaseAuth.getInstance().currentUser
        firestore = FirebaseFirestore.getInstance()
        okHttpClient = OkHttpClient()
//        fcmPush = FcmPush()
        //리사이클러 뷰와 어뎁터랑 연결
        mainView = inflater.inflate(R.layout.fragment_familyboard, container, false)


        return mainView
    }

    override fun onResume() {
        super.onResume()
        mainView?.detailviewfragment_recyclerview?.layoutManager = LinearLayoutManager(activity)
        mainView?.detailviewfragment_recyclerview?.adapter = DetailRecyclerViewAdapter()
//        var mainActivity = activity as MainActivity
//        mainActivity.progress_bar.visibility = View.INVISIBLE

    }

    override fun onStop() {
        super.onStop()
        imagesSnapshot?.remove()
    }

    inner class DetailRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val contentDTOs: ArrayList<ContentDTO>
        val contentUidList: ArrayList<String>

        init {
            contentDTOs = ArrayList()
            contentUidList = ArrayList()
            getCotents()
        }

        fun getCotents() {
            firestore!!.collection("accounts").whereEqualTo("email", Useremail).get()
                .addOnSuccessListener { documents ->
                    //load the family key
                    for (document in documents) {
                        var data = document.toObject(UserDTO::class.java)
                        downloadKey = data.family
                        Log.d("Family", "처음 downloadKey 설정 : $downloadKey")
                    } // load the image download url
                    imagesSnapshot = firestore?.collection("images")
                        ?.orderBy("timestamp", Query.Direction.DESCENDING)
                        ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            contentDTOs.clear()
                            contentUidList.clear()
                            if (querySnapshot == null) return@addSnapshotListener
                            for (snapshot in querySnapshot!!.documents) {
                                var item = snapshot.toObject(ContentDTO::class.java)!!
                                println(item.uid)
                                Log.d("Family", "downloadKey 설정 : ${item.uid},$downloadKey")
                                if (downloadKey == item.uid) { // only load data which has same family key
                                    Log.d("upload image", "성공")
                                    contentDTOs.add(item)
                                    contentUidList.add(snapshot.id)
                                }
                            }
                            notifyDataSetChanged()
                        }
                }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.family_board_recycle, parent, false)
            return CustomViewHolder(view)

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val viewHolder = (holder as CustomViewHolder).itemView
            // Setting user ID
            viewHolder.detailviewitem_profile_textview.text = contentDTOs[position].userId
            // Setting the date
            var timeStamp = contentDTOs[position].timestamp.toString()
            viewHolder.time.text = timeStamp.substring(0,4)+"."+timeStamp.substring(4,6)+"."+timeStamp.substring(6,8)+" "+timeStamp.substring(8,10)+":"+timeStamp.substring(10,12)
            // download the image with downloadUrl
            Log.d("image","${contentDTOs[position]}")
            Glide.with(holder.itemView.context)
                .load(contentDTOs[position].imageUrl)
                .into(viewHolder.detailviewitem_imageview_content)
            //text
            viewHolder.detailviewitem_explain_textview.text = contentDTOs[position].explain
            viewHolder.detailviewitem_title_textview.text = contentDTOs[position].title
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        write_btn.setOnClickListener {
            activity?.let {
                val intent = Intent(context, AddImagePage::class.java)
                startActivity(intent)
            }
        }



    }
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
