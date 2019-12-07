import java.util.*
import kotlin.collections.ArrayList

class Day5 {
    companion object {

        fun solveDay5Part1(input: List<Int>) {
            IntCodeComputer(input).run(ArrayList(), null)
        }

        fun solveDay5Part2(input: List<Int>) {
            solveDay5Part1(input)
        }
    }
}

fun main() {

    val input = Scanner(Day5::class.java.getResourceAsStream("Day5Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Int> = ArrayList()
    while (input.hasNextInt()) {
        inputList.add(input.nextInt())
    }

    Day5.solveDay5Part1(inputList)
    Day5.solveDay5Part2(inputList)
}
