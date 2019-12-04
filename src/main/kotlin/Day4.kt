import java.util.*
import kotlin.collections.ArrayList

class Day4 {
    companion object {
        fun solveDay4Part1(min: Int, max: Int): Int {

            var possibilites = 0

            outer@ for (i in min..max) {
                val chars = i.toString().toCharArray()
                var hasADuplicateLetter = false
                for (j in chars.indices) {
                    if (j + 1 < chars.size && chars[j] > chars[j + 1]) {
                        continue@outer
                    }
                    if (chars.filter { it == chars[j] }.count() > 1) {
                        hasADuplicateLetter = true
                    }
                }

                if (hasADuplicateLetter) {
                    possibilites++
                }
            }

            return possibilites
        }

        fun solveDay4Part2(min: Int, max: Int): Int {
            var possibilites = 0

            outer@ for (i in min..max) {
                val chars = i.toString().toCharArray()
                var hasADuplicateLetter = false
                for (j in chars.indices) {
                    if (j + 1 < chars.size && chars[j] > chars[j + 1]) {
                        continue@outer
                    }
                    if (chars.filter { it == chars[j] }.count() == 2) {
                        hasADuplicateLetter = true
                    }
                }

                if (hasADuplicateLetter) {
                    possibilites++
                }
            }

            return possibilites
        }
    }
}

fun main() {
    println(Day4.solveDay4Part1(245182, 790572))
    println(Day4.solveDay4Part2(245182, 790572))
}
