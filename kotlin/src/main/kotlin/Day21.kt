import java.util.*
import kotlin.collections.ArrayList

class Day21(
    val input: List<Long>
) {
    fun solveDay21Part1() {
        // If there's nothing ahead of you
        // and there is something 4 places ahead of you, jump
        // If there's nothing 2 ahead of you
        // and there is something 4 places ahead of you, jump
        // If there's nothing 3 ahead of you
        // and there is something 4 places ahead of you, jump
        val asciiInput =
        """
NOT A J
AND D T
OR T J
NOT B T
AND D T
OR T J
NOT C T
AND D T
OR T J
WALK 

        """.trimIndent()

        solve(asciiInput)
    }

    fun solveDay21Part2() {

        // If there's nothing ahead of you
        // and there is something 4 places ahead of you, jump
        // If there's nothing 2 ahead of you
        // and there is something 4 places ahead of you, jump
        // If there's nothing 3 ahead of you
        // and there is something 4 places ahead of you, jump

        // If there isn't something 5 places ahead of you
        // Or 8 places ahead of you (so you can either walk 1 forward or jump directly again)
        // Don't jump
        val asciiInput =
            """
NOT A J
AND D T
OR T J
NOT B T
AND D T
OR T J
NOT C T
AND D T
OR T J
NOT E T
NOT T T
OR H T
AND T J
RUN 

        """.trimIndent()

        solve(asciiInput)

    }

    private fun solve(asciiInput: String) {
        val outputHandler = OutputHandler(mutableListOf()) {
            false
        }

        val arguments = asciiInput.map { it.toLong() }.toMutableList()
        IntCodeComputer(input).run(arguments, outputHandler)

        val output = outputHandler.outputList
        println(output.last())

    }
}

fun main() {

    val input = Scanner(Day21::class.java.getResourceAsStream("Day21Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

    Day21(inputList).solveDay21Part1()
    Day21(inputList).solveDay21Part2()
}
