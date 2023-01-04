package com.example.standardfunction

fun main(args: Array<String>) {
    val firstElement = listOf(1,2,3).first().let{
        it * it
    }
    println("$firstElement")
}