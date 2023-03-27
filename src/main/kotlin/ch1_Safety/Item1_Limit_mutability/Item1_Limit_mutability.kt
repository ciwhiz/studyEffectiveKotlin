package ch1_Safety.Item1_Limit_mutability

fun main() {
    val account = Chapter1.BackAccount()
    println(account.balance)
    account.deposit(100.0)
    println(account.balance)
    account.withdraw(50.0)
    println(account.balance)
}

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

