package com.example.itda.boardfolder

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itda.AlarmDTO
import com.example.itda.FcmPush
import com.example.itda.MyApplication
import com.example.itda.R

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.content_page.*
import java.text.SimpleDateFormat
import java.util.*


class SearchBoardDetail : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    lateinit var list: Map<String, Any?>
    var boardname = ""
    lateinit var data: MutableList<String?>
    var alarmDTO = AlarmDTO()
    var fcmPush : FcmPush? =null
    var reply_list :MutableList<String?>? = null
    var useremail = MyApplication.prefs.getString("email", "no email")
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_page)
        val writerUid = intent.getStringExtra("WriterUid")!!.toString()
        val date = intent.getStringExtra("Date")!!.toString()
        val email = intent.getStringExtra("Email")!!.toString()
        val boardNa = intent.getStringExtra("BoardPage")!!.toString()
        boardname = boardNa
        val writeTime = intent.getStringExtra("WriteTime")!!.toString()
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        checkReply(boardname, date, email)
        fcmPush = FcmPush()

        reply_send.setOnClickListener {
            Log.d("reply", "click 실행")
            var time =
                SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis()))
            val text = reply_box.text.toString()
            reply_list?.add(time+"※"+useremail+"◎"+text)
            var input = mutableMapOf<String,MutableList<String?>?>("replies" to reply_list)
            firestore!!.collection(boardNa).document(writeTime).update(input as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Write reply complete!!", Toast.LENGTH_SHORT).show()
                        checkReply(boardNa, date, email)
                        reply_box.setText(null)
                    }
                }
            firestore!!.collection("totalBoard").document(writeTime).update(input as Map<String, Any>).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Write reply complete!!", Toast.LENGTH_SHORT).show()
                    checkReply(boardNa, date, email)
                    reply_box.setText(null)
                }
            }
            alarmDTO = AlarmDTO(email, useremail,text,boardNa,date.toLong(),time.toLong(),writerUid)
            if (email!=useremail) {
                firestore!!.collection("alarm").document(time.toString()).set(alarmDTO)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                        }
                    }
            }
            commentAlarm(writerUid)

        }

    }
    fun commentAlarm(destination: String) {
        Log.d("alarm","함수 진입")

        val alarmDTO = AlarmDTO()
        alarmDTO.destinationEmail = destination
        alarmDTO.userEmail = useremail
//        alarmDTO.message = message
        alarmDTO.replyDate = System.currentTimeMillis()
//
//        var message = useremail + " left a comment"
        fcmPush?.sendMessage(destination, "New comment is added on your post !","")
    }

    @SuppressLint("SetTextI18n")
    fun checkReply(boardName: String, date: String, email: String) {
        val adapter = ReplyRecycleAdapter()
        var dat: MutableList<String?> = mutableListOf()
        firestore?.collection(boardName)?.whereEqualTo("date", date.toLong())
            ?.whereEqualTo("email", email)?.get()?.addOnSuccessListener { result ->
                Log.d("check", "date : ${date}, email : $email")
                Log.d("check", "document : ${result.documents}")
                var oneData = result.toObjects(BoardDTO::class.java).get(0)
                reply_list = oneData.replies
                Log.d("check", "oneData : $oneData")

                var date_text = oneData.date!!.toString()
                var title_text = oneData.title!!.toString()
                var text_text = oneData.text!!.toString()
                var like_text = oneData.like!!.toLong()
                post_date.text = date_text.substring(0, 4) + "." + date_text.substring(
                    4,
                    6
                ) + "." + date_text.substring(6, 8) + " " + date_text.substring(
                    8,
                    10
                ) + ":" + date_text.substring(10, 12)
                post_title.text = title_text
                post_text.text = text_text
                writer_email.text = email.replaceRange(1,email.indexOf("@"),"*")
                //post_like.text = like_text.toString() + " Likes"
                if (!reply_list.isNullOrEmpty()) {
                    for (document in reply_list!!) {
                        dat.add(document)
                    }
                    Log.d("firebase", "for문 끝")
                    data = dat
                    adapter?.listData = data
                    post_recycle_view?.adapter = adapter
                    post_recycle_view?.layoutManager = LinearLayoutManager(this)
                }
            }
    }
//    override fun onBackPressed(){
//        val intent = Intent(this, BoardPage::class.java)
//        intent.putExtra("BoardPage", boardname)
//        startActivity(intent)
//        finish()
//    }

}