package com.bignerdranch.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bignerdranch.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel : QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

//      listener for when text view is tapped
        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.trueButton.setOnClickListener{
          checkAnswer(true)
        }

        binding.falseButton.setOnClickListener {
          checkAnswer(false)
        }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        updateQuestion()
    }
//  for map value to persist it must be initialized outside the function
    private val userAnswerTracker = mutableMapOf<Int,Boolean>()
    private var userScore = 0

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        ifAnswered()
        Log.d("UserScore", "UserScore:${userScore} : ${userAnswerTracker.size}")
    }

    private fun checkAnswer(userAnswer: Boolean) {
        var currentQuestion = quizViewModel.currentQuestionText
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        if (userAnswer == correctAnswer) {
            userScore++
        }
         userAnswerTracker[currentQuestion] = userAnswer
        ifAnswered()
//        Log.d("UserAnswerTracker", "The current Question is $currentQuestion the user answer $userAnswer the correct answer is $correctAnswer")
//        Log.d("UserAnswerMap", userAnswerTracker.map { "${it.key} : ${it.value} : ${userAnswerTracker.size}" }.joinToString(", "))
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if(userAnswerTracker.size == 6) {
            Toast.makeText(this, "Total Score: ${(userScore*100) / userAnswerTracker.size}%",
                Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun ifAnswered() {
        val questionTextResId = quizViewModel.currentQuestionText
        if (userAnswerTracker.containsKey(questionTextResId)){
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
        } else {
            binding.trueButton.isEnabled = true
            binding.falseButton.isEnabled = true
        }
    }

}