package ch6_Class_design

fun String.isPhoneNumber(): Boolean =
    length == 7 && all { it.isDigit() }

내부적으로, 아래와 같이 컴파일 됨.

fun isPhoneNumber(`$this`: String): Boolean =
    `$this`.length == 7 && `$this`.all { it.isDigit() }

interface PhoneBook {
    fun String.isPhoneNumber(): Boolean
}

class Fizz : PhoneBook {
    override fun String.isPhoneNumber(): Boolean =
        length == 7 && all { it.isDigit() }
}

// Bad practice, do not do this
class PhoneBookIncorrect {
    // ...
    fun String.isPhoneNumber() =
       length == 7 && all { it.isDigit() }
}

// This is how we limit extension functions visibility
class PhoneBookCorrect {
    // ...
}

private fun String.isPhoneNumber() =
    length == 7 && all { it.isDigit() }

val ref = String::isPhoneNumber
val str = "1234567890"
val boundedRef = str::isPhoneNumber

val refX = PhoneBookIncorrect::isPhoneNumber // ERROR
val boundedRefX = PhoneBookIncorrect()::isPhoneNumber // ERROR

/*class A {
    val a = 10
}
class B {
    val b = 10
    fun A.test() = a + b
}*/
class A {
    // ....
}
class B {
    // ....
    fun A.update() = ...// Shell is update A or B?
}

fun main() {
    PhoneBookIncorrect().apply {"1234567890".test()}
}