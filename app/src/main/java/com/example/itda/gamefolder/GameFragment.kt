package com.example.itda.gamefolder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.itda.R
import com.example.itda.boardfolder.BoardDTO
import com.example.itda.databinding.FragmentGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment() {
    interface Communicator {
        fun passData(score:String)
    }
    private lateinit var communicator: Communicator
    private var questions= mutableListOf<QuizDTO>()
    lateinit var currentQuestion: QuizDTO
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    val numQuestions = 10
    var firestore : FirebaseFirestore?=null
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_game,container,false)
        return view
    }


    fun pushData(score:String){
        communicator = activity as Communicator
        communicator.passData(score)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

            var score = 0
            auth= Firebase.auth
            var dat: MutableList<QuizDTO> = mutableListOf()
            firestore = FirebaseFirestore.getInstance()
            firestore?.collection("quiz")?.orderBy("date", Query.Direction.DESCENDING)
                ?.get()?.addOnSuccessListener { result ->
                    Log.d("asdf", "진입은 성공")
                    for (document in result) {
                        Log.d("asdf", "${document.id} => ${document.data}")
                        //convert document data easily through data class
                        var oneData = document.toObject(QuizDTO::class.java)
                        dat.add(oneData)
                    }
                    questions = dat
                    questions.shuffle()
                    questionIndex = 0
                    currentQuestion = questions[questionIndex]
                    // randomize the answers into a copy of the array
                    answers = currentQuestion.answers!!.toMutableList()
                    // and shuffle them
                    answers.shuffle()
                    questionText.text=currentQuestion.text
                    firstAnswerRadioButton.text=answers[0]
                    secondAnswerRadioButton.text=answers[1]
                    thirdAnswerRadioButton.text=answers[2]
                    fourthAnswerRadioButton.text=answers[3]
                }
                    submitButton.setOnClickListener {
                        currentQuestion = questions[questionIndex]
                        val checkedId = questionRadioGroup.checkedRadioButtonId
                        // Do nothing if nothing is checked (id == -1)
                        if (-1 != checkedId) {
                            var answerIndex = 0
                            when (checkedId) {
                                R.id.secondAnswerRadioButton -> answerIndex = 1
                                R.id.thirdAnswerRadioButton -> answerIndex = 2
                                R.id.fourthAnswerRadioButton -> answerIndex = 3
                            }
                            // The first answer in the original question is always the correct one, so if our
                            // answer matches, we have the correct answer.
                            if (answers[answerIndex] == currentQuestion.answers!![0]) {
                                score += 10
                                questionIndex++
                            } else {
                                questionIndex++
                            }
                            // Advance to the next question
                            if (questionIndex < numQuestions) {
                                currentQuestion = questions[questionIndex]
                                // randomize the answers into a copy of the array
                                answers = currentQuestion.answers!!.toMutableList()
                                // and shuffle them
                                answers.shuffle()
                                questionText.text=currentQuestion.text
                                firstAnswerRadioButton.text=answers[0]
                                secondAnswerRadioButton.text=answers[1]
                                thirdAnswerRadioButton.text=answers[2]
                                fourthAnswerRadioButton.text=answers[3]

                            } else {
                                pushData(score.toString())

                            }
                        }
                    }

        }
    }
