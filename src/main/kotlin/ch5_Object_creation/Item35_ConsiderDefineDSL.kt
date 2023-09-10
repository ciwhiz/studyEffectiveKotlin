package ch5_Object_creation

import javax.swing.text.html.HTML
import kotlin.reflect.KClass

class Item35_ConsiderDefineDSL {

    // 함수리터럴이란, 함수를 어떤 변수로 받았을 때 그 함수 내용 그 자체.
    // 람다표현식과 익명함수는 함수 리터럴, 함수 리터럴은 객체로 선언이 안되어있지만,
    // 표현식을 통해 값으로 전달되는 함수다.
    // 여기서는,  { x: Int, y: Int -> x + y } 자체.
    val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }


    // receiver가 있는 함수 리터럴 이란?
    /*
    A.(B) -> C
    fun main(){
        val sum = fun Int.(other: Int): Int = this + other
        var a = 10
        print(a.sum(5))
    }
    Int.(Int) -> Int

     */

    /*
    DSL 이란 무엇일까요? DSL은 Domain Specific Language의 약자로 특정 도메인에 국한해 사용하는 언어입니다. 반대 개념으로는 General Purpose Language가 있으며 우리가 일반적으로 사용하는 C, C++, Kotlin, Swift 등의 프로그래밍 언어들이 이에 해당합니다.

    GPL -> 일상언어, DSL -> 전문 용어

    우리는 Kotlin 언어가 제공하는 다음의 몇 가지 기능들을 조합하여 간단히 DSL을 정의하고 사용할 수 있습니다.
    람다 표현식 (Lambda Expression)
    고계함수 (Higher-order Function)
    확장함수 (Extension Function)

     */
}

fun main(args: Array<String>) {
    val test: (HTML.() -> Unit) = {
        println("test")
    }
    html(test)
}

class HTML {
    fun body(){
        println("HTML")
    }
}

fun html(aaa:HTML.() -> Unit): HTML {
    val html = HTML() // receiver 객체 생성
    html.aaa()        // receiver 객체를 람다로 전달
    return html
}
//
//html {    // receiver가 있는 람다의 시작점
//  body()  // receiver 객체의 메서드를 호출
//}

//test

