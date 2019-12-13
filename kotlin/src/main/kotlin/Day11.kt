import java.util.*
import kotlin.collections.ArrayList

enum class Direction {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
}

enum class Color {
    BLACK,
    WHITE
}

class GridElement(
    var hasBeenPainted: Boolean,
    var color: Color
)

class Day11 {
    companion object {
        fun solveDay11Part1(input: List<Long>): Int {
            val intCodeComputer = IntCodeComputer(input)
            val output: MutableList<Long> = ArrayList()
            val outputHandler = OutputHandler(output) {
                output.size == 2
            }

            // false = black
            // also I'm lazy thus this large grid
            val grid = Array(1000) {
                Array(1000) {
                    GridElement(false, Color.BLACK)
                }
            }

            paintGrid(grid, intCodeComputer, outputHandler)

            return grid.sumBy {
                it.count { color -> color.hasBeenPainted }
            }
        }

        fun solveDay11Part2(input: List<Long>) {
            val intCodeComputer = IntCodeComputer(input)
            val output: MutableList<Long> = ArrayList()
            val outputHandler = OutputHandler(output) {
                output.size == 2
            }

            // false = black
            // also I'm lazy thus this large grid
            val grid = Array(1000) {
                Array(1000) {
                    GridElement(false, Color.BLACK)
                }
            }

            grid[500][500].color = Color.WHITE

            paintGrid(grid, intCodeComputer, outputHandler)

            for (y in grid) {
                val currLine = StringBuilder()
                for (x in y) {
                    currLine.append(if(x.color == Color.WHITE) "█" else " ")
                }
                println(currLine.toString())
            }
        }

        private fun paintGrid(grid: Array<Array<GridElement>>, intCodeComputer: IntCodeComputer, outputHandler: OutputHandler) {
            var currLocation = Pair(500, 500)
            var direction = Direction.TOP

            while (true) {
                val currGridValue = grid[currLocation.second][currLocation.first]
                val currColor = if (currGridValue.color == Color.WHITE) 1L else 0L

                val halted = intCodeComputer.run(mutableListOf(currColor), outputHandler)
                if (halted) break

                val newColor = outputHandler.outputList[0]
                grid[currLocation.second][currLocation.first].color = if (newColor == 0L) Color.BLACK else Color.WHITE
                grid[currLocation.second][currLocation.first].hasBeenPainted = true

                val newDirection = if (outputHandler.outputList[1] == 0L) Direction.LEFT else Direction.RIGHT
                direction = rotate(direction, newDirection)

                currLocation = when (direction) {
                    Direction.TOP -> currLocation.copy(second = currLocation.second + 1)
                    Direction.RIGHT -> currLocation.copy(first = currLocation.first + 1)
                    Direction.BOTTOM -> currLocation.copy(second = currLocation.second - 1)
                    Direction.LEFT -> currLocation.copy(first = currLocation.first - 1)
                }

                outputHandler.outputList.clear()
            }
        }

        private fun rotate(direction: Direction, newDirection: Direction): Direction {
            return when (direction) {
                Direction.TOP -> if (newDirection == Direction.LEFT) Direction.LEFT else Direction.RIGHT
                Direction.RIGHT -> if (newDirection == Direction.LEFT) Direction.TOP else Direction.BOTTOM
                Direction.BOTTOM -> if (newDirection == Direction.LEFT) Direction.RIGHT else Direction.LEFT
                Direction.LEFT -> if (newDirection == Direction.LEFT) Direction.BOTTOM else Direction.TOP
            }
        }
    }
}

fun main() {

    val input = Scanner(Day11::class.java.getResourceAsStream("Day11Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

    println(Day11.solveDay11Part1(inputList))
    Day11.solveDay11Part2(inputList)
}
