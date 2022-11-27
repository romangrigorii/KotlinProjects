package com.example.kotlinbasics2

fun main(){
    var newperson1 = Person("James", "Bond")
    var newperson2 = Person("Roman","Grigorii",30)
    newperson2.stateHobby()
    newperson2.stateAge()

    var car = Car()
    println("the car brand is ${car.myBrand}")
    println("the car speed is ${car.maxSpeed}")
    car.maxSpeed += 20
    println("with modification it can go as fast as ${car.maxSpeed}")

    val user1 = User(1, "Denis")
    user1.name = "Michael" // note we can change the name but not the id
    val user2 = User(1, "Roman")
    println(user1 == user2) // this allows us to check if the content of the class is the same
    println(user1)
    // this allows us to copy all the attributes from one class to another while changing what we want via a constructor
    val user3 = user1.copy(name = "Tabitha")
    println("$user1, $user3")
    // we can extract attributes from a class
    val (a,b) = user3
    println("id: $a name: $b")

    var yourCar = RegularCar(170.0,"A3", "Audi")
    var myCar = ElectricCar(200.0,"S-Model", "Tesla", 85.0)
    yourCar.drive(100.0)
    myCar.drive(100.0)
    myCar.brake()
    yourCar.brake()

    var newhuman = Human("Roman", "Moldova", 70.0, 30.0)
    newhuman.run()

    typecaststuff()
}


class Person(firstName: String = "Roman", lastName: String = "Grigorii"){
    // member variables
    var age : Int? = null
    var hobby : String = "watching Netflix"
    var firstName = firstName
    var lastName = lastName

    // Initialization block
    init {
        println("The name is $lastName, $firstName $lastName")
    }

    // member secondary constructor. if we ever pass an age to the class, it will be saved within the class

    constructor(firstName: String, lastName: String, age: Int) : this(firstName, lastName){
        this.age = age
    }

    // member functions

    fun stateHobby(){
        println("${this.firstName}'s hobby is ${this.hobby}")
    }

    fun stateAge(){
        println("${this.firstName}'s age is ${this.age}")
    }
}

// parameters and variables are different things!
fun myFunction(a : Int) {
    // this is a different a from the one we passed to the function
    // var a = 5
    // upon defining a variable a, that is the a that will be used
    // we can also assigned the parameter to the variable
    var a = a // assigning variable of the same as the parameter to the parameter value is called shadowing
}


class Car(){
    lateinit var owner : String // means we will initialize it later on
    val myBrand : String = "BMW"
        get() {
            return field.toLowerCase()
        }
    var maxSpeed: Int = 250
        set(value){
            field = value
        }
    private var myModel : String = "M5"
        private set // this means that myModel is available to set only within the class
        private get // this means that myModel is available to get only within the class

    init {
        this.owner = "Roman";
    }
}

data class User(val id: Long, var name: String){

}

//**** INHERITANCE

// Super Class, Parent Class, or Base class of ElectricCar
open class RegularCar(override val maxSpeed: Double, name: String, val brand : String) : Drivable {
    open var range : Double  = 200.0
    fun extendRange(amount : Double){
        if (amount>0){
            range += amount
        }
    }

    override fun drive() : String {
        return "Just drive"
    }
    open fun drive(distance : Double){
        println("I drove for $distance km")
    }
}
// Sub class, Child class, or Derived Class of RegularCar
// Observe how Electric car inherits the parameters of RegularCar
class ElectricCar(maxSpeed: Double, name: String, brand: String, batteryLife : Double) : RegularCar(maxSpeed,name,brand){
    override var range : Double = batteryLife * 6
    var chargerType = 1
    override fun drive(distance: Double) {
        println("I drove for $distance km using electricity")
    }
    override fun drive() : String {
        return "I drove for many km using electricity"
    }

    override fun brake(){
        super.brake() // this will take on the inherited functionality of brake
        // as it stands this is redundant, but we may add functionality
        println("brake of the electric car")
    }
}

//**** INTERFACE

interface Drivable{
    val maxSpeed : Double
    fun drive(): String
    fun brake(){
        println("The driver is braking")
    }
}
// Vehicle is called Super Class, Parent Class, or Base class of RegularCar
abstract class Vehicle() : Drivable {

    override var maxSpeed : Double = 100.0

    override fun drive(): String {
        return "a drive string"
    }

    open fun drive(distance : Double){
        println("I drove for $distance km")
    }

    override fun brake(){
    }
}

// abstract classes

abstract class Mammal(private val name : String, private val origin : String, private val weight : Double){
    abstract val maxSpeed : Double
    abstract fun run()
    abstract fun breath()
}

class Human(name:String, origin:String, weight:Double, override var maxSpeed: Double) : Mammal(name, origin, weight){
    override fun run(){
        println("I run on two legs")
    }
    override fun breath(){
        println("I breath through my nose")
    }
}

class Elephant(name:String, origin:String, weight:Double, override var maxSpeed: Double) : Mammal(name, origin, weight){
    override fun run(){
        println("I run on four legs")
    }
    override fun breath(){
        println("I breath through my trunk")
    }
}

// ** TYPECASTING

fun typecaststuff(){

    val stringlist: List<String> = listOf( "Roman", "Denis", "Ana")
    val objectlist: List<Any> = listOf("Roman", 1, 12)

    for (value in objectlist){
        if (value is Int){
            println("Integer: $value")
        } else if (value is Double){
            println("Double: $value")
        } else if (value is String){
            println("String: '$value' of length ${value.length}")
        } else {
            println("unknown type O_._O")
        }
    }

    for (value in objectlist){
        when (value){
            is Int -> println("Integer: $value")
            is Double -> println("Double: $value")
            is String -> println("String: '$value' of length ${value.length}")
            else -> println("Unknown Type")
        }
    }

    val str1 : Any = 12312
    val str3 : Any  = "123"
    val str2: String? = str1 as? String // this will convert str1 to string
    println("$str1 is a String: ${str1 is String}")
    println("$str2 is a String: ${str2 is String}")
    println("${str3 as? Int} ")
}