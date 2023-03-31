package ch1_Safety.Item1_Limit_mutability

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ProtocolFamily
import java.util.*
import kotlin.concurrent.thread

/*fun main() {
    val account = Chapter1.BackAccount()
    println(account.balance)
    account.deposit(100.0)
    println(account.balance)
    account.withdraw(50.0)
    println(account.balance)

    var num = 0
    for (i in 1..1000) {
        thread {
            Thread.sleep(10)
            num += 1
        }
    }
    Thread.sleep(5000)
    print(num)
}*/
var name: String = "Michael"
var surname: String = "Jordan"
val fullName
    get() = "$name $surname"
/*suspend fun main() {
    var num = 0
    coroutineScope {
        for (i in 1..1000) {
            launch {
                delay(10)
                num += 1
            }
        }
    }
    println(num)

    val lock = Any()
    var num1 = 0
    for (i in 1..1000) {
        thread {
            Thread.sleep(10 )
            synchronized(lock) {
                num1 += 1
            }
        }
    }
    Thread.sleep(1000)
    println(num1)

    // real-only property 인 val
    val a = 10
    // a = 20  // Error !
    // read-only property 지만 반드시 immutable 하거나 final 인 것은 아니다.
    // mutable object 를 가질 수 있다.
    val list = mutableListOf(1, 2, 3)
    list.add(4)
    println(list)     // [1, 2, 3, 4]
    // read-only prop 은 다른 prop 에 연관된 custom getter 를 정의해서 사용할 수 있다.

    println(fullName)
    name = "Mono"
    println(fullName)
    // 우리가 정의한 custom getter 가 있기때문에 값을 요청할 때마다 호출됩니다.
    println(fizz)   // 42
    println(fizz)   // 42
    println(buzz)   // Calculating... 42
    println(buzz)   // Calculating... 42


    if (fullName2 != null) {
        //println(fullName2.length)   // Error
    }
    if (fullName3 != null) {
        println(fullName3.length)   // Mike Brown
    }

    println(mutableList)
    mutableList.add(4)
    println(mutableList)
}*/
// val은 정의 옆에 상태가 바로 적히므로, 코드의 실행을 예측하는 것이 훨씬 간단하며, 스마트 캐스트 등의 추가적인 기능을 활용할 수 있다.
// fullName2은 게터로 정의했으므로 스마트 캐스트할 수 없다. 게터를 활용하므로, 값을 사용하는 시점의 name에 따라서 다른 결과가 나올 수 있기 때문이다.
// fullName3와 같이 Non-local prop이면서, final 이고, custom getter를 갖지 않는다면 스마트캐스트될 수 있다.
val name2: String? = "Mike"
val surname2: String = "Brown"
val fullName2: String?
    get() = name2?.let { "$it $surname2" }
val fullName3: String? = name2?.let { "$it $surname2" }

// Kotlin 의 속성은 기본적으로 캡슐화되며 사용자 지정 접근자(getter 및 setter)를 가질 수 있는 이 특성은 API 를 변경하거나 정의할 때 유연성을 제공하기 때문에 Kotlin 에서 매우 중요합니다.
//항목 16: 속성은 동작이 아니라 상태를 나타내야 합니다.에서 자세히 설명합니다. 그러나 핵심 아이디어는 var 는 게터/세터가 있지만, val 은 getter 만 있기 때문에 mutate 지점을 제공하지 않는다는 것입니다. 이것이 바로 val 을 var 로 override 할 수 있는 이유입니다.
interface Element {
    val active: Boolean
}
class ActualElement: Element {
    override var active: Boolean = false
}

fun calculate(): Int {
    println("Calculating...")
    return 42
}
val fizz = calculate()  // Calculating...
val buzz
    get() = calculate()

class Chapter1 {
    var a = 10
    val list: MutableList<Int> = mutableListOf()

    class BackAccount {
        var balance = 0.0
            private  set
        fun deposit(depositAmount: Double) {
            balance += depositAmount
        }
        @Throws(InsufficientFunds::class)
        fun withdraw(withdrawAmount: Double) {
            if (balance < withdrawAmount) {
                throw InsufficientFunds()
            }
            balance -= withdrawAmount
        }
    }

    class InsufficientFunds : Exception()
}

// 코틀린에 read-write 와 read-only property 가 구분되듯이 read-write collections 와 read-only collections 가 나뉘어있다.
//인터페이스 읽기 전용: Iterable, Collection, Set, List
//읽고 쓸 수 있는 컬렉션: MutableIterable, MutableCollection, MutableSet, MutableList
//컬렉션 다운캐스팅은 읽기 전용으로 리턴하였는데, 이를 읽기전용으로만 사용하지 않는 계약을 위반하고, 추상화를 무시하는 행위이다. 이런 코드는 안전하지 않고, 예측하지 못한 결과를 초래한다.

val list = listOf(1, 2, 3)

// Don't DO THIS!
//if (list is MutableList) {
//    list.add(4)
//}
// DO THIS.
val mutableList = list.toMutableList()
// mutableList.add(4) // main 안에서 수행.


//3. Data Class 의 Copy
//immutable 객체사용의 장점
//생성될 때 상태에 머무르기 때문에 추론하기 쉽다.
//공유된 객체 사이의 충돌하지 않기 때문에 프로그램을 병렬화하기 쉽다.
//immutable한 객체는 변하지 않기 때문에, 해당 객체에 대한 참조는 cache될 수 있다.
//방어적인 copy를 수행할 필요가 없다. 즉 immutable한 object에 대한 copy를 수행할 때 반드시 deep copy를 할 필요가 없다.
//immutable한 객체는 이를 활용해서 다른 객체를 만드는 데에 완벽한 재료이다.
//immutable한 객체는 map에서 key로 활용될 수 있다. 이에 반해 mutable object는 map의 key로 절대 사용해서는 안된다. → 동작을 예측하기 어렵다.

/*fun main() {
    val person = FullName("Gana", "Kim")
    val names: Set<FullName> = setOf(
        person,
        FullName("Dara", "Lee"),
        FullName("Maba", "Park"),
        FullName("Saah", "Choi")
    )
    println(names)
    println(person in names)
    person.name = "Black"
    println(names)
    println(person in names)
    person.name = "Yellow"
    println(names)
    println(person in names)
}
data class FullName(var name: String, var surname: String)*/

// immutable object의 단점?은 값을 바꿀 수 없다는 것이다. 방법은 새로운 객체를 만들어내는 것이다.
/*fun main() {
    var user = User("Jordan", "M")
    println(user)
    user = user.withSurname("J");
    println(user)
}

class User(
    private val name: String,
    private val surname: String
) {
    fun withSurname(surname: String) = User(name, surname)
    override fun toString(): String {
        return "$name $surname"
    }
}*/

// kotlin data class를 활용해서 더 나이스하게 할 수 있다. (data class는 copy를 구현하고 있다.)
/*fun main() {
    var user = User("Jordan", "M")
    println(user)
    user = user.copy(surname = "J")
    println(user)
}

data class User(
    val name: String,
    val surname: String
)*/

// 다른 종류의 mutation 지점
/*fun main() {
    val list1: MutableList<Int> = mutableListOf() // read-only 프로퍼티에 mutable한 collection 생성
    var list2: List<Int> = listOf() // read-write 프로퍼티에 immutable한 collection 생성

    // 두 프로퍼티 모두 변경 가능하다.
    list1.add(1)
    list2 = list2 + 1

    println(list1)
    println(list2)

    // 동일하게 아래와 같이 표현할 수도 있다.
    list1 += 2 // 내부적으로 list1.plusAssign(1)
    list2 += 2 // 내부적으로 list2. = list2.plus(1)

    println(list1)
    println(list2)

    // 1번의 장점은 single mutating point를 가지고 있고, 구현체가 synchronization을 구현하고 있어서 따로 구현할 필요는 없지만, 구현체에 의존한다는 단점이 있다.
//    1번의 경우 구체적인 리스트 구현 내부에 변경 가능 지점이 있어, 멀티스레드 처리가 이루어질 경우, 내부적으로 적절한 동기화가 되어있는지 알 수 없어 위험하다.
//    2번의 경우 프로퍼티 자체가 변경 가능 지점이라 멀티스레드 처리의 안정성이 더 좋다.
    // 2번은 synchronization을 구현해야 하지만, 구현체에 의존하지 않는다는 장점이 있다.
}*/
// 제일하지 말아야할 것은 read-write 프로퍼티에 mutable collection을 생성하는 것이다.*

// mutation point를 노출하지 말자
/*
fun main() {
    val userRepo = UserRepo()
    println(userRepo.loadAll())

    userRepo.loadAll()[1] = User("Bee")
    println(userRepo.loadAll())
}

class UserRepo() {
    private val storedUsers: MutableMap<Int, User> = mutableMapOf(
        1 to User("Red"),
        2 to User("Blue"),
        3 to User("Green"),
        4 to User("Yellow")
    )
    fun loadAll() = storedUsers
}

data class User(val name: String)*/

// immutable한 collection으로 upcasting하는 방법의 가변성 제한 방법을 사용할 수 있다.
/*
fun main() {
    val userRepo = UserRepo()
    println(userRepo.loadAll())

    //userRepo.loadAll()[1] = User("Bee") //compile error
    println(userRepo.loadAll())
}

class UserRepo() {
    private val storedUsers: MutableMap<Int, User> = mutableMapOf(
        1 to User("Red"),
        2 to User("Blue"),
        3 to User("Green"),
        4 to User("Yellow")
    )
    fun loadAll(): Map<Int, User> { return storedUsers }
}

data class User(val name: String)*/

/*
본 장에서는 가변성을 제한하는 것이 왜 중요하고 왜 immutable한 객체를 선호하는지 배웠다. 아래 룰을 기억하자.

var 보다는 val을 우선 고려하자.
mutable한 프로퍼티 보다는 immutable한 프로퍼티를 우선 고려하자.
mutable한 객체 보다는 immutable한 객체를 우선 고려하자.
값을 변경해야 한다면, immutable 한 data class 객체를 사용해서 copy를 활용하는 것을 고려하자.
mutable collection 보다는 read-only collection를 우선 고려하자.
mutation points를 잘 설계해서 필요 없는 부분을 만들지 말자.
mutable한 객체를 노출하지 말자.

 */