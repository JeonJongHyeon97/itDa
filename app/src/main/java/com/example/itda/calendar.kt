package com.example.itda

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.AndroidRuntimeException
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
import kotlinx.android.synthetic.main.fragment_calendar.cha_Btn
import kotlinx.android.synthetic.main.fragment_setting.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.NullPointerException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*



class calendar : Fragment() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var Useremail = MyApplication.prefs.getString("email", "no email")
    var downloadKey :String? = "null"
    var check = false
    var selectDay = SimpleDateFormat("yyyyMMdd").format(Date(System.currentTimeMillis())).toString()
    var list : Map<String,Any> = mutableMapOf("contents" to "20")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar,container,false)
        var time = SimpleDateFormat("yyyyMMdd").format(Date(System.currentTimeMillis())).toString()
        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        Log.d("check", "Useremail : $Useremail")
        firestore!!.collection("accounts").whereEqualTo("email", Useremail).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var data = document.toObject(UserDTO::class.java)
                    downloadKey = data.family
                    Log.d("check", "downloadKey 세팅 완료 : $downloadKey")
                }
                firestore?.collection("calendar")?.document(downloadKey!!)?.get()?.addOnSuccessListener { document ->
                    Log.d("check", "dada size : ${document.data?.size}")
                    if (document.data?.size == null) {
                        firestore!!.collection("calendar").document(downloadKey!!)
                            .set(mutableMapOf("20" to list))
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("check", "$downloadKey")
                                    Log.d("check", "첫글 작성")
                                }
                            }
                    }

                }
                firestore?.collection("calendar")?.document(downloadKey!!)?.get()?.addOnSuccessListener { document ->
                    Log.d("check", "dada size : ${document.data?.size}")
                    try{
                        list = document.data as Map<String, Any>
                        Log.d("check", "list : $list")
                        Log.d("check", "document : ${document.data}")
                        checkSchedule(time)
                    } catch(e:AndroidRuntimeException ) {
                        firestore!!.collection("calendar").document(downloadKey!!)
                            .set(mutableMapOf("20" to list))
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("check", "$downloadKey")
                                    Log.d("check", "첫글 작성")
                                }
                            }
                    }


                }
            }


        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val calendar = Calendar.getInstance()

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Log.d("check", "$year,${month+1},$dayOfMonth")
            calendar.set(year,month,dayOfMonth)
            var mon = ""
            var day = ""
            if (month < 10) {
                mon= "0"+(month+1).toString()
            }else{mon = (month+1).toString()}
            if (dayOfMonth < 10) {
                day = "0"+dayOfMonth.toString()
            }else{day = dayOfMonth.toString()}
            selectDay = year.toString()+mon+day
            Log.d("check", "$selectDay")
            checkSchedule(selectDay)
        }

        save_Btn_setting.setOnClickListener {
            save(selectDay, contextEditText.text.toString())
        }

        cha_Btn.setOnClickListener {
            modify(selectDay)
        }

        del_Btn.setOnClickListener {
            delete(selectDay)
        }
    }

    fun checkSchedule(date:String){
        var contents = list.get(date).toString()
        Log.d("check", "contents => $contents")
        if (contents.length>4){
            var date=contents.substring(10,contents.indexOf("&"))
            var text = contents.substring(contents.indexOf("&")+1,contents.length-1)
            Log.d("check", "$date, $text")
            textView2?.text=text
            check=true
            buttonSetup(check)
        }else{
            textView2?.text="일정을 기입해주세요."
            check=false
            buttonSetup(check)
        }
    }

    fun save(Day:String, text:String){
        var data = mutableMapOf<String,Any>(
            Day to hashMapOf("contents" to Day+"&"+text)
        )
        Log.d("save","data : $data")
        if (text.isNullOrEmpty()){
            Toast.makeText( getContext(), "Please enter the content", Toast.LENGTH_SHORT).show()
        }else {
            firestore!!.collection("calendar").document(downloadKey!!).update(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(getContext(), "Save complete!!", Toast.LENGTH_SHORT).show()
                    }
                    firestore?.collection("calendar")?.document(downloadKey!!)?.get()
                        ?.addOnSuccessListener { document ->
                            list = document.data as Map<String, Any>
                            checkSchedule(Day)
                            Log.d("che", "$list")
                        }
                }
            contextEditText.text=null
            buttonSetup(check)
        }
    }
    fun modify(Day:String){
        buttonSetup(false)
        contextEditText.setText(textView2.text.toString())


    }
    fun delete(Day:String){
        firestore?.collection("calendar")?.document(downloadKey!!)?.get()
            ?.addOnSuccessListener { document ->
                list = document.data as MutableMap<String, Any>
                (list as MutableMap<String, Any>).remove(Day)
                firestore!!.collection("calendar").document(downloadKey!!).set(list)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(getContext(), "Delete complete!!", Toast.LENGTH_SHORT).show()
                        }

                    }
                checkSchedule(Day)
            }

        contextEditText.text=null
        buttonSetup(check)
    }

    fun buttonSetup(check:Boolean){
        if (check) {
            save_Btn_setting?.visibility = View.INVISIBLE
            contextEditText?.visibility = View.INVISIBLE
            cha_Btn?.visibility = View.VISIBLE
            del_Btn?.visibility = View.VISIBLE
        }else{
            save_Btn_setting?.visibility = View.VISIBLE
            contextEditText?.visibility = View.VISIBLE
            cha_Btn?.visibility = View.INVISIBLE
            del_Btn?.visibility = View.INVISIBLE
        }
    }

}