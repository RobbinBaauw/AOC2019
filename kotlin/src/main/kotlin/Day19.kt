import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class Day19(
    val input: List<Long>
) {
    data class GridPoint(
        var x: Long,
        var y: Long,
        var type: PointType
    )

    enum class PointType {
        STATIONARY,
        BEINGPULLED
    }

    fun solveDay19Part1(): Int {
        val grid = parseGrid()
        draw(grid)
        return grid.count { it.type == PointType.BEINGPULLED }
    }

    fun solveDay19Part2(): Long {
        var y = Pair(0L, false)
        var x = 0L

        val outputHandler = OutputHandler(mutableListOf()) {
            false
        }

        while (true) {
            IntCodeComputer(input).run(mutableListOf(x, y.first), outputHandler)
            if (outputHandler.outputList.last() == 1L) {
                IntCodeComputer(input).run(mutableListOf(x - 99, y.first + 99), outputHandler)
                y = y.copy(second = true)

                if (outputHandler.outputList.last() == 1L) {
                    return (x - 99) * 10000 + y.first
                }

                x++
            } else if (!y.second) {
                x++
            } else {
                y = Pair(y.first + 1, false)
            }

        }

    }

    private fun parseGrid(): MutableSet<GridPoint> {
        val grid: MutableSet<GridPoint> = HashSet()

        val outputHandler = OutputHandler(mutableListOf()) {
            false
        }

        for (y in 0..49L) {
            for (x in 0..49L) {
                IntCodeComputer(input).run(mutableListOf(x, y), outputHandler)
                grid.add(GridPoint(x, y, when (outputHandler.outputList.last()) {
                    0L -> PointType.STATIONARY
                    1L -> PointType.BEINGPULLED
                    else -> throw Exception()
                }))
            }
        }

        return grid
    }

    private fun draw(traversedCoords: MutableSet<GridPoint>) {
        val maxX = traversedCoords.maxBy { it.x }?.x?:0
        val minX = traversedCoords.minBy { it.x }?.x?:0

        val maxY = traversedCoords.maxBy { it.y }?.y?:0
        val minY = traversedCoords.minBy { it.y }?.y?:0

        println("----------------------------")
        for (y in minY..maxY) {
            print("\n")

            for (x in minX..maxX) {
                traversedCoords
                    .firstOrNull { it.x == x && it.y == y }
                    ?.let {
                        print(when (it.type) {
                            PointType.BEINGPULLED -> "#"
                            PointType.STATIONARY -> "."
                        })
                    }
                    ?.run { print(" ") }
            }
        }
        println("")
        println("----------------------------")
    }
}

fun main() {

    val input = Scanner(Day19::class.java.getResourceAsStream("Day19Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

    println(Day19(inputList).solveDay19Part1())
    println(Day19(inputList).solveDay19Part2())
}
