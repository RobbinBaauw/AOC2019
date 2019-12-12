import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.abs
import kotlin.system.measureTimeMillis

data class Moon(
    var position: Triple<Int, Int, Int>,
    var velocity: Triple<Int, Int, Int> = Triple(0, 0, 0)
) {
    fun getTotalEnergy(): Int {
        return position.toList().map { abs(it) }.sum() * velocity.toList().map { abs(it) }.sum()
    }
}

class Day12 {
    companion object {
        fun solveDay12Part1(input: List<Moon>): Int {

            val permutations = getPermutations(input)
            for (i in 0..999) {
                update(permutations, input)
            }

            return input.map { it.getTotalEnergy() }.sum()
        }

        fun solveDay12Part2(input: List<Moon>): Long {
            val permutations = getPermutations(input)

            val x: MutableSet<List<Pair<Int, Int>>> = HashSet()
            val y: MutableSet<List<Pair<Int, Int>>> = HashSet()
            val z: MutableSet<List<Pair<Int, Int>>> = HashSet()

            var xStopped = false
            var xSteps = 0L

            var yStopped = false
            var ySteps = 0L

            var zStopped = false
            var zSteps = 0L

            var amountOfSteps = 0L
            while (true) {
                update(permutations, input)

                val newX = input.map { Pair(it.position.first, it.velocity.first) }
                val newY = input.map { Pair(it.position.second, it.velocity.second) }
                val newZ = input.map { Pair(it.position.third, it.velocity.third) }

                if (x.contains(newX) && !xStopped) {
                    xStopped = true
                    xSteps = amountOfSteps
                } else {
                    x.add(newX)
                }

                if (y.contains(newY) && !yStopped) {
                    yStopped = true
                    ySteps = amountOfSteps
                } else {
                    y.add(newY)
                }

                if (z.contains(newZ) && !zStopped) {
                    zStopped = true
                    zSteps = amountOfSteps
                } else {
                    z.add(newZ)
                }

                amountOfSteps++

                if (xStopped && yStopped && zStopped) {
                    return lcm(lcm(xSteps, ySteps), zSteps)
                }
            }
        }

        private fun gcd(a: Long, b: Long): Long {
            if (b == 0L) return a
            return gcd(b, a % b)
        }

        private fun lcm(a: Long, b: Long): Long {
            return abs(a * b) / gcd(a, b)
        }

        private fun getPermutations(input: List<Moon>): MutableSet<Pair<Moon, Moon>> {
            val permutations: MutableSet<Pair<Moon, Moon>> = HashSet()
            for (moon in input) {
                for (moon2 in input) {
                    if (moon != moon2) {
                        if (!permutations.contains(Pair(moon2, moon))) {
                            permutations.add(Pair(moon, moon2))
                        }
                    }
                }
            }
            return permutations
        }

        private fun update(permutations: MutableSet<Pair<Moon, Moon>>, input: List<Moon>) {
            for (permutation in permutations) {

                val moon = permutation.first
                val otherMoon = permutation.second

                val (x, y, z) = moon.position
                val (x2, y2, z2) = otherMoon.position

                if (x > x2) {
                    moon.velocity = moon.velocity.copy(first = moon.velocity.first - 1)
                    otherMoon.velocity = otherMoon.velocity.copy(first = otherMoon.velocity.first + 1)
                } else if (x < x2) {
                    moon.velocity = moon.velocity.copy(first = moon.velocity.first + 1)
                    otherMoon.velocity = otherMoon.velocity.copy(first = otherMoon.velocity.first - 1)
                }

                if (y > y2) {
                    moon.velocity = moon.velocity.copy(second = moon.velocity.second - 1)
                    otherMoon.velocity = otherMoon.velocity.copy(second = otherMoon.velocity.second + 1)
                } else if (y < y2) {
                    moon.velocity = moon.velocity.copy(second = moon.velocity.second + 1)
                    otherMoon.velocity = otherMoon.velocity.copy(second = otherMoon.velocity.second - 1)
                }

                if (z > z2) {
                    moon.velocity = moon.velocity.copy(third = moon.velocity.third - 1)
                    otherMoon.velocity = otherMoon.velocity.copy(third = otherMoon.velocity.third + 1)
                } else if (z < z2) {
                    moon.velocity = moon.velocity.copy(third = moon.velocity.third + 1)
                    otherMoon.velocity = otherMoon.velocity.copy(third = otherMoon.velocity.third - 1)
                }
            }

            for (moon in input) {
                moon.position = Triple(
                    moon.position.first + moon.velocity.first,
                    moon.position.second + moon.velocity.second,
                    moon.position.third + moon.velocity.third
                )
            }
        }
    }
}

fun main() {

    val input = Scanner(Day12::class.java.getResourceAsStream("Day12Input"))

    val inputList: MutableList<Moon> = ArrayList()
    while (input.hasNext()) {
        var next = input.next()
        val x = next.substringAfter("<x=").substringBefore(",").toInt()

        next = input.next()
        val y = next.substringAfter("y=").substringBefore(",").toInt()

        next = input.next()
        val z = next.substringAfter("z=").substringBefore(">").toInt()
        inputList.add(Moon(Triple(x, y, z)))
    }

    println(Day12.solveDay12Part1(inputList))
    println(Day12.solveDay12Part2(inputList))
}
