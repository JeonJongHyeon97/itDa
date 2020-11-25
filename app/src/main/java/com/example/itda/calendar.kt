package com.example.itda

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*



class calendar : Fragment() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var calendarDTO =CalendarDTO()
    var Useremail = MyApplication.prefs.getString("email", "no email")
    val host = "test@naver.com"
    var fname: String = ""
    var str: String = ""
    lateinit var list : Map<String,Any>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar,container,false)
        var time = SimpleDateFormat("yyyyMMdd").format(Date(System.currentTimeMillis())).toString()
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("calendar")?.document(host)?.get()?.addOnSuccessListener { document ->
            list = document.data as Map<String, Any>
            checkSchedule(time)
        }
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val calendar = Calendar.getInstance()
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Log.d("check", "$year,${month+1},$dayOfMonth")
            calendar.set(year,month,dayOfMonth)
            checkSchedule(year.toString()+(month+1).toString()+dayOfMonth.toString())
        }

        save_Btn_setting.setOnClickListener { // 저장 Button이 클릭되면
            saveDiary(fname) // saveDiary 메소드 호출
            Toast.makeText(activity, "저장되었습니다.", Toast.LENGTH_SHORT).show() // 토스트 메세지
            str = contextEditText.getText().toString() // str 변수에 edittext내용을 toString
    //형으로 저장
            textView2.text = "${str}" // textView에 str 출력
            save_Btn_setting.visibility = View.INVISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE
            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
        }
    }
    fun checkSchedule(date:String){
        var contents = list.get(date).toString()
        Log.d("check", "contents => $contents")
        if (contents.length>4){
            var date=contents.substring(10,contents.indexOf("&"))
            var text = contents.substring(contents.indexOf("&")+1,contents.length-1)
            Log.d("check", "$date, $text")
            textView2.text=text
        }else{
            textView2.text="일정을 기입해주세요."
        }
    }

    @SuppressLint("WrongConstant")
    fun saveDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
//            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = contextEditText.getText().toString()
//            fos.write(content.toByteArray())
//            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    fun removeDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
//            fos = openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ""
//            fos.write(content.toByteArray())
//            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}