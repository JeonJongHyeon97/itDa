package com.example.itda.boardfolder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itda.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.content_page.*
import java.text.SimpleDateFormat
import java.util.*


class BoardDetail : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    lateinit var list: Map<String, Any?>
    var boardname = ""
    lateinit var data: MutableList<String?>
    var from:String? = null
    var reply_list :MutableList<String?>? = null
    var useremail = MyApplication.prefs.getString("email", "no email")
    var fcmPush :FcmPush? =null
    var alarmDTO = AlarmDTO()
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_page)
        from = intent.getStringExtra("From")!!.toString()
        val date = intent.getStringExtra("Date")!!.toString()
        val writerUid = intent.getStringExtra("WriterUid")!!.toString()
        val email = intent.getStringExtra("Email")!!.toString()
        val boardName = intent.getStringExtra("BoardPage")!!.toString()
        boardname = boardName
        Log.d("myPost", "$boardName")
        val writeTime = intent.getStringExtra("WriteTime")!!.toString()
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        checkReply(boardName, date, email)
        fcmPush = FcmPush()

        reply_send.setOnClickListener {
            Log.d("reply", "click 실행")
            var time =
                SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis()))
            val text = reply_box.text.toString()
            reply_list?.add(time+"※"+useremail+"◎"+text)
            var input = mutableMapOf<String,MutableList<String?>?>("replies" to reply_list)
            firestore!!.collection(boardName).document(writeTime).update(input as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Write reply complete!!", Toast.LENGTH_SHORT).show()
                        checkReply(boardName, date, email)
                        reply_box.setText(null)
                    }
                }
            firestore!!.collection("totalBoard").document(writeTime).update(input as Map<String, Any>).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Write reply complete!!", Toast.LENGTH_SHORT).show()
                        checkReply(boardName, date, email)
                        reply_box.setText(null)
                    }
                }
            alarmDTO = AlarmDTO(email, useremail,text,boardName,date.toLong(),time.toLong(),writerUid)
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

        fcmPush?.sendMessage(destination, useremail.substring(0,1) + "**@**.com left a comment","")
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
            var size = email.substring(0,email.indexOf("@")).length
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
    override fun onBackPressed(){
        if (from == "Mypost"){
            finish()
        }else{
            val intent = Intent(this, BoardPage::class.java)
            intent.putExtra("BoardPage", boardname)
            startActivity(intent)
            finish()
        }
    }
}