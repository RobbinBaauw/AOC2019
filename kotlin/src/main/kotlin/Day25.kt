import java.util.*
import kotlin.collections.ArrayList

class Day25(
    val input: List<Long>
) {
    fun solveDay25Part1() {

        val possibleItems = listOf("take fixed point", "take food ration", "take astronaut ice cream", "take polygon", "take easter egg")

        for (amt in 1..6) {
            val permutationsOutput = mutableListOf<List<String>>()
            val permutations = listOf<String>()
            getPermutationsOfSize(possibleItems, permutations, permutationsOutput, possibleItems.size, amt)

            for (item in permutationsOutput) {

                var asciiInput =
                    """
west
south
east
south
north
west
south
south
north
north
north
west
west
west
take dark matter
east
south0
west1
east
north
east
south2
west
east
south3
east4
north
south
east
take weather machine
north
north

        """.trimIndent()

                for (currItem in item) {
                    val index = possibleItems.indexOf(currItem)
                    asciiInput = asciiInput.replace("$index", "\n$currItem")
                }

                asciiInput = asciiInput.replace(Regex("[0-9]"), "")
                val output = solve(asciiInput)

                if (!output.contains("heavier") && !output.contains("lighter")) {
                    println(output)
                    println("Items were $item")
                    return
                }
            }
        }

    }

    private fun getPermutationsOfSize(
        set: List<String>,
        prefix: List<String>,
        output: MutableList<List<String>>,
        n: Int, k: Int
    ) {
        if (k == 0) {
            output.add(prefix)
            return
        }

        for (i in 0 until n) {
            val newPrefix = prefix.toMutableList()
            newPrefix.add(set[i])

            getPermutationsOfSize(
                set, newPrefix,
                output,
                n, k - 1
            )
        }
    }

    private fun solve(asciiInput: String): String {
        val outputHandler = OutputHandler(mutableListOf()) {
            false
        }

        val arguments = asciiInput.map { it.toLong() }.toMutableList()
        IntCodeComputer(input).run(arguments, outputHandler)

        val output = outputHandler.outputList
        return output.map { it.toChar() }.joinToString("")
    }
}

fun main() {

    val input = Scanner(Day25::class.java.getResourceAsStream("Day25Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

    Day25(inputList).solveDay25Part1()
}
