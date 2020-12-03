package com.example.itda.homefolder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.itda.MainActivity
import com.example.itda.MyApplication
import com.example.itda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_setting.*


class setting: Fragment() {
    var firestore: FirebaseFirestore? = null
    private lateinit var auth: FirebaseAuth
    var Useremail = MyApplication.prefs.getString("email", "aaaaaa@naver.com")
    var downloadKey:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        return view

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        login_email.text = Useremail

        firestore!!.collection("accounts").whereEqualTo("Email", Useremail).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var data = document.toObject(UserDTO::class.java)
                    downloadKey=data.family.toString()
                    Log.d("Family", "$downloadKey")
                    entered_familykey.text = downloadKey

                }
            }

        logout_btn.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "Log out complete !", Toast.LENGTH_SHORT).show()
            activity?.let {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        cha_Btn.setOnClickListener {
            family_key.setText(downloadKey)
            entered_familykey.visibility=View.INVISIBLE
            family_key.visibility=View.VISIBLE
        }

        save_btn.setOnClickListener {
            var newKey = family_key.text.toString()
            var input = mutableMapOf<String,String?>("family" to newKey)
            firestore!!.collection("accounts").document(Useremail).update(input as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Family Key is Changed !", Toast.LENGTH_SHORT).show()
                        downloadKey=newKey
                        entered_familykey.text=newKey
                        entered_familykey.visibility=View.VISIBLE
                        family_key.visibility=View.INVISIBLE
                    }
                }
        }
    }
}