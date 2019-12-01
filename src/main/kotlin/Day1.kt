import java.util.*
import kotlin.collections.ArrayList

class Day1 {
    companion object {
        fun solveDay1Part1(input: List<Int>): Int {
            return input
                .map { (it / 3) - 2 }
                .sum()
        }

        fun solveDay1Part2(input: List<Int>): Int {
            if (input.isEmpty()) {
                return 0
            }

            val currentSum = input
                .map { (it / 3) - 2 }
                .filter { it > 0 }

            return currentSum.sum() + solveDay1Part2(currentSum)
        }
    }
}

fun main() {

    val input = Scanner(Day1::class.java.getResourceAsStream("Day1Input"))

    val inputList: MutableList<Int> = ArrayList()
    while (input.hasNextInt()) {
        inputList.add(input.nextInt())
    }

    println(Day1.solveDay1Part1(inputList))
    println(Day1.solveDay1Part2(inputList))
}
