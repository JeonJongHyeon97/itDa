package com.example.itda.familydiaryfolder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.itda.MyApplication
import com.example.itda.R
import com.example.itda.homefolder.UserDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.add_images.*

import java.text.SimpleDateFormat
import java.util.*

class AddImagePage : AppCompatActivity() {
    var Useremail = MyApplication.prefs.getString("email", "no email")
    val PICK_IMAGE_FROM_ALBUM = 0

    var photoUri: Uri? = null

    var storage: FirebaseStorage? = null
    var firestore: FirebaseFirestore? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_images)
        println("user email : $Useremail")
//        var downloadKey :String? = "null"
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        addphoto_image.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }

        addphoto_btn_upload.setOnClickListener {
            contentUpload()
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            //이미지 선택시
            if(resultCode == Activity.RESULT_OK){
                //이미지뷰에 이미지 세팅
                println(data?.data)
                photoUri = data?.data
                addphoto_image.setImageURI(data?.data)
            }

            else{
                finish()
            }

        }
    }

        fun contentUpload(){
        progress_bar.visibility = View.VISIBLE

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_.png"
        val storageRef = storage?.reference?.child("images")?.child(imageFileName)
            if (storageRef != null) {
                storageRef.putFile(photoUri!!).continueWithTask { task->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    storageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    val uri = task.result
                    var downloadKey: String? = null
                    firestore!!.collection("accounts").whereEqualTo("email", Useremail).get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                var data = document.toObject(UserDTO::class.java)
                                downloadKey = data.family.toString()
                            }
                            println("downloadKey : $downloadKey")


                            //시간 생성
                            val contentDTO = ContentDTO()
                            var time =
                                SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis()))
                                    .toLong()
                            //이미지 주소
                            contentDTO.imageUrl = uri!!.toString()
                            //유저의 UID
                            contentDTO.uid = downloadKey
                            //게시물의 설명
                            contentDTO.explain = addphoto_edit_explain.text.toString()
                            //유저의 아이디
                            contentDTO.userId = Useremail
                            //게시물 업로드 시간
                            contentDTO.timestamp = time
                            contentDTO.title = edtittext_title.text.toString()
                            //게시물을 데이터를 생성 및 엑티비티 종료
                            firestore?.collection("images")?.document()?.set(contentDTO)

                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                        ?.addOnFailureListener {
                            progress_bar.visibility = View.GONE
                            progress_bar.bringToFront()
                            Toast.makeText(
                                this, "upload fail",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
}