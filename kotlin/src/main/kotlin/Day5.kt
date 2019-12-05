import java.util.*
import kotlin.collections.ArrayList

class Day5 {
    companion object {
        fun solveDay5Part1(input: List<Int>) {

            val newList = input.toMutableList()

            var ip = 0
            loop@ while (ip < newList.size) {
                val opcodeAndType = newList[ip].toString().padStart(5, '0')
                when (opcodeAndType.takeLast(2).toInt()) {
                    99 -> break@loop
                    1 -> {
                        val val1 = getVal(opcodeAndType, newList, ip + 1, 2)
                        val val2 = getVal(opcodeAndType, newList, ip + 2, 1)
                        val loc3 = newList[ip + 3]
                        newList[loc3] = val1 + val2
                        ip += 4
                    }
                    2 -> {
                        val val1 = getVal(opcodeAndType, newList, ip + 1, 2)
                        val val2 = getVal(opcodeAndType, newList, ip + 2, 1)
                        val loc3 = newList[ip + 3]
                        newList[loc3] = val1 * val2
                        ip += 4
                    }
                    3 -> {
                        val loc1 = newList[ip + 1]
                        newList[loc1] = readLine()!!.toInt()
                        ip += 2
                    }
                    4 -> {
                        val val1 = getVal(opcodeAndType, newList, ip + 1, 2)
                        println(val1)
                        ip += 2
                    }
                    5 -> {
                        val val1 = getVal(opcodeAndType, newList, ip + 1, 2)
                        val val2 = getVal(opcodeAndType, newList, ip + 2, 1)
                        if (val1 != 0) ip = val2 else ip += 3
                    }
                    6 -> {
                        val val1 = getVal(opcodeAndType, newList, ip + 1, 2)
                        val val2 = getVal(opcodeAndType, newList, ip + 2, 1)
                        if (val1 == 0) ip = val2 else ip += 3
                    }
                    7 -> {
                        val val1 = getVal(opcodeAndType, newList, ip + 1, 2)
                        val val2 = getVal(opcodeAndType, newList, ip + 2, 1)
                        val loc3 = newList[ip + 3]

                        if (val1 < val2) newList[loc3] = 1 else newList[loc3] = 0

                        ip += 4
                    }
                    8 -> {
                        val val1 = getVal(opcodeAndType, newList, ip + 1, 2)
                        val val2 = getVal(opcodeAndType, newList, ip + 2, 1)
                        val loc3 = newList[ip + 3]

                        if (val1 == val2) newList[loc3] = 1 else newList[loc3] = 0

                        ip += 4
                    }
                    else -> throw Exception("Invalid IP!")
                }
            }
        }

        private fun getVal(
            opcodeAndType: String,
            newList: MutableList<Int>,
            i: Int,
            opcodeIndex: Int
        ): Int {
            val type1 = opcodeAndType[opcodeIndex].toString().toInt()
            val loc1 = newList[i]
            return if (type1 == 0) newList[loc1] else loc1
        }

        fun solveDay5Part2(input: List<Int>) {
            solveDay5Part1(input)
        }
    }
}

fun main() {

    val input = Scanner(Day5::class.java.getResourceAsStream("Day5Input")).useDelimiter("[,\n]")

    val inputList: MutableList<Int> = ArrayList()
    while (input.hasNextInt()) {
        inputList.add(input.nextInt())
    }

    Day5.solveDay5Part1(inputList)
    Day5.solveDay5Part2(inputList)
}
