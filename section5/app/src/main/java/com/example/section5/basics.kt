package com.example.section5

fun main(){

    // ARRAYS
    // equivalent initializations

    println("***** arrays *****")

    val numbers1: IntArray = intArrayOf(1,2,3,4,5,6)
    val numbers2 = intArrayOf(1,2,3,4,5,6)
    val numbers3 = arrayOf(1,2,3,4,5,6)

    println(numbers1.contentToString())

    for (element in numbers2){
        print("$element ")
    }
    println()

    val days = arrayOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
    println(days.contentToString())

    val fruits = arrayOf(Fruit("apple", 2.5), Fruit("grape", 3.5))
    println(fruits.contentToString())
    for (index in fruits.indices){
        println("${fruits[index].name} is in index $index")
    }
    for (fruit in fruits){
        println("${fruit.name}")
    }

    // LISTS - immutable type

    println("***** arrays *****")


    val months = listOf("Jan","Feb","Mar","Apr")
    val anyTypes = listOf(1,2,true,"Hello",1.001)
    println(anyTypes.size)

    for (month in months){
        print("$month ")
    }
    println()

    val additionalMonths = months.toMutableList()
    val newMonths = arrayOf("May","Jun","Jul")
    additionalMonths.addAll(newMonths)
    println(additionalMonths)

    val dayss = mutableListOf<String>("Mon","Tue","Wed")
    dayss.add("Thu")
    println(dayss)
    dayss.removeAt(0)
    val removeList = mutableListOf<String>("Mon","Wed")
    dayss.removeAll(removeList)
    println(dayss)

    // SETS

    println("***** sets *****")

    val fruits2 = setOf("Orange","Apple","Grape","Apple")
    println(fruits2.size) // will print 3 because there are only three unique items
    println(fruits2.toSortedSet())

    val newFruits2 = fruits2.toMutableList()
    newFruits2.add("Water Melon")
    newFruits2.add("Pear")
    println(newFruits2)
    println(newFruits2.elementAt(4))

    // MAPS
    println("***** maps *****")
    val daysOfTheWeek = mapOf(1 to "Monday", 2 to "Tuesday", 3 to "Wednsday")
    println(daysOfTheWeek[1])
    for (key in daysOfTheWeek.keys){
        println("$key is to ${daysOfTheWeek[key]}")
    }

    val fruitsMap = mapOf("Favorite" to Fruit("Grape", 2.5), "OK" to Fruit("apple",1.0))

    val newDaysOfWeek = daysOfTheWeek.toMutableMap()
    newDaysOfWeek[4] = "Thursday"
    newDaysOfWeek[5] = "Friday"
    println(newDaysOfWeek.toSortedMap())

    val lst = ArrayList<Double>()
    lst.add(1.0)
    lst.add(3.0)
    lst.add(2.0)
    lst.add(5.0)
    lst.add(4.0)
    println("${lst.sum()/lst.size}")

    // lambda expressions
    println("*****lambda expressions*****")
    val sum1:(Int, Int) -> Int = {a:Int, b:Int -> a+ b}
    val sum2 = {a:Int, b:Int -> a+b}
    println("${sum2(3,5)}")

    // access modifiers
}

data class Fruit(val name:String, val price:Double)


