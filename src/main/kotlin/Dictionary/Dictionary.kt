package Dictionary

import java.util.*

fun double(i: Int) = i * 2  // top-level function

class A {
    private fun triple(i: Int) = i * 3  // member function

    fun twelveTimes(i: Int): Int {          // member function
        fun fourTimes() = double(double(i)) // local function
        return triple(fourTimes())
    }
}

val double = fun(i: Int) = i * 2    // Anonymous function
val triple = {i: Int -> i * 3}  // Lambda expression
// which is a shorter notation for an anonymous function


class IntWrapper(val i: Int) {
    fun doubled(): IntWrapper = IntWrapper(i * 2)
}

// Usage
val wrapper = IntWrapper(10)
val doubledWrapper = wrapper.doubled()
val doubledReference = IntWrapper::doubled


fun IntWrapper.tripled() = IntWrapper(i * 3)

// Use
//val wrapper = IntWrapper(10)
val tripledWrapper = wrapper.tripled()
val tripledReference = IntWrapper::tripled
// Notice that to call both references we need to pass an instance of IntWrapper:
val doubledWrapper2 = doubledReference(wrapper)
val tripledWrapper2 = tripledReference(wrapper)

class User(
    val name: String,
    val surname: String
) {
    val fullName: String
        get() = "$name $surname"

    fun withSurname(surname: String) =
        User(this.name, surname)
}

val User.officialFullName: String
    get() = "$surname, $name"

fun User.withName(name: String) =
    User(name, this.surname)

fun randomString(length: Int): String {
    //.....
    return Math.random().toString()
}
val randStr = randomString(10)

inline fun <reified T> printName() {
    print(T::class.simpleName)
}

fun main() {
    printName<String>() // String
}

class SomeObject {
    val text: String
    constructor(text: String) {
        this.text = text
        print("Creating object")
    }
}

/*
class SomeObject(text: String) {
    val text: String = text
    init {
        print("Creating object")
    }
}

class SomeObject(val text: String) {
    init {
        print("Creating object")
    }
}

class SomeObject(val text: String) {
    constructor(date: Date): this(date.toString())
    init {
        print("Creating object")
    }
}

class SomeObject @JvmOverloads constructor(
    val text: String = ""
) {
    init {
        print("Creating object")
    }
}*/
