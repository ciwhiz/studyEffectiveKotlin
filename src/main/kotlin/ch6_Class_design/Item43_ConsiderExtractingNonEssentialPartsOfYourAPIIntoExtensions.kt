package ch6_Class_design

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/*//Defining methods as members
class Workshop(*//*....*//*) {
    //...
    fun makeEvent(date: DateTime): Event = //...
    val permalink
        get() = "/workshop/$name"
}

//============================================================

//Defining methods as extensions
class Workshop(*//*...*//*) {
    //...
}
fun Workshop.makeEvent(date:DateTime):Event=//...

val Workshop.permalink
    get() = "/workshop/$name"

fun useWorkshop(workshop: Wokrshop) {
    val event = workshop.makeEvent(date)
    val permalink = workshop.permalink
    val makeEventRef = Workshop::makeEvent
    val permalinkPropRef = Workshop::permalink
}*/

open class C
class D: C()
//fun C.foo() = "c"
//fun D.foo() = "d"

/*fun main() {
    val d = D()
    print(d.foo()) // d
    //val c: C = d
    val c = C()
    print(c.foo()) // c

    print(D().foo()) // d
    print((D() as C).foo()) // c
}*/

fun foo(`this$receiver`: C) = "c"
fun foo(`this$receiver`: D) = "d"

fun main() {
    val d = D()
    print (foo(d)) // d

    val c: C = d
    print (foo(c)) // c

    print (foo(D())) // d
    print (foo(D() as C)) // c
}

@OptIn(ExperimentalContracts::class)
inline fun CharSequence?.isNullOrBlank(): Boolean {
    contract {
        returns(false) implies (this@isNullOrBlank != null)
    }
    return this == null || this.isBlank()
}

public fun Iterable<Int>.sum(): Int {
    var sum: Int = 0
    for (element in this) {
        sum += element
    }
    return sum
}