class Day4 {
    companion object {
        fun solveDay4Part1(min: Int, max: Int): Int {
            return (min..max)
                .map {
                    it.toString().toCharArray()
                }
                .filter { chars ->
                    chars.asSequence().zipWithNext().all { it.first <= it.second }
                }
                .count { chars ->
                    chars.any { char ->
                        chars.count { it == char } > 1
                    }
                }
        }

        fun solveDay4Part2(min: Int, max: Int): Int {
            return (min..max)
                .map {
                    it.toString().toCharArray()
                }
                .filter { chars ->
                    chars.asSequence().zipWithNext().all { it.first <= it.second }
                }
                .count { chars ->
                    chars.any { char ->
                        chars.count { it == char } == 2
                    }
                }
        }
    }
}

fun main() {
    println(Day4.solveDay4Part1(372304, 847060))
    println(Day4.solveDay4Part2(372304, 847060))
}
