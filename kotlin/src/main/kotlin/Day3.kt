import java.util.*
import kotlin.math.abs

class Day3 {
    companion object {
        fun solveDay3Part1(wire1Coords: List<Pair<Int, Int>>, wire2Coords: List<Pair<Int, Int>>): Int {

            var smallestDistance = Int.MAX_VALUE

            wire1Coords.forEachIndexed { indexWire1, wire1Coord ->
                wire2Coords.forEachIndexed { indexWire2, wire2Coord ->
                    if (wire1Coord == wire2Coord) {
                        val currDist = abs(wire1Coord.first) +
                                abs(wire1Coord.second)

                        if (currDist < smallestDistance) {
                            smallestDistance = currDist
                        }
                    }
                }

                println(indexWire1)
            }

            return smallestDistance
        }

        fun solveDay3Part2(wire1Coords: List<Pair<Int, Int>>, wire2Coords: List<Pair<Int, Int>>): Int {
            wire1Coords.forEachIndexed { indexWire1, wire1Coord ->
                wire2Coords.forEachIndexed { indexWire2, wire2Coord ->
                    if (wire1Coord == wire2Coord) {
                        return indexWire1 + indexWire2 + 2
                    }
                }

                println(indexWire1)
            }

            return -1
        }
    }
}

fun main() {

    val input = Scanner(Day3::class.java.getResourceAsStream("Day3Input"))

    val line1 = input.nextLine().split(",")
    val line2 = input.nextLine().split(",")

//    println(Day3.solveDay3Part1(parseToCoordinates(line1), parseToCoordinates(line2)))
    println(Day3.solveDay3Part2(parseToCoordinates(line1), parseToCoordinates(line2)))
}

fun parseToCoordinates(input: List<String>): List<Pair<Int, Int>> {
    val coords: MutableList<Pair<Int, Int>> = ArrayList()

    var previousCoords = Pair(0, 0)
    input.forEach { currInput ->

        val amount = currInput.substring(1).toInt()

        repeat(amount) {
            when {
                currInput.startsWith("R")-> previousCoords = previousCoords.copy(first = previousCoords.first + 1)
                currInput.startsWith("L")-> previousCoords = previousCoords.copy(first = previousCoords.first - 1)
                currInput.startsWith("U")-> previousCoords = previousCoords.copy(second = previousCoords.second + 1)
                currInput.startsWith("D")-> previousCoords = previousCoords.copy(second = previousCoords.second - 1)
            }

            coords.add(previousCoords)
        }
    }

    return coords
}
