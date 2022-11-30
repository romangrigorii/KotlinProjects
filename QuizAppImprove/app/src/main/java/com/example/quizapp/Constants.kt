package com.example.quizapp

object Constants {

    const val USER_NAME : String = "user_name"
    const val TOTAL_QUESTIONS : String = "total_questions"
    const val CORRECT_ANSWERS : String = "correct_answers"

    fun getQuestions(num :Int): ArrayList<Question>{
        var questionsList = ArrayList<Question>()
        var newCountries = ArrayList<Country>()
        var newCountry : Country
        var que : Question
        var correctAnswer : Int
        for (i in 1..num) {
            newCountries.clear()
            for (ii in 1..4) {
                newCountry = allcountries.random()
                while (newCountries.size>0 && newCountries.contains(newCountry)) {
                    newCountry = allcountries.random()
                }
                newCountries.add(newCountry)
            }
            correctAnswer = (1..4).random()

            que = Question(
                i, "What country does this flag belong to?",
                newCountries[correctAnswer-1].imageloc,
                newCountries[0].name,
                newCountries[1].name,
                newCountries[2].name,
                newCountries[3].name,
                correctAnswer
            )
            questionsList.add(que)
        }
        return questionsList
    }
}
