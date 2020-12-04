package com.example.itda.gamefolder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.itda.R
import com.example.itda.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(
        val text: String,
        val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
        Question(text = "고구가 100개 먹은듯한 답답어의 줄임말은?",
            answers = listOf("고답", "정답", "유답", "빵답")),
        Question(text = "팬 아니어도 저장하다의 줄임말은?",
            answers = listOf("팬아저", "팔이절여", "폰아져", "팥이조아")),
        Question(text = "얼죽아의 아는 어떤 커피인가요?",
            answers = listOf("아메리카노", "아이스카페라떼", "아이스카푸치노", "아이스바닐라")),
        Question(text = "영끌의 뜻은 무엇인가요?",
            answers = listOf("영혼까지 끌어모은다", "영국가서  끌끌끌", "영감이 끓어오른다", "영치기 끌치기")),
        Question(text = "핑프란 말은 어떤 말을 줄인건가요?",
            answers = listOf("핑거 프린(세)스", "핑거 프랑스", "핑핑이 프랑스", "핑이 프랑스만큼 높네")),
        Question(text = "고답의 고는 어떤 고인가요",
            answers = listOf("고구마", "고기", "고성방가", "고민")),
        Question(text = "엄근진의 근은 무엇인가요?",
            answers = listOf("근엄하다", "근육", "두근", "근손실")),
        Question(text = "꾸안꾸는 무슨 뜻인가요??",
            answers = listOf("꾸민듯안꾸민듯꾸민", "꾸러미안꾸러미", "꾸꾸까까", "꾸미고싶은데안꾸밈")),
        Question(text = "시강은 어떤 뜻인가요?",
            answers = listOf("시선 강탈", "시세 강조", "시사 강조", "시민 강조")),
        Question(text = "자삭의 자는 어떤 말을 의미하나요",
            answers = listOf("<자신>", "<자유>", "<자리>", "<자주>"))
    )




    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this
        var score = 0
        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
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
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    score+=10
                    questionIndex++
                }else{questionIndex++}
                    // Advance to the next question
                if (questionIndex < numQuestions) {
                    currentQuestion = questions[questionIndex]
                    setQuestion()
                    binding.invalidateAll()
                } else {
                    // We've won!  Navigate to the gameWonFragment.
                    view.findNavController()
                        .navigate(
                            GameFragmentDirections
                                .actionGameFragmentToGameWonFragment(numQuestions, questionIndex))

                }

            }
        }
        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }

}