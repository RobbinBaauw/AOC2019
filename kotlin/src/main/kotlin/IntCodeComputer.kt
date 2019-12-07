class IntCodeComputer(input: List<Int>) {
    var ip = 0
    var newList: MutableList<Int> = input.toMutableList()

    /**
     * Retuns true if it ended normally
     */
    fun run(arguments: MutableList<Int>, writeOutputTo: MutableList<Int>?): Boolean {

        while (ip < newList.size) {
            val opcodeAndType = newList[ip].toString().padStart(5, '0')
            when (opcodeAndType.takeLast(2).toInt()) {
                99 -> return true
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

                    if (arguments.isNotEmpty()) {
                        newList[loc1] = arguments[0]
                        arguments.removeAt(0)
                    } else {
                        newList[loc1] = readLine()!!.toInt()
                    }

                    ip += 2
                }
                4 -> {
                    val val1 = getVal(opcodeAndType, newList, ip + 1, 2)

                    ip += 2

                    if (writeOutputTo != null) {
                        writeOutputTo.add(val1)
                        return false
                    }

                    println(val1)
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

        return true
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
}
