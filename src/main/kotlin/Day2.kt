import java.util.*
import kotlin.collections.ArrayList

class Day2 {
    companion object {
        fun solveDay2Part1(input: List<Int>): Int {

            val newList = input.toMutableList()

            newList[1] = 12
            newList[2] = 2

            for (i in newList.indices step 4) {
                val currValue = input[i]
                if (currValue == 99) break

                val loc1 = input[i + 1]
                val val1 = input[loc1]

                val loc2 = input[i + 2]
                val val2 = input[loc2]

                val loc3 = input[i + 3]

                if (currValue == 1) {
                    newList[loc3] = val1 + val2
                } else {
                    newList[loc3] = val1 * val2
                }
            }

            return newList[0]
        }

        fun solveDay2Part2(input: List<Int>): Int {


            for (noun in 0..99) {
                for (verb in 0..99) {
                    val newInput = input.toMutableList()

                    newInput[1] = noun
                    newInput[2] = verb

                    try {
                        for (i in newInput.indices step 4) {
                            val currValue = newInput[i]
                            if (currValue == 99) break

                            val loc1 = newInput[i + 1]
                            val val1 = newInput[loc1]

                            val loc2 = newInput[i + 2]
                            val val2 = newInput[loc2]

                            val loc3 = newInput[i + 3]

                            if (currValue == 1) {
                                newInput[loc3] = val1 + val2
                            } else {
                                newInput[loc3] = val1 * val2
                            }
                        }

                        if (newInput[0] == 19690720) {
                            return 100 * noun + verb
                        }

                    } catch (e: Exception) {

                    }


                }
            }

            return -1
        }
    }
}

fun main() {

    val input = Scanner(Day2::class.java.getResourceAsStream("Day2Input")).useDelimiter(",")

    val inputList: MutableList<Int> = ArrayList()
    while (input.hasNextInt()) {
        inputList.add(input.nextInt())
    }

    println(Day2.solveDay2Part1(inputList))
    println(Day2.solveDay2Part2(inputList))
}
