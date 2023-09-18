package ch2_Readability

class CounterSet<T>(private val innerSet: MutableSet<T> = mutableSetOf<T>()) : MutableSet<T> by innerSet {
    var elementsAdded: Int = 0
        private set

    override fun add(element: T): Boolean {
        elementsAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elementsAdded += elements.size
        return innerSet.addAll(elements)
    }
}

class MutableLazyHolder<T : Any>(val initializer: () -> T) {
    private var value: Any = Any()
    private var initialized = false

    fun get(): T {
        if (!initialized) {
            value = initializer()
            initialized = true
        }
        return value as T
    }

    fun setValue() {
        this.value = value
        initialized = true
    }
}

class User(
    val name: String,
    val surname: String,
    val age: Int
)

fun main() {
    println("test")
    println(CounterSet<Int>().add(1))
    println(1)
}