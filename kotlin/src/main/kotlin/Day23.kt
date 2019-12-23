import java.util.*
import kotlin.collections.ArrayList

class Day23(
    val input: List<Long>
) {
    fun solveDay23Part1(): Long {
        return solve(true)
    }

    fun solveDay23Part2(): Long {
        return solve(false)
    }

    private fun solve(part1: Boolean): Long {
        val computers = createComputers()

        var xyNat = Pair(0L, 0L);

        val deliveredYValues: MutableList<Long> = ArrayList()

        while (true) {
            for (computer in computers) {
                val queueIsEmpty = computer.queue.isEmpty()
                if (!queueIsEmpty) {
                    computer.intCodeComputer.run(computer.queue, computer.outputHandler)
                } else {
                    computer.intCodeComputer.run(mutableListOf(-1), computer.outputHandler)
                }

                val outputList = computer.outputHandler.outputList

                if (outputList.isEmpty() && queueIsEmpty) {
                    computer.hasBeenIdleFor++
                } else {
                    computer.hasBeenIdleFor = 0
                }

                for (o in outputList.indices step 3) {
                    val receiver = outputList[o]
                    val x = outputList[o + 1]
                    val y = outputList[o + 2]

                    if (receiver == 255L) {
                        if (part1) {
                            return y
                        } else {
                            xyNat = Pair(x, y)
                        }
                    } else {
                        computers[receiver.toInt()].queue.addAll(listOf(x, y))
                    }
                }
                outputList.clear()
            }

            if (!part1 && computers.all { it.hasBeenIdleFor > 1000 }) {
                if (deliveredYValues.size > 0 && deliveredYValues.last() == xyNat.second) {
                    return xyNat.second
                }

                deliveredYValues.add(xyNat.second)
                computers[0].queue.addAll(listOf(xyNat.first, xyNat.second))
            }
        }

    }

    class Computer(val intCodeComputer: IntCodeComputer, val queue: MutableList<Long>, val outputHandler: OutputHandler, var hasBeenIdleFor: Int = 0)

    private fun createComputers(): List<Computer> {
        return (0..49L).map {
            val outputHandler = OutputHandler(mutableListOf()) {
                false
            }

            val intCodeComputer = IntCodeComputer(input)
            Computer(intCodeComputer, mutableListOf(it), outputHandler)
        }
    }
}

fun main() {

    val input = Scanner(Day23::class.java.getResourceAsStream("Day23Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

    println(Day23(inputList).solveDay23Part1())
    println(Day23(inputList).solveDay23Part2())
}
