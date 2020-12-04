package com.example.itda.gamefolder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.itda.MyApplication
import com.example.itda.R
import com.example.itda.boardfolder.BoardDTO
import com.example.itda.databinding.FragmentGameWonBinding
import com.example.itda.gamefolder.GameFragment.Companion.score
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class GameWonFragment : Fragment() {
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    var scoreDTO = ScoreDTO()
    var Useremail = MyApplication.prefs.getString("email", "aaaaaa@naver.com")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGameWonBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game_won, container, false)
        binding.score.text= score.toString()
        binding.exitBtn.setOnClickListener { view: View ->
            activity?.finish()
        }

        auth= Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        var time =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis()))
                .toLong()
        scoreDTO= ScoreDTO(time, Useremail,score.toLong())
        firestore!!.collection("score").document(time.toString()).set(scoreDTO)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firestore?.collection("score")?.orderBy("score", Query.Direction.DESCENDING)
                        ?.get()?.addOnSuccessListener { result ->
                            var rank = 1
                            for (document in result) {
                                Log.d("asdf", "${document.id} => ${document.data}")
                                var oneData = document.toObject(ScoreDTO::class.java)
                                println(oneData)
                                Log.d("score", "${oneData.userEmail} => ${oneData.score}")
                                if((oneData.userEmail==Useremail)&&(oneData.score== score.toLong()))
                                    break
                                rank++
                            }
                            binding.rank.text="Ranking No."+rank.toString()
                        }
                }
                score = 0
            }


        val args = GameWonFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "NumCorrect: ${args.numCorrect}, NumQuestions: ${args.numQuestions}", Toast.LENGTH_LONG).show()

        setHasOptionsMenu(true)
        return binding.root
    }

}
