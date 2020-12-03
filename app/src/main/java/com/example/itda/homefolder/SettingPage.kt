package com.example.itda.homefolder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.itda.MainActivity
import com.example.itda.MyApplication
import com.example.itda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingPage: AppCompatActivity() {
    var firestore: FirebaseFirestore? = null
    private lateinit var auth: FirebaseAuth
    var Useremail = MyApplication.prefs.getString("email", "aaaaaa@naver.com")
    var downloadKey:String? = null
    // Firebase Authentication 관리 클래스
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_setting)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        login_email.text = Useremail
        Log.d("Family", "진입1")
        entered_familykey.text = "downloadKey"
        firestore!!.collection("accounts").whereEqualTo("email", Useremail).get()
            .addOnSuccessListener { documents ->
                var name_list = mutableListOf<String?>()
                Log.d("Family", "진입2")
                for (document in documents) {
                    var data = document.toObject(UserDTO::class.java)
                    downloadKey=data.family.toString()
                    name_list.add(data.family)
                }
                Log.d("Family", "$name_list[0]")
                entered_familykey?.text=name_list[0]
            }


        logout_btn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Log out complete !", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        cha_Btn.setOnClickListener {
            family_key.setText(downloadKey)
            entered_familykey.visibility= View.INVISIBLE
            cha_Btn.visibility= View.INVISIBLE
            family_key.visibility= View.VISIBLE
            save_btn.visibility= View.VISIBLE
        }

        save_btn.setOnClickListener {
            var newKey = family_key.text.toString()
            Log.d("Change", "$Useremail")
            Log.d("Change", "$newKey")
            firestore!!.collection("accounts").document(Useremail).update("family",newKey)
                .addOnCompleteListener { task ->
                    Toast.makeText(this, "Family Key is Changed !", Toast.LENGTH_SHORT).show()
                    downloadKey=newKey
                    entered_familykey.text = newKey
                    entered_familykey.visibility= View.VISIBLE
                    family_key.visibility= View.INVISIBLE
                    save_btn.visibility= View.INVISIBLE
                    cha_Btn.visibility= View.VISIBLE
                    }
                }
        }
    }
