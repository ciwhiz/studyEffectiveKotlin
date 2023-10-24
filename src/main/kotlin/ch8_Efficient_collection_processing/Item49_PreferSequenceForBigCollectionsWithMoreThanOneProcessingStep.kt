package ch8_Efficient_collection_processing

import java.io.File
import java.lang.Thread.sleep
import kotlin.system.exitProcess

class Item49_PreferSequenceForBigCollectionsWithMoreThanOneProcessingStep {
}

fun main() {
    Item49_PreferSequenceForBigCollectionsWithMoreThanOneProcessingStep()
    println("helo item49")

    sequenceOf(1, 2, 3)
        .filter { print("F$it, "); it % 2 == 1 }
        .map { print("M$it, "); it * 2 }
        .forEach { print("E$it, ") }
    // Prints: F1, M1, E2, F2, F3, M3, E6,
    println()
    listOf(1, 2, 3)
        .filter { print("F$it, "); it % 2 == 1 }
        .map { print("M$it, "); it * 2 }
        .forEach { print("E$it, ") }
    // Prints: F1, F2, F3, M1, M3, E2, E6,
    println()
    for (e in listOf(1, 2, 3)) {
        print("F$e, ")
        if (e % 2 == 1) {
            print("M$e, ")
            val mapped = e * 2
            print("E$mapped, ")
        }
    }
    // Prints: F1, M1, E2, F2, F3, M3, E6,
    println()
    println("find sample")
    (1..10).asSequence()
        .filter { print("F$it, "); it % 2 == 1 }
        .map { print("M$it, "); it * 2 }
        .find { it > 5 }
    //Prints:F1,M1,F2,F3,M3,
    println()
    (1..10)
        .filter { print("F$it, "); it % 2 == 1 }
        .map { print("M$it, "); it * 2 }
        .find { it > 5 }
    //Prints:F1,F2,F3,F4,F5,F6,F7,F8,F9,F10, //M1,M3,M5,M7,M9,

    println()
    println("sequence can be infinite")
    generateSequence(1) { it + 1 }
        .map { it * 2 }
        .take(10)
        .forEach { print("$it, ") } //Prints:2,4,6,8,10,12,14,16,18,20,

    println()
    println("fibonacci")
    val fibonacci = sequence {
        yield(1)
        var current = 1
        var prev = 1
        while (true) {
            yield(current)
            val temp = prev
            prev = current
            current += temp
        }
    }
    print(fibonacci.take(10).toList())
    //[1,1,2,3,5,8,13,21,34,55]
    // print(fibonacci.toList()) // Runs forever timeout
    println()
    println("sequence do not create every step ")
    val numbers = listOf(1, 2, 3)
    numbers
        .filter { it % 10 == 0 } // 1 collection here
        .map { it * 2 } // 1 collection here
        .sum()
    print(numbers)
    //In total,2 collections created under the hood
    numbers
        .asSequence()
        .filter { it % 10 == 0 }
        .map { it * 2 }
        .sum()
    //No collections created
    println()
    println("잘못된 파일 읽기")
    //BAD SOLUTION, DO NOT USE COLLECTIONS FOR
    //POSSIBLY BIG FILES
    File("/Users/sholee/dev/kotlin/studyEffectiveKotlin/src/main/kotlin/ch8_Efficient_collection_processing/ChicagoCrimes.csv").readLines()
        .drop(1) // Drop descriptions of the columns
        .mapNotNull { it.split(",").getOrNull(6) }
        // Find description
        .filter { "CANNABIS" in it }
        .count()
        .let(::println)
    println()
    println("올바른 파일 읽기")
    File("/Users/sholee/dev/kotlin/studyEffectiveKotlin/src/main/kotlin/ch8_Efficient_collection_processing/ChicagoCrimes.csv").useLines { lines ->
        //The type of `lines` is Sequence<String>
        lines
            .drop(1) // Drop descriptions of the columns
            .mapNotNull { it.split(",").getOrNull(6) }
            // Find description
            .filter { "CANNABIS" in it }
            .count()
            .let { println(it) } // 318185
    }

    println()
    println("----")
    class Product(var bought: Boolean, var price: Double){

    }
    val productsList = listOf(Product(true, 100.9), Product(true, 200.1), Product(false, 50.2))
    fun singleStepListProcessing(): List<Product> {
        return productsList.filter { it.bought }
    }
    fun singleStepSequenceProcessing(): List<Product> {
        return productsList.asSequence()
        .filter { it.bought }
        .toList()
    }

    println()
    println("----")
    fun twoStepListProcessing(): List<Double> {
        return productsList
            .filter { it.bought }
            .map { it.price }
    }
    fun twoStepSequenceProcessing(): List<Double> {
        return productsList.asSequence()
            .filter { it.bought }
            .map { it.price }
            .toList()
    }
    fun threeStepListProcessing(): Double {
        return productsList
            .filter { it.bought }
            .map { it.price }
            .average()
    }
    fun threeStepSequenceProcessing(): Double {
        return productsList.asSequence()
            .filter { it.bought }
            .map { it.price }
            .average()
    }

    println()
    println()
    generateSequence(0){it+1}.take(10).sorted().toList()
//. [0,1,2,3,4,5,6,7,8,9]
    generateSequence(0){it+1}.sorted().take(10).toList()
// Infinite time. Does not return.


    println()
    println("What about Java stream?")

    productsList.asSequence()
        .filter { it.bought }
        .map { it.price }
        .average()
    productsList.stream()
        .filter { it.bought }
        .mapToDouble { it.price }
        .average()
        .orElse(0.0)

}