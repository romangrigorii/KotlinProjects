package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ContextUtils.getActivity
import org.w3c.dom.Text

class QuizQuestions : AppCompatActivity(), View.OnClickListener {

    // this one click listener will listen to any clicks being made on the screen
    private var mUserName : String? = null
    private var mCorrectAnswers : Int = 0
    private var mCurrentPosition : Int = 1 // this defined how far along in the app I am
    private var mQuestionsList : ArrayList<Question>? = null // List of questions I will ask
    private var mSelectedOptionPosition : Int = 0 // currently selected option. This is 0 by default.

    private var progressBar : ProgressBar? = null
    private var tvProgress : TextView? = null
    private var tvQuestion : TextView? = null
    private var tvImage : ImageView? = null

    private var tvOptionOne : TextView? = null
    private var tvOptionTwo : TextView? = null
    private var tvOptionThree : TextView? = null
    private var tvOptionFour : TextView? = null
    private var btnSubmit : Button? = null
    private var clicked : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) { // this processes startup logic
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)
        // declaration
        mUserName = intent.getStringExtra(Constants.USER_NAME)
        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.progressVal)
        tvQuestion = findViewById(R.id.tv_question)
        tvImage = findViewById(R.id.tv_image)

        tvOptionOne = findViewById(R.id.tv_option_one)
        tvOptionTwo = findViewById(R.id.tv_option_two)
        tvOptionThree = findViewById(R.id.tv_option_three)
        tvOptionFour = findViewById(R.id.tv_option_four)
        btnSubmit = findViewById(R.id.answersubmit)

        tvOptionOne?.setOnClickListener(this) // this refers to the class, thus OnClick() method is tied to every option.
        // This is useful so we don't have to rewrite the same code for every button.
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

        mQuestionsList = Constants.getQuestions(10) // this will simply populate the list with questions
        setQuestion() // will setup the options with the questions. setQuestion() will be rerun when every question is answered

    }

    private fun setQuestion() {
        defaultOptionsView() // reset the view of the available options
        clicked = false
        val question: Question = mQuestionsList!![mCurrentPosition - 1] // !! will throw an exception if the list is null
        progressBar?.progress = mCurrentPosition
        tvProgress?.text = "$mCurrentPosition/${progressBar?.max}"
        tvQuestion?.text = question.question
        tvOptionOne?.text = question.optionOne
        tvOptionTwo?.text = question.optionTwo
        tvOptionThree?.text = question.optionThree
        tvOptionFour?.text = question.optionFour
        tvImage?.setImageResource(question.image)
        if (mCurrentPosition == mQuestionsList!!.size){
            btnSubmit?.text = "FINISH"
        } else {
            btnSubmit?.text = "SUBMIT"
        }
    }

    private fun defaultOptionsView(){ // reset all the options

        val options = ArrayList<TextView>() // initialized ArrayList
        // the list is a simple way to iterate through element and apply the same code
        tvOptionOne?.let{
            options.add(0, it)
        }
        tvOptionTwo?.let{
            options.add(1, it)
        }
        tvOptionThree?.let{
            options.add(2, it)
        }
        tvOptionFour?.let{
            options.add(3, it)
        }

        for (option in options){ // this defaults all the options
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    private fun selectionOptionView(tv: TextView, selectedOptionNum: Int){
        defaultOptionsView() // whenever an option is selected we deselect all others
        mSelectedOptionPosition = selectedOptionNum // a new selection has been made in the range 1..4
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    override fun onClick(view: View?) {
        if (!clicked) {
            when (view?.id) {
                R.id.tv_option_one -> tvOptionOne?.let { selectionOptionView(it, 1) }
                R.id.tv_option_two -> tvOptionTwo?.let { selectionOptionView(it, 2) }
                R.id.tv_option_three -> tvOptionThree?.let { selectionOptionView(it, 3) }
                R.id.tv_option_four -> tvOptionFour?.let { selectionOptionView(it, 4) }
            }
        }
        if (view?.id == R.id.answersubmit ){
            processSubmit()
        }
    }

    private fun processSubmit() {
        if (mSelectedOptionPosition == 0) { // if we press submit but no option is selected
            mCurrentPosition++
            when {
                mCurrentPosition <= mQuestionsList!!.size -> {
                    setQuestion() // load up the next question
                }
                else -> {
                    val intent = Intent(this, QuizEnded::class.java) // moves from one activity to another
                    intent.putExtra(Constants.USER_NAME, mUserName)
                    intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                    intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
                    startActivity(intent) // starts up the activity of the intent
                    finish() // will close the main activity so you cannot got back to it
                }
            }
        } else {
            val question = mQuestionsList?.get(mCurrentPosition - 1)
            if (question!!.correctAnswer != mSelectedOptionPosition){
                answerView(mSelectedOptionPosition, R.drawable.incorrect_option)
            } else {
                mCorrectAnswers++
            }
            answerView(question.correctAnswer, R.drawable.correct_option)
            if (mCurrentPosition >= mQuestionsList!!.size){
                btnSubmit?.text = "FINISH"
            } else {
                btnSubmit?.text = "GO TO NEXT QUESTION"
                clicked = true
     }
            mSelectedOptionPosition = 0 // resets the option
        }
    }

    private fun answerView(answer: Int, drawableView: Int){
        when (answer){
            1 -> tvOptionOne?.background = ContextCompat.getDrawable(
                this,
                drawableView
            )
            2 -> tvOptionTwo?.background = ContextCompat.getDrawable(
                this,
                drawableView
            )
            3 -> tvOptionThree?.background = ContextCompat.getDrawable(
                this,
                drawableView
            )
            4 -> tvOptionFour?.background = ContextCompat.getDrawable(
                this,
                drawableView
            )

        }
    }
}