import java.util.*
import kotlin.collections.ArrayList


class Day7 {
    companion object {
        fun solveDay7Part1(input: List<Long>) {

            val permutations = mutableListOf<MutableList<Long>>()
            getPermutations(5, mutableListOf(0, 1, 2, 3, 4), permutations)

            val outputList = ArrayList<Long>()
            var largestSignal = 0L

            permutations.forEach {
                it.add(1, 0)
                for (i in 0..4) {
                    IntCodeComputer(input).run(it, outputList)
                    val lastOutput = outputList.last()
                    if (i == 4) {
                        if (largestSignal < lastOutput) {
                            largestSignal = lastOutput
                        }
                    } else {
                        it.add(1, lastOutput)
                    }

                }
            }

            println(largestSignal)
        }

        fun solveDay7Part2(input: List<Long>) {

            val permutations = mutableListOf<MutableList<Long>>()
            getPermutations(5, mutableListOf(5, 6, 7, 8, 9), permutations)

            val outputList = ArrayList<Long>()
            var largestSignal = 0L

            for (permutation in permutations) {
                val permutationCopy = permutation.toMutableList()

                val intCodeComputers = (0..4).map {
                    IntCodeComputer(input)
                }.toList()

                permutation.add(1, 0)
                loop@ while (true) {
                    for (i in 0..4) {
                        val stopped = intCodeComputers[i].run(permutation, outputList)

                        val lastOutput = outputList.last()
                        if (stopped) {
                            if (largestSignal < lastOutput) {
                                largestSignal = lastOutput
                            }
                            break@loop
                        }

                        if (permutation.isEmpty()) {
                            permutation.add(lastOutput)
                        } else {
                            permutation.add(1, lastOutput)
                        }
                    }
                }
            }

            println(largestSignal)
        }


        private fun getPermutations(
            n: Int,
            inputElements: MutableList<Long>,
            outputList: MutableList<MutableList<Long>>
        ) {
            if (n == 1) {
                outputList.add(ArrayList(inputElements))
            } else {
                getPermutations(n - 1, inputElements, outputList)
                for (i in 0 until n - 1) {
                    if (n % 2 == 0) {
                        Collections.swap(inputElements, i, n - 1)
                    } else {
                        Collections.swap(inputElements, 0, n - 1)
                    }
                    getPermutations(n - 1, inputElements, outputList)
                }
            }
        }
    }
}

fun main() {

    val input = Scanner(Day7::class.java.getResourceAsStream("Day7Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

//    Day7.solveDay7Part1(inputList)
    Day7.solveDay7Part2(inputList)
}
