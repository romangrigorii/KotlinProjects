package com.example.quizapp

object Constants {

    const val USER_NAME : String = "user_name"
    const val TOTAL_QUESTIONS : String = "total_questions"
    const val CORRECT_ANSWERS : String = "correct_answers"

    fun getQuestions(): ArrayList<Question>{
        val questionsList = ArrayList<Question>()
        val que1 = Question(
            id = 1, question = "What country does this flag belong to?",
            R.drawable.ar,
            "Argentina", "Australia", "Armenia", "Austria",
            1
        )
        val que2 = Question(
            id = 2, question = "What country does this flag belong to?",
            R.drawable.md,
            "Denmark", "Ukraine", "Mexico", "Moldova",
            4
        )
        val que3 = Question(
            id = 3, question = "What country does this flag belong to?",
            R.drawable.us,
            "Canada", "USA", "Sudan", "Italy",
            2
        )
        val que4 = Question(
            id = 4, question = "What country does this flag belong to?",
            R.drawable.sa,
            "England", "Wales", "Svalbard", "Scotland",
            3
        )
        val que5 = Question(
            id = 5, question = "What country does this flag belong to?",
            R.drawable.me,
            "Russia", "Austria", "Armenia", "Montenegro",
            4
        )
        val que6 = Question(
            id = 6, question = "What country does this flag belong to?",
            R.drawable.mx,
            "Mexico", "Italy", "Venezuela", "Romania",
            1
        )
        val que7 = Question(
            id = 7, question = "What country does this flag belong to?",
            R.drawable.ru,
            "France", "Russia", "Croatia", "Brazil",
            2
        )
        val que8 = Question(
            id = 8, question = "What country does this flag belong to?",
            R.drawable.bz,
            "Belize", "Greece", "Colombia", "Spain",
            1
        )
        val que9 = Question(
            id = 9, question = "What country does this flag belong to?",
            R.drawable.ba,
            "Nepal", "Bulgaria", "Moldova", "Bosnia",
            1
        )
        val que10 = Question(
            id = 10, question = "What country does this flag belong to?",
            R.drawable.ch,
            "Lithuania", "Netherlands", "Switzerland", "Germany",
            3
        )

        questionsList.add(que1)
        questionsList.add(que2)
        questionsList.add(que3)
        questionsList.add(que4)
        questionsList.add(que5)
        questionsList.add(que6)
        questionsList.add(que7)
        questionsList.add(que8)
        questionsList.add(que9)
        questionsList.add(que10)

        return questionsList
    }
}