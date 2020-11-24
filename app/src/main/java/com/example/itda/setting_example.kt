package com.example.itda

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setting.*
import java.io.FileOutputStream


class setting_example: Fragment() {
    var str: String = ""
    var fname: String = ""
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_setting,container,false)
            return view
        }
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            contextEditText.setText("") // EditText에 공백값 넣기
            save_btn.setOnClickListener { // 저장 Button이 클릭되면
//                saveDiary(fname) // saveDiary 메소드 호출
                Toast.makeText(activity, "저장되었습니다.", Toast.LENGTH_SHORT).show() // 토스트 메세지
                str = contextEditText.getText().toString() // str 변수에 edittext내용을 toString
                //형으로 저장
                textView2.text = "${str}" // textView에 str 출력
                save_btn.visibility = View.INVISIBLE
                cha_Btn.visibility = View.VISIBLE
                del_Btn.visibility = View.VISIBLE
                contextEditText.visibility = View.INVISIBLE
                textView2.visibility = View.VISIBLE
            }
        }

        fun checkedDay() {


                contextEditText.visibility = View.INVISIBLE
                textView2.visibility = View.VISIBLE
                textView2.text = "${str}" // textView에 str 출력

                save_btn.visibility = View.INVISIBLE
                cha_Btn.visibility = View.VISIBLE
                del_Btn.visibility = View.VISIBLE

                cha_Btn.setOnClickListener { // 수정 버튼을 누를 시
                    contextEditText.visibility = View.VISIBLE
                    textView2.visibility = View.INVISIBLE
                    contextEditText.setText(str) // editText에 textView에 저장된
                    // 내용을 출력
                    save_btn.visibility = View.VISIBLE
                    cha_Btn.visibility = View.INVISIBLE
                    del_Btn.visibility = View.INVISIBLE
                    textView2.text = "${contextEditText.getText()}"
                }

                del_Btn.setOnClickListener {
                    textView2.visibility = View.INVISIBLE
                    contextEditText.setText("")
                    contextEditText.visibility = View.VISIBLE
                    save_btn.visibility = View.VISIBLE
                    cha_Btn.visibility = View.INVISIBLE
                    del_Btn.visibility = View.INVISIBLE
                    removeDiary(fname)
                    Toast.makeText(activity, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }

                if (textView2.getText() == "") {
                    textView2.visibility = View.INVISIBLE
                    save_btn.visibility = View.VISIBLE
                    cha_Btn.visibility = View.INVISIBLE
                    del_Btn.visibility = View.INVISIBLE
                    contextEditText.visibility = View.VISIBLE
                }
            }
        }

        @SuppressLint("WrongConstant")
//        fun saveDiary(readyDay: String) {
//            var fos: FileOutputStream? = null
//
//            try {
//                var content: String = contextEditText.getText().toString()
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }

//        @SuppressLint("WrongConstant")
        fun removeDiary(readyDay: String) {
            try {
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

