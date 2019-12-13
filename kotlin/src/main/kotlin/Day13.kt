import java.util.*
import kotlin.collections.ArrayList

enum class Type {
    EMPTY,
    WALL,
    BLOCK,
    PADDLE,
    BALL
}

class Tile(
    var type: Type
)

class Day13 {
    companion object {
        fun solveDay13Part1(input: List<Long>): Int {
            val intCodeComputer = IntCodeComputer(input)
            val output: MutableList<Long> = ArrayList()
            val outputHandler = OutputHandler(output) {
                output.size == 3
            }

            val grid = Array(1000) {
                Array(1000) {
                    Tile(Type.EMPTY)
                }
            }

            while (true) {
                val halted = intCodeComputer.run(mutableListOf(), outputHandler)
                if (halted) break

                val x = output[0].toInt()
                val y = output[1].toInt()
                val type = when (output[2]) {
                    0L -> Type.EMPTY
                    1L -> Type.WALL
                    2L -> Type.BLOCK
                    3L -> Type.PADDLE
                    4L -> Type.BALL
                    else -> throw Exception()
                }

                grid[y][x].type = type

                output.clear()
            }

            return grid.sumBy { it.count { tile -> tile.type == Type.BLOCK } }
        }

        fun solveDay13Part2(input: List<Long>): Long {
            val toMutableList = input.toMutableList()
            toMutableList[0] = 2L
            val intCodeComputer = IntCodeComputer(toMutableList)
            val output: MutableList<Long> = ArrayList()
            val outputHandler = OutputHandler(output) {
                false
            }

            val grid = Array(1000) {
                Array(1000) {
                    Tile(Type.EMPTY)
                }
            }

            var score = 0L

            intCodeComputer.run(mutableListOf(), outputHandler)
            score = update(grid, outputHandler.outputList)

            while (true) {

                var ballX = 0
                var paddleX = 0

                grid.forEachIndexed { y, yTiles ->
                    yTiles.forEachIndexed { x, xTiles ->
                        if (xTiles.type == Type.BALL) {
                            ballX = x
                        } else if (xTiles.type == Type.PADDLE) {
                            paddleX = x
                        }
                    }
                }

                val inputNr = when {
                    ballX > paddleX -> {
                        1L
                    }
                    ballX < paddleX -> {
                        -1L
                    }
                    else -> {
                        0L
                    }
                }

                val halted = intCodeComputer.run(mutableListOf(inputNr), outputHandler)
                val newScore = update(grid, outputHandler.outputList)
                if (newScore > score) {
                    score = newScore
                }

                if (halted) {
                    break
                }
            }

            return score
        }

        private fun update(grid: Array<Array<Tile>>, output: MutableList<Long>): Long {

            var score = 0L
            for (i in output.indices step 3) {
                val x = output[i].toInt()
                val y = output[i + 1].toInt()
                if (x == -1 && y == 0) {
                    score = output[i + 2]
                } else {
                    val type = when (output[i + 2]) {
                        0L -> Type.EMPTY
                        1L -> Type.WALL
                        2L -> Type.BLOCK
                        3L -> Type.PADDLE
                        4L -> Type.BALL
                        else -> throw Exception()
                    }

                    grid[y][x].type = type
                }

            }

            output.clear()
            return score
        }
    }

}

fun main() {

    val input = Scanner(Day13::class.java.getResourceAsStream("Day13Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

//    println(Day13.solveDay13Part1(inputList))
    println(Day13.solveDay13Part2(inputList))
}
