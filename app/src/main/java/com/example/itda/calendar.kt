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
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("calendar")?.document(host)?.get()?.addOnSuccessListener { document ->
            list = document.data as Map<String, Any>
        }
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val calendar = Calendar.getInstance()
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Log.d("check", "$year,${month+1},$dayOfMonth")
            calendar.set(year,month,dayOfMonth)
            var contents = list.get(year.toString()+(month+1).toString()+dayOfMonth.toString()).toString()
            Log.d("check", "contents => $contents")
            if (contents.length>4){
            var date=contents.substring(10,contents.indexOf("&"))
            var text = contents.substring(contents.indexOf("&")+1,contents.length-1)
            Log.d("check", "$date, $text")
            textView2.text=text
            }else{
                textView2.text=""
            }
//                textView2.text=list[0]+"\n"+list[1]

//                    var oneData = document.toObject(NeologismData::class.java)
//                    println(oneData)
//                    Log.d("firebase", "for문 돌아가는중")
//                    if (oneData.name!!.contains(keyword.toString())) dat.add(oneData)
//                Log.d("firebase", "for문 끝")
//                data = dat
//                if (data.isNullOrEmpty()) {
//                    Toast.makeText(this, "Result is empty", Toast.LENGTH_SHORT).show()
//                } else {
//                    adapter?.listData = data
//                    neologism_recycle?.adapter = adapter
//                    neologism_recycle?.layoutManager = LinearLayoutManager(this)
//                }
//
//
//            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
//
//            contextEditText.setText("") // EditText에 공백값 넣기
//
//            checkedDay(year, month, dayOfMonth) // checkedDay 메소드 호출
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

    fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {
        fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"
    // 저장할 파일 이름 설정. Ex) 2019-01-20.txt
        var fis: FileInputStream? = null // FileStream fis 변수 설정

        try {
//            fis = openFileInput(fname) // fname 파일 오픈!!

//            val fileData = ByteArray(fis.available()) // fileData에 파이트 형식
//    //으로 저장
//            fis.read(fileData) // fileData를 읽음
//            fis.close()

//            str = String(fileData) // str 변수에 fileData를 저장

            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
            textView2.text = "${str}" // textView에 str 출력

            save_Btn_setting.visibility = View.INVISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE

            cha_Btn.setOnClickListener { // 수정 버튼을 누를 시
                contextEditText.visibility = View.VISIBLE
                textView2.visibility = View.INVISIBLE
                contextEditText.setText(str) // editText에 textView에 저장된
    // 내용을 출력
                save_Btn_setting.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                textView2.text = "${contextEditText.getText()}"
            }

            del_Btn.setOnClickListener {
                textView2.visibility = View.INVISIBLE
                contextEditText.setText("")
                contextEditText.visibility = View.VISIBLE
                save_Btn_setting.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                removeDiary(fname)
                Toast.makeText(activity, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }

            if (textView2.getText() == "") {
                textView2.visibility = View.INVISIBLE
//                diaryTextView.visibility = View.VISIBLE
                save_Btn_setting.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                contextEditText.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            e.printStackTrace()
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