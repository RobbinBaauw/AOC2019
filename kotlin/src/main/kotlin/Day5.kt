import java.util.*
import kotlin.collections.ArrayList

class Day5 {
    companion object {

        fun solveDay5Part1(input: List<Long>) {
            IntCodeComputer(input).run(ArrayList(), null)
        }

        fun solveDay5Part2(input: List<Long>) {
            solveDay5Part1(input)
        }
    }
}

fun main() {

    val input = Scanner(Day5::class.java.getResourceAsStream("Day5Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

    Day5.solveDay5Part1(inputList)
    Day5.solveDay5Part2(inputList)
}