/*
Item 35: Consider defining a DSL for complex object creation

함께 사용된 코틀린 기능 세트는 우리가 만들 수 있게 해줍니다. 구성과 유사한 DSL(도메인 특정 언어)
이러한 DSL은 우리가 더 복잡한 객체나 객체의 계층 구조를 정의해야 할 때 유용합니다.
정의하기는 쉽지 않지만 일단 정의되면 Boilerplate 와 복잡성을 줄이고, 개발자는 자신의 의도를 명확하게 표현할 수 있습니다
예를 들어, Kotlin DSL은 HTML을 표현하는 인기 있는 방법입니다:
클래식 HTML과 리액트 HTML을 모두 사용할 수 있습니다. 이런 식으로 보일 수 있습니다

body {
    div {
        a("https://kotlinlang.org") {
            target = ATarget.blank
            + "Main site"
        }
    }
    +"Some content"
}
다른 플랫폼의 보기도 DSL을 사용하여 정의할 수 있습니다.
다음은 Anko 라이브러리를 사용하여 정의된 간단한 Android 보기입니다.
verticalLayout {
     val name = editText()
     button("Say Hello") {
        onClick { toast("Hello, ${name.text}!") }
     }
 }

데스크탑 앱과 비슷하다. 다음은 JavaFX 위에 구축된 TornadoFX에 정의된 보기입니다.
class HelloWorld : View() {
    override val root = hbox {
        label("Hello world") {
            addClass(heading)
        }

        textfield {
            promptText = "Enter your name"
        }
    }
}
DSL은 또한 데이터 또는 구성을 정의하는 데 자주 사용됩니다. 다음은 DSL인 Ktor의 API 정의입니다.
fun Routing.api() {
    route("news") {
        get {
            val newsData = NewsUseCase.getAcceptedNewsData()
            call.respond(newsData)
        }
        get("propositions") {
            requireSecret()
            val newsData = NewsUseCase.getPropositions()
            call.respond(newsData)
        }
    }
    // ...
 }
다음은 Kotlin 테스트에 정의된 테스트 사례 사양입니다:
class MyTests : StringSpec({
    "length should return size of string" {
    "hello".length shouldBe 5
    }
    "startsWith should test for a prefix" {
     "world" should startWith("wor")
     }
 })

 Gradle DSL을 사용하여 Gradle 구성을 정의할 수도 있습니다.
 plugins {
     `java-library`
 }

 dependencies {
    api("junit:junit:4.12")
    implementation("junit:junit:4.12")
    testImplementation("junit:junit:4.12")
 }

 configurations {
    implementation {
          resolutionStrategy.failOnVersionConflict()
 }
 ......
DSL을 사용하면 복잡하고 계층적인 데이터 구조를 쉽게 만들 수 있습니다.
이러한 DSL 내에서 Kotlin이 제공하는 모든 것을 사용할 수 있으며 Kotlin에서 DSL로 유용한 힌트를 얻을 수 있습니다.
Groovy와 달리 완전히 형식이 안전합니다. 일부 Kotlin DSL을 이미 사용했을 가능성이 높지만 이를 직접 정의하는 방법을 아는 것도 중요합니다.

Defining your own DSL
자체 DSL을 만드는 방법을 이해하려면 receiver 가 있는 함수 유형의 개념을 이해하는 것이 중요합니다.
그러나 그 전에 먼저 함수 유형 자체의 개념을 간략하게 검토하겠습니다.

함수 타입은 함수로 사용할 수 있는 객체를 나타내는 타입이다

예를 들어 필터 기능에서
요소가 허용될 수 있는지 여부를 결정하는 predicate 를 나타내기 위해 존재합니다.

inline fun <T> Iterable<T>.filter(
    predicate: (T) -> Boolean
 ): List<T> {
     val list = arrayListOf<T>()
     for (elem in this) {
         if (predicate(elem)) {
             list.add(elem)
         }
      }
      return list
 }

 다음은 함수 유형의 몇 가지 예입니다.
 • ()->Unit - Function with no arguments and returns Unit.
• (Int)->Unit - Function that takes Int and returns Unit.
• (Int)->Int - Function that takes Int and returns Int.
• (Int, Int)->Int - Function that takes two arguments of type Int and returns Int.
• (Int)->()->Unit - Function that takes Int and returns another function. This other function has no arguments and returns Unit.
• (()->Unit)->Unit - Function that takes another function and returns Unit. This other function has no arguments and returns Unit.


함수 유형의 인스턴스를 만드는 기본 방법은 다음과 같습니다.
 • 람다 식 사용
 • 익명 함수 사용
 • 함수 참조 사용

예를 들어, 다음 함수를 생각해 보십시오.
fun plus(a: Int, b: Int) = a + b
유사한 함수는 다음과 같은 방법으로 만들 수 있습니다.
val plus1: (Int, Int)->Int = { a, b -> a + b }   // 람다식
val plus2: (Int, Int)->Int = fun(a, b) = a + b   // 익명함수
val plus3: (Int, Int)->Int = ::plus              // 함수참조 사용

위의 예에서
속성 유형이 지정되므로 람다 식과 익명 함수의 인수 유형을 유추할 수 있습니다.
그 반대일 수도 있습니다. 인수 유형을 지정하면 함수 유형을 유추할 수 있습니다.

val plus4 = { a: Int, b: Int -> a + b }
val plus5 = fun(a: Int, b: Int) = a + b

함수 유형은 함수를 나타내는 객체를 나타냅니다.

익명 함수는 심지어 정상적인 함수처럼 보이는데, 이름은 없습니다.
람다 식은 익명 함수에 대한 짧은 표기법입니다.

함수를 나타내는 함수 타입이 있음에도
확장 함수는 대체 뭐일까요?
우리가 그것들을 잘 표현할 수 있을까요?

fun Int.myPlus(other: Int) = this + other
앞에서 일반 함수와 같은 방식으로 이름을 지정하지 않고 익명 함수를 생성한다고 언급했습니다.
따라서 익명 확장 함수는 동일한 방식으로 정의됩니다.

val myPlus = fun Int.(other: Int) = this + other
어떤 유형이 있습니까? 대답은 확장 함수를 나타내는 특별한 타입이 있다는 것입니다.
이를 리시버가 있는 함수 타입이라고 합니다.
일반 함수 유형과 비슷해 보이지만 추가로 인수 앞에 receiver type을 지정하고 dot(.)을 사용하여 구분합니다.

val myPlus: Int.(Int)->Int = fun Int.(other: Int) = this + other
이러한 함수는 람다 식을 사용하여 정의할 수 있습니다.
특히 리시버가 포함된 람다 표현식이며,
해당 범위 this 키워드는 확장 리시버를 참조합니다.
(이 경우 Int 유형의 인스턴스):

val myPlus: Int.(Int)->Int = { this + it }
익명 확장 함수 또는 리시버가 있는 람다를 사용하여 생성된 객체는 3가지 방법으로 호출할 수 있습니다.
• 표준 객체와 마찬가지로 invoke 메소드를 사용합니다.  myPlus.invoke(1, 2)
• 확장 함수 아닌 것처럼          myPlus(1, 2)
• 일반 확장 함수와 동일하게.      1.myPlus(2)

리시버가 있는 함수 유형의 가장 중요한 특성은 "this" 가 참조하는 것을 변경한다는 것입니다.
apply 함수 내의 인스턴스로 사용되는 this 는 리시버 객체의 메서드와 프로퍼티에 더 쉽게 접근하고 참조할 수 있게 도와줍니다

inline fun <T> T.apply(block: T.() -> Unit): T {
    this.block()
    return this
}

class User {
    var name: String = ""
    var surname: String = ""
}

val user = User().apply {
    name = "Marcin"
    surname = "Moskała"
}

리시버가 있는 함수 유형은 Kotlin DSL의 가장 기본적인 구성 요소입니다.
아래 HTML 테이블을 만들 수 있게 해주는 아주 간단한 DSL을 만들어 봅시다.
fun createTable(): TableDsl = table {
     tr {
        for (i in 1..2) {
            td {
                +"This is column $i"
            }
        }
     }
}
이 DSL의 처음부터 table 이란 함수를 볼 수 있습니다.
우리는 어떤 리시버도 없이 최상위 레벨에 있으므로 최상위 함수여야 합니다.
비록 함수 내부에 인자로 우리가 tr 을 사용하는 것을 볼 수 있지만, tr 함수는 테이블 정의 내에서만 허용되어야 합니다.
이것이 table 함수 인수에 그러한 함수가 있는 리시버를 가지고 있어야 하는 이유입니다.
마찬가지로 tr 함수 인수에는 td 함수를 포함할 리시버가 있어야 합니다.

fun table(init: TableBuilder.()->Unit): TableBuilder { ... }

class TableBuilder {
    fun tr(init: TrBuilder.() -> Unit) { ... }
}

class TrBuilder {
    fun td(init: TdBuilder.()->Unit) { ... }
}

class TdBuilder

아래 state 는 어떻습니까?
+"This is row $i"

이게 뭘까요? 이것은 String 의 단항 더하기 연산자에 불과합니다.
이것은 TdBuilder 내에서 정의해야 합니다.

class TdBuilder {
    var text = ""
    operator fun String.unaryPlus() {
        text += this
    }
}

이제 DSL이 잘 정의되었습니다.
잘 작동하도록 사용하려면 매 단계에서 빌더를 생성하고 매개변수의 함수(아래 예에서 init)을 사용하여 초기화해야 합니다.
그런 다음, 빌더는 이 초기화 함수 인수에 지정된 모든 데이터를 포함할 것입니다

이것이 우리가 필요한 데이터입니다.

따라서 이 빌더를 반환하거나 이 데이터를 보유하는 다른 개체를 생성할 수 있습니다.
이 예에서는 빌더만 반환합니다.
다음은 테이블 함수를 정의하는 방법입니다.

fun table(init: TableBuilder.()->Unit): TableBuilder {
    val tableBuilder = TableBuilder()
    init.invoke(tableBuilder)
    return tableBuilder
}

앞서 보여준 것처럼 apply 함수를 사용해서 이 함수를 더 짧게 할 수 있습니다.
fun table(init: TableBuilder.()->Unit) = TableBuilder().apply(init)

마찬가지로 이 DSL의 다른 부분에서 더 간결하게 사용할 수 있습니다.
class TableBuilder {
    var trs = listOf<TrBuilder>()
    fun td(init: TrBuilder.()->Unit) {
        trs = trs + TrBuilder().apply(init)
    }
}

class TrBuilder {
    var tds = listOf<TdBuilder>()
    fun td(init: TdBuilder.()->Unit) {
        tds = tds + TdBuilder().apply(init)
    }
}

이것이 HTML 테이블 생성을 위한 완전한 기능의 DSL 빌더입니다.
Item 15에서 설명했던 DslMarker 를 사용하여 개선할 수 있습니다.
리시버를 명시적으로 참조하는 것을 고려하십시오.

When should we use it?

DSL은 정보를 정의하는 방법을 제공합니다.
당신이 원하는 어떤 종류의 정보든지 표현하는 데 사용할 수 있지만
그러나 이 정보가 나중에 어떻게 사용될지는 사용자에게 명확하지 않습니다.

Anko, TornadoFX 또는 HTML DSL에서 우리는 우리가 정의한 것을 기반으로 View가 정확하게 구축될 것이라고 믿는다.
그러나 그게 얼마나 정확하게 되었는지는 알기 어렵습니다.

좀 더 복잡한 사용에서는 발견하기가 어려울 수 있습니다.
익숙하지 않은 사람들에게는 사용법이 혼란스러울 수도 있습니다.
유지 보수는 말할 것도 없습니다.
그것들을 정의하는 방법은 개발자 혼란과 성능 측면 모두에서 비용이 될 수 있습니다.

DSL은 다른 간단한 기능을 대신 사용할 수 있는 경우에는 오히려 과잉입니다.

다음과 같이 표현해야 할 때 정말 유용합니다.
• 복잡한 데이터 구조,
• 계층 구조,
• 엄청난 양의 데이터.

모든 것들이 빌더나 단지 생성자를 사용해서 DSL 같은 구조 없이 표현될 수 있습니다.

DSL은 이러한 구조에 대한 bolierplate 제거에 관한 것입니다.

당신이 반복적인 boilerplate 코드를 보고 더 간단한 코틀린 features 들로 해결할 수 없을 때,
DSL사용을 고려해봐야합니다.


[Summary]

DSL은 언어 내부의 특수한 언어입니다.
복잡한 객체는 물론 HTML 코드나 복잡한 구성 파일과 같은 전체 객체 계층 구조를 매우 간단하게 만들 수 있습니다.

반면에 DSL 구현은 새로운 개발자에게는 혼란스럽거나 어려울 수 있습니다.

(DSL)그것들은 또한 정의하기가 어렵습니다.
이것이 우리가 DSL이 실제 가치를 제공할 때만 사용해야 하는 이유입니다.

예를 들어 정말 복잡한 객체를 생성하거나
복잡한 객체 계층 구조에 가능합니다.

그렇기 때문에 프로젝트보다는 라이브러리에서 정의하는 것이 바람직합니다.
좋은 DSL을 만드는 것은 쉽지 않지만 잘 정의된 DSL은 우리 프로젝트를 훨씬 더 좋게 만들 수 있습니다.

 */