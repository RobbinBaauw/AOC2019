import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

data class GridPoint(
    var x: Int,
    var y: Int,
    var type: PointType
) {
    var leftPoint: GridPoint? = null
    var rightPoint: GridPoint? = null
    var topPoint: GridPoint? = null
    var bottomPoint: GridPoint? = null
}

data class Path(
    val length: Int,
    val startingTurn: Direction
) {
    override fun toString(): String {
        val dirString = when (startingTurn) {
            Direction.LEFT -> "L"
            Direction.RIGHT -> "R"
            else -> throw Exception()
        }

        return "$dirString,$length"
    }
}

enum class PointType {
    SCAFFOLD,
    OPEN,
    UP,
    RIGHT,
    DOWN,
    LEFT
}

class Day17(
    val input: List<Long>
) {
    fun solveDay17Part1(): Int {
        val intCodeComputer = IntCodeComputer(input)
        val outputHandler = OutputHandler(mutableListOf()) {
            false
        }

        intCodeComputer.run(mutableListOf(), outputHandler)
        val grid = parseGrid(outputHandler.outputList)
        draw(grid)

        val scaffolds = grid.filter { it.type == PointType.SCAFFOLD }
        val intersections = scaffolds
            .filter {
                scaffolds.any { gr -> gr.x == it.x && gr.y == it.y - 1 }
                        && scaffolds.any { gr -> gr.x == it.x && gr.y == it.y + 1 }
                        && scaffolds.any { gr -> gr.y == it.y && gr.x == it.x - 1 }
                        && scaffolds.any { gr -> gr.y == it.y && gr.x == it.x + 1 }
            }

        return intersections.map { it.x * it.y }.sum()
    }

    fun solveDay17Part2(): Long {
        val intCodeComputer = IntCodeComputer(input)
        val outputHandler = OutputHandler(mutableListOf()) {
            false
        }

        intCodeComputer.run(mutableListOf(), outputHandler)
        val grid = parseGrid(outputHandler.outputList)
        draw(grid)

        val paths: MutableList<Path> = ArrayList()
        val takenPaths: MutableList<Int> = ArrayList()

        var currentPoint: GridPoint = grid.first { it.type == PointType.LEFT || it.type == PointType.RIGHT || it.type == PointType.UP || it.type == PointType.DOWN }
        var currDirection = when (currentPoint.type) {
            PointType.DOWN -> Direction.BOTTOM
            PointType.RIGHT -> Direction.RIGHT
            PointType.UP -> Direction.TOP
            PointType.LEFT -> Direction.LEFT
            else -> throw Exception()
        }

        loop1@ while (true) {
            val leftPoint = currentPoint.leftPoint
            val rightPoint = currentPoint.rightPoint
            val topPoint = currentPoint.topPoint
            val bottomPoint = currentPoint.bottomPoint

            val currRotation = when {
                leftPoint != null -> Direction.LEFT
                rightPoint != null -> Direction.RIGHT
                topPoint != null -> Direction.TOP
                bottomPoint != null -> Direction.BOTTOM
                else -> break@loop1
            }

            var pathLength = 0
            loop@ while (true) {

                when (currRotation) {
                    Direction.BOTTOM -> currentPoint.topPoint = null
                    Direction.TOP -> currentPoint.bottomPoint = null
                    Direction.LEFT -> currentPoint.rightPoint = null
                    Direction.RIGHT -> currentPoint.leftPoint = null
                }

                currentPoint = when (currRotation) {
                    Direction.BOTTOM -> currentPoint.bottomPoint ?: break@loop
                    Direction.TOP -> currentPoint.topPoint ?: break@loop
                    Direction.LEFT -> currentPoint.leftPoint ?: break@loop
                    Direction.RIGHT -> currentPoint.rightPoint ?: break@loop
                }

                pathLength++
            }

            val newOrExistingPath = Path(pathLength, requiredDirection(currDirection, currRotation))
            val indexOf = paths.indexOf(newOrExistingPath)
            if (indexOf != -1) {
                takenPaths.add(indexOf)
            } else {
                paths.add(newOrExistingPath)
                takenPaths.add(paths.size - 1)
            }

            currDirection = currRotation
        }

        val pathString = takenPaths.joinToString("")
        val pathIndices = getPathFragments(pathString, mutableListOf()) ?: throw Exception()

        val pathAIndicesJoined = pathIndices[0]
        val pathBIndicesJoined = pathIndices[1]
        val pathCIndicesJoined = pathIndices[2]

        val pathA = pathAIndicesJoined.toCharArray().map { Character.getNumericValue(it) }.joinToString(",") { paths[it].toString() }
        val pathB = pathBIndicesJoined.toCharArray().map { Character.getNumericValue(it) }.joinToString(",") { paths[it].toString() }
        val pathC = pathCIndicesJoined.toCharArray().map { Character.getNumericValue(it) }.joinToString(",") { paths[it].toString() }

        val finalPath: MutableList<String> = ArrayList()
        var pathsToTake = takenPaths.joinToString("")
        while (pathsToTake.isNotBlank()) {
            pathsToTake = when {
                pathsToTake.startsWith(pathAIndicesJoined) -> {
                    finalPath.add("A")
                    pathsToTake.removePrefix(pathAIndicesJoined)
                }
                pathsToTake.startsWith(pathBIndicesJoined) -> {
                    finalPath.add("B")
                    pathsToTake.removePrefix(pathBIndicesJoined)
                }
                else -> {
                    finalPath.add("C")
                    pathsToTake.removePrefix(pathCIndicesJoined)
                }
            }
        }

        val pathStrings = mutableListOf(finalPath.joinToString(","), pathA, pathB, pathC, "n", "")

        val changedInput = input.toMutableList()
        changedInput[0] = 2L
        val intCodeComputer2 = IntCodeComputer(changedInput)
        val outputHandler2 = OutputHandler(mutableListOf()) {
            false
        }

        intCodeComputer2.run(pathStrings.joinToString("\n").map { it.toLong() }.toMutableList(), outputHandler2)
        return outputHandler2.outputList.last()
    }

    private fun getPathFragments(currPath: String, previousFragments: List<String>): List<String>? {
        for (fragmentIndex in 0..4) {
            var copyPathString = currPath
            val newFragment = currPath.substring(0..fragmentIndex)

            copyPathString = copyPathString.removePrefix(newFragment)

            val newFragments = previousFragments.toMutableList()
            newFragments.add(newFragment)

            var startsWith = newFragments.firstOrNull { copyPathString.startsWith(it) }
            while (startsWith != null) {
                copyPathString = copyPathString.removePrefix(startsWith)
                startsWith = newFragments.firstOrNull { copyPathString.startsWith(it) }
            }

            if (newFragments.size == 3) {
                if (copyPathString.isBlank()) {
                    return newFragments
                }
            } else {
                val childResult = getPathFragments(copyPathString, newFragments)
                if (childResult != null) {
                    return childResult
                }
            }
        }

        return null
    }

    private fun requiredDirection(direction: Direction, newDirection: Direction): Direction {
        return when (direction) {
            Direction.TOP -> if (newDirection == Direction.LEFT) Direction.LEFT else Direction.RIGHT
            Direction.RIGHT -> if (newDirection == Direction.TOP) Direction.LEFT else Direction.RIGHT
            Direction.BOTTOM -> if (newDirection == Direction.RIGHT) Direction.LEFT else Direction.RIGHT
            Direction.LEFT -> if (newDirection == Direction.BOTTOM) Direction.LEFT else Direction.RIGHT
        }
    }

    private fun parseGrid(outputList: MutableList<Long>): MutableSet<GridPoint> {
        val grid: MutableSet<GridPoint> = HashSet()

        var x = 0
        var y = 0
        for (point in outputList) {
            if (point == 10L) {
                y++
                x = 0
                continue
            }

            grid.add(GridPoint(x, y, when (point) {
                35L -> PointType.SCAFFOLD
                46L -> PointType.OPEN
                60L -> PointType.LEFT
                62L -> PointType.RIGHT
                86L -> PointType.DOWN
                94L -> PointType.UP
                else -> throw Exception()
            }))

            x++
        }

        val scaffolds = grid.filter { it.type != PointType.OPEN }
        for (point in scaffolds) {
            point.bottomPoint = scaffolds.firstOrNull { gr -> gr.x == point.x && gr.y == point.y + 1 }
            point.topPoint = scaffolds.firstOrNull { gr -> gr.x == point.x && gr.y == point.y - 1 }
            point.leftPoint = scaffolds.firstOrNull { gr -> gr.y == point.y && gr.x == point.x - 1 }
            point.rightPoint = scaffolds.firstOrNull { gr -> gr.y == point.y && gr.x == point.x + 1 }
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
                            PointType.SCAFFOLD -> "#"
                            PointType.OPEN -> "."
                            PointType.UP -> "^"
                            PointType.DOWN -> "v"
                            PointType.RIGHT -> ">"
                            PointType.LEFT -> "<"
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

    val input = Scanner(Day17::class.java.getResourceAsStream("Day17Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

    println(Day17(inputList).solveDay17Part1())
    println(Day17(inputList).solveDay17Part2())
}
