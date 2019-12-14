import Day14.Companion.parseInput
import java.util.*
import kotlin.collections.HashMap

class CreatedElement(
    val amount: Long,
    val inputElements: List<Pair<Long, String>>
)

class Day14 {
    companion object {
        fun solveDay14Part1(elements: Map<String, CreatedElement>): Long {

            // Pair<Created, Left>
            val requiredMapping: MutableMap<String, Pair<Long, Long>> = HashMap()

            getInputElements(elements, requiredMapping, elements["FUEL"]!!, 1)

            return requiredMapping["ORE"]!!.first
        }

        fun solveDay14Part2(elements: Map<String, CreatedElement>): Long {

            val requiredMapping: MutableMap<String, Pair<Long, Long>> = HashMap()
            getInputElements(elements, requiredMapping, elements["FUEL"]!!, 1)
            val minFuel = 1000000000000L / requiredMapping["ORE"]!!.first

            return search(minFuel, minFuel * 2, elements)
        }

        private fun search(min: Long, max: Long, elements: Map<String, CreatedElement>): Long {

            if (min == max) {
                return min
            }

            val mid = (min + max) / 2

            val requiredMapping: MutableMap<String, Pair<Long, Long>> = HashMap()
            getInputElements(elements, requiredMapping, elements["FUEL"]!!, mid)
            val requiredOre = requiredMapping["ORE"]!!.first

            return if (requiredOre > 1000000000000L) {
                search(min, mid, elements)
            } else if (requiredOre < 1000000000000L) {
                search(mid + 1, max, elements)
            } else {
                mid
            }
        }

        private fun getInputElements(elements: Map<String, CreatedElement>, requiredMapping: MutableMap<String, Pair<Long, Long>>, rootElement: CreatedElement, amount: Long) {

            val multiply = (amount + rootElement.amount - 1) / rootElement.amount

            for (el in rootElement.inputElements) {
                val name = el.second
                var requiredNewAmount = el.first * multiply

                requiredMapping[name] = requiredMapping[name] ?. let {
                    Pair(it.first + requiredNewAmount, it.second)
                } ?: run {
                    Pair(requiredNewAmount, 0L)
                }

                if (name != "ORE") {
                    val currElement = elements[name]!!
                    val requiredCreatedPair = requiredMapping[name]!!

                    val nextCreatedAmount = (requiredNewAmount + currElement.amount - 1) / currElement.amount
                    var leftOver = (currElement.amount * nextCreatedAmount) - requiredNewAmount + requiredCreatedPair.second

                    if (leftOver < 0) throw Exception()

                    if (leftOver >= currElement.amount) {
                        requiredNewAmount -= currElement.amount
                        leftOver -= currElement.amount
                    }

                    requiredMapping[name] = Pair(requiredCreatedPair.first, leftOver)

                    getInputElements(elements, requiredMapping, currElement, requiredNewAmount)
                }
            }
        }

        fun parseInput(input: String): Map<String, CreatedElement> {

            return input
                .trim()
                .lines()
                .map {
                    val split = it.split(" => ")

                    val requiresElements = split[0].split(", ").map { el ->
                        val amount = el.split(" ")[0].toLong()
                        val element = el.split(" ")[1]

                        Pair(amount, element)
                    }

                    val returnsAmount = split[1].split(" ")[0].toLong()
                    val returnsElement = split[1].split(" ")[1]

                    returnsElement to CreatedElement(
                        returnsAmount,
                        requiresElements
                    )
                }
                .toMap()
        }
    }
}

fun main() {

    val input = Scanner(Day14::class.java.getResourceAsStream("Day14Input")).useDelimiter("\\A").next()

    val parseInput = parseInput(input)

    println(Day14.solveDay14Part1(parseInput))
    println(Day14.solveDay14Part2(parseInput))
}
