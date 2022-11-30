package com.example.kotlinbasics

fun main() {
    var myName = "Frank" // one way to declare variable using type inference
    var yourName : String = "Roman" // another way to declare variable specifying the type explicitly
    
    print("Hello $myName and $yourName\n") // this is called a string template

    print("The last letter of my variable is ${yourName[myName.length-1]}\n") // using {} to insert function call is called template expression of string interpolation

    val myByte : Byte = 13
    val myShort : Short = 15
    val myInt : Int = 123123
    val myLong: Long = 12_234_151_824

    val myFloat : Float = 12.345F
    val myDouble : Double = 3.141592653

    var isSunny : Boolean = true

    val letterChar = 'A'
    val numberChar = '1'

    val a = 5.0
    val b = 3
    val c = 5
    var resultD : Double = a/b
    var resultI1 : Int = (a/b).toInt()
    var resultI2 = c/b
    print("$resultD $resultI1 $resultI2\n")
    /* if else statements
     */

    var name = "Roman"
    if (name == "Roman"){
        println("Welcome home, Roman")
    } else {
        println("Who are you??")
    }

    val age = 17
    val drinkingAge = 21
    val votingAge = 18
    val drivingAge = 16
    val currentAge = if(age>=drinkingAge){
        println("Now you may drink in the US")
        drinkingAge // this statement alone will return the value to whatever variable its being assigned to
    }else if(age>=votingAge){
        println("You may vote now")
        votingAge
    }else if(age>=drivingAge){
        println("You may drive now")
        drivingAge
    }else{
        println("You are too young")
        age
    }
    println("current age is $currentAge")

    // when expression
    // when statements are evaluate stop to bottom and stop when one is triggered

    var season = 3
    when(season){
        1 -> println("Spring")
        2 -> println("Summer")
        3 -> println("Fall")
        4 -> println("Winter")
        else -> println("Invalid Season")
    }
    var month = 3
    when(month){
        in 3..5->println("Spring")
        in 6..8->println("Summer")
        in 9..11->println("Fall")
        12,1,2 -> println("Winter")
        !in 1..12 -> println("Invalid Season")
    }
    var x : Any = 13.37
    when(x){
        is Int -> println("$x is an Int")
        is Double -> println("$x is a Double")
        is String -> println("$x is a String")
        else -> println("$x is none of the above")
    }

    // while statements

    var cc: Int = 1
    while ( cc <= 10) {
        print("${cc++} ")
    }
    println()

    // do+while statement will run the entries of do block at least once!

    cc = 1
    do{
        print("${cc++} ")
    } while (cc<=10)
    println()
    var feltTemp = "cold"
    var roomTemp = 10
    while (feltTemp == "cold"){
        roomTemp++
        if (roomTemp>=20){
            feltTemp = "comfy"
            println("Its comfy now")
        }
    }

    // for loops

    for (num in 1..10){
        print("$num ")
    }
    println()

    for (num in 1 until 11){
        print("$num ")
    }
    println()

    for (num in 10 downTo 1){
        print("$num ")
    }
    println()

    for (num in 10 downTo 1 step 3){
        print("$num ")
    }
    println()

    // break and continue

    myFunction()

    println("${addUp(3,5)}")

    // nullables

    var name2 : String = "Denis" // we can't assigned null to the String type
    var randnull : String? = "Roman" // we use a type called string nullable, initialized with String?
    randnull = null

    var len2 = name2.length // we can find the length of the string but not a nullable
    // var len3 = randnull.length // illegal
    var len3 = randnull?.length // this is legal - we are specifying that we are working with a null
    println("$len2 $len3")

    randnull?.let { println(randnull.length) } // will print when randnull isnt null

    var newname = randnull?: "Guest" // will assign a a non null value to newname if randnull is non null, otherwise assigns default Guest
    newname!!.toLowerCase() // !! will throw an exception with newname is a null

}

fun myFunction(){
    println("Called from myFunction")
}

fun addUp(a:Int, b:Int) : Int {
    return a + b
}