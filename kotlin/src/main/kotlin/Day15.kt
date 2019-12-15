import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

data class Coord(
    var x: Int,
    var y: Int
) {
    var response: Response? = null
}

enum class Response {
    WALL,
    MOVED,
    OXYGEN
}

class Day15(
    val input: List<Long>
) {

    var oxygenIntCodeComputer: IntCodeComputer? = null

    fun solveDay15Part1(): MutableSet<Coord> {
        val traversedCoords: MutableSet<Coord> = HashSet()

        val zero = Coord(0, 0)

        val coordQueue: Queue<Triple<IntCodeComputer, Direction, Coord>> = LinkedList()
        var depth = 0

        addToQueue(zero, IntCodeComputer(input), coordQueue)

        while (coordQueue.isNotEmpty()) {
            traverseCoordQueue(coordQueue, traversedCoords)
            depth++
            draw(traversedCoords, depth)
        }

        return traversedCoords
    }

    fun solveDay15Part2() {
        val coords = solveDay15Part1()
        val traversedCoords: MutableSet<Coord> = HashSet()
        val oxygenCoord = coords.first { it.response == Response.OXYGEN }

        val coordQueue: Queue<Triple<IntCodeComputer, Direction, Coord>> = LinkedList()
        var depth = 0

        addToQueue(oxygenCoord, oxygenIntCodeComputer!!, coordQueue)
        traversedCoords.add(oxygenCoord)

        while (coordQueue.isNotEmpty()) {
            traverseCoordQueue(coordQueue, traversedCoords)
            draw(traversedCoords, depth)
            depth++
        }

    }

    private fun traverseCoordQueue(coordQueue: Queue<Triple<IntCodeComputer, Direction, Coord>>, traversedCoords: MutableSet<Coord>) {
        loop@ for (i in coordQueue.indices) {
            val currCoordDirection = coordQueue.poll()
            val computer = currCoordDirection.first
            val direction = currCoordDirection.second
            val coord = currCoordDirection.third

            if (!traversedCoords.contains(coord)) {

                traversedCoords.add(coord)

                val driveResponse = drive(direction, computer)
                coord.response = driveResponse
                when (driveResponse) {
                    Response.OXYGEN -> {
                        oxygenIntCodeComputer = computer
                        coordQueue.clear()
                        break@loop
                    }
                    Response.MOVED -> {
                        addToQueue(coord, computer, coordQueue)
                    }
                    Response.WALL -> {}
                }
            }
        }
    }

    private fun addToQueue(coord: Coord, intCodeComputer: IntCodeComputer, coordQueue: Queue<Triple<IntCodeComputer, Direction, Coord>>) {
        val northCoord = coord.copy(y = coord.y + 1)
        coordQueue.add(Triple(intCodeComputer.clone(), Direction.TOP, northCoord))

        val eastCoord = coord.copy(x = coord.x + 1)
        coordQueue.add(Triple(intCodeComputer.clone(), Direction.LEFT, eastCoord))

        val southCoord = coord.copy(y = coord.y - 1)
        coordQueue.add(Triple(intCodeComputer.clone(), Direction.BOTTOM, southCoord))

        val westCoord = coord.copy(x = coord.x - 1)
        coordQueue.add(Triple(intCodeComputer.clone(), Direction.RIGHT, westCoord))
    }

    private fun drive(direction: Direction, intCodeComputer: IntCodeComputer): Response {

        val directionNumber = when (direction) {
            Direction.LEFT -> 3L
            Direction.BOTTOM -> 2L
            Direction.RIGHT -> 4L
            Direction.TOP -> 1L
        }

        val outputHandler = OutputHandler(mutableListOf()) {
            false
        }

        intCodeComputer.run(mutableListOf(directionNumber), outputHandler)

        return when (outputHandler.outputList[0]) {
            0L -> Response.WALL
            1L -> Response.MOVED
            2L -> Response.OXYGEN
            else -> throw Exception()
        }
    }

    private fun draw(traversedCoords: MutableSet<Coord>, depth: Int) {
        val maxX = traversedCoords.maxBy { it.x }?.x?:0
        val minX = traversedCoords.minBy { it.x }?.x?:0

        val maxY = traversedCoords.maxBy { it.y }?.y?:0
        val minY = traversedCoords.minBy { it.y }?.y?:0

        println("----------------------------")
        println("Depth: $depth")
        for (y in minY..maxY) {
            print("\n")

            for (x in minX..maxX) {
                traversedCoords
                    .firstOrNull { it.x == x && it.y == y }
                    ?.let {
                        if (it.x == 0 && it.y == 0) {
                            print("S")
                        } else {
                            print(when (it.response) {
                                Response.MOVED -> "."
                                Response.WALL -> "#"
                                Response.OXYGEN -> "O"
                                else -> ""
                            })
                        }

                    }
                    ?.run { print(" ") }
            }
        }
        println("")
        println("----------------------------")
    }
}

fun main() {

    val input = Scanner(Day15::class.java.getResourceAsStream("Day15Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Long> = ArrayList()
    while (input.hasNextLong()) {
        inputList.add(input.nextLong())
    }

    println(Day15(inputList).solveDay15Part1())
    println(Day15(inputList).solveDay15Part2())
}
