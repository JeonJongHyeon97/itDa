package com.example.itda

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileInputStream
import java.io.FileOutputStream


class Calendar_example : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var fname: String? = null
        var str: String? = null
        var calendarView: CalendarView? = null
        var cha_Btn: Button? = null
        var del_Btn: Button? = null
        var save_Btn: Button? = null
        var diaryTextView: TextView? = null
        var textView2: TextView? = null
        var textView3: TextView? = null
        var contextEditText: EditText? = null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView = findViewById(R.id.calendarView)
        diaryTextView = findViewById(R.id.diaryTextView)
        save_Btn = findViewById(R.id.save_Btn)
        del_Btn = findViewById(R.id.del_Btn)
        cha_Btn = findViewById(R.id.cha_Btn)
        textView2 = findViewById(R.id.textView2)
        textView3 = findViewById(R.id.textView3)
        contextEditText = findViewById(R.id.contextEditText)
        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
        val intent = intent
        val name = intent.getStringExtra("userName")
        val userID = intent.getStringExtra("userID")
            calendarView.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            diaryTextView.visibility=View.VISIBLE
            save_Btn.setVisibility(View.VISIBLE)
            contextEditText.setVisibility(View.VISIBLE)
            textView2.setVisibility(View.INVISIBLE)
            cha_Btn.setVisibility(View.INVISIBLE)
            del_Btn.setVisibility(View.INVISIBLE)
            diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth))
            contextEditText.setText("")
            checkDay(year, month, dayOfMonth, userID)
        })
        save_Btn.setOnClickListener(View.OnClickListener {
            saveDiary(fname)
            str = contextEditText.getText().toString()
            textView2.setText(str)
            save_Btn.setVisibility(View.INVISIBLE)
            cha_Btn.setVisibility(View.VISIBLE)
            del_Btn.setVisibility(View.VISIBLE)
            contextEditText.setVisibility(View.INVISIBLE)
            textView2.setVisibility(View.VISIBLE)
        })
    }

    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String?) {
        var fname: String? = null
        var str: String? = null
        var calendarView: CalendarView? = null
        var cha_Btn: Button? = null
        var del_Btn: Button? = null
        var save_Btn: Button? = null
        var diaryTextView: TextView? = null
        var textView2: TextView? = null
        var textView3: TextView? = null
        var contextEditText: EditText? = null
        fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt" //저장할 파일 이름설정
        var fis: FileInputStream? = null //FileStream fis 변수
        try {
            fis = openFileInput(fname)
            val fileData = ByteArray(fis.available())
            fis.read(fileData)
            fis.close()
            str = String(fileData)
            contextEditText!!.visibility = View.INVISIBLE
            textView2!!.visibility = View.VISIBLE
            textView2!!.text = str
            save_Btn!!.visibility = View.INVISIBLE
            cha_Btn!!.visibility = View.VISIBLE
            del_Btn!!.visibility = View.VISIBLE
            cha_Btn!!.setOnClickListener {
                contextEditText!!.visibility = View.VISIBLE
                textView2!!.visibility = View.INVISIBLE
                contextEditText!!.setText(str)
                save_Btn!!.visibility = View.VISIBLE
                cha_Btn!!.visibility = View.INVISIBLE
                del_Btn!!.visibility = View.INVISIBLE
                textView2!!.text = contextEditText!!.text
            }
            del_Btn!!.setOnClickListener {
                textView2!!.visibility = View.INVISIBLE
                contextEditText!!.setText("")
                contextEditText!!.visibility = View.VISIBLE
                save_Btn!!.visibility = View.VISIBLE
                cha_Btn!!.visibility = View.INVISIBLE
                del_Btn!!.visibility = View.INVISIBLE
                removeDiary(fname)
            }
            if (textView2!!.text == null) {
                textView2!!.visibility = View.INVISIBLE
                diaryTextView!!.visibility = View.VISIBLE
                save_Btn!!.visibility = View.VISIBLE
                cha_Btn!!.visibility = View.INVISIBLE
                del_Btn!!.visibility = View.INVISIBLE
                contextEditText!!.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    fun removeDiary(readDay: String?) {
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = ""
            fos.write(content.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?) {
        var fname: String? = null
        var str: String? = null
        var calendarView: CalendarView? = null
        var cha_Btn: Button? = null
        var del_Btn: Button? = null
        var save_Btn: Button? = null
        var diaryTextView: TextView? = null
        var textView2: TextView? = null
        var textView3: TextView? = null
        var contextEditText: EditText? = null
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = contextEditText!!.text.toString()
            fos.write(content.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}