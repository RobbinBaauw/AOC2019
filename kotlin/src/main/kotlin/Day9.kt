import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis


class Day9 {
    companion object {
        fun solveDay9Part1(input: List<Long>) {
            IntCodeComputer(input).run(mutableListOf(1), null)
        }

        fun solveDay9Part2(input: List<Long>) {
            IntCodeComputer(input).run(mutableListOf(2), null)
        }
    }
}

fun main() {

    val input = Scanner(Day9::class.java.getResourceAsStream("Day9Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

    Day9.solveDay9Part1(inputList)
    Day9.solveDay9Part2(inputList)
}
