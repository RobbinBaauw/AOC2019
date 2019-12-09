class IntCodeComputer(input: List<Long>) {
    var ip = 0L
    var base = 0L

    var newList: MutableList<Long> = input.toMutableList()

    /**
     * Retuns true if it ended normally
     */
    fun run(arguments: MutableList<Long>, writeOutputTo: MutableList<Long>?): Boolean {

        while (ip < newList.size) {
            val opcodeAndType = getIndex(ip).toString().padStart(5, '0')
            when (opcodeAndType.takeLast(2).toInt()) {
                99 -> return true
                1 -> {
                    val val1 = getVal(opcodeAndType, ip + 1, 2)
                    val val2 = getVal(opcodeAndType, ip + 2, 1)
                    val loc3 = getVal(opcodeAndType, ip + 3, 0, true)
                    setIndex(loc3, val1 + val2)
                    ip += 4
                }
                2 -> {
                    val val1 = getVal(opcodeAndType, ip + 1, 2)
                    val val2 = getVal(opcodeAndType, ip + 2, 1)
                    val loc3 = getVal(opcodeAndType, ip + 3, 0, true)
                    setIndex(loc3, val1 * val2)
                    ip += 4
                }
                3 -> {
                    val loc1 = getVal(opcodeAndType, ip + 1, 2, true)

                    if (arguments.isNotEmpty()) {
                        setIndex(loc1, arguments[0])
                        arguments.removeAt(0)
                    } else {
                        println("Input:")
                        setIndex(loc1, readLine()!!.toLong())
                    }

                    ip += 2
                }
                4 -> {
                    val val1 = getVal(opcodeAndType, ip + 1, 2)

                    ip += 2

                    if (writeOutputTo != null) {
                        writeOutputTo.add(val1)
                        return false
                    }

                    println(val1)
                }
                5 -> {
                    val val1 = getVal(opcodeAndType, ip + 1, 2)
                    val val2 = getVal(opcodeAndType, ip + 2, 1)
                    if (val1 != 0L) ip = val2 else ip += 3
                }
                6 -> {
                    val val1 = getVal(opcodeAndType, ip + 1, 2)
                    val val2 = getVal(opcodeAndType, ip + 2, 1)
                    if (val1 == 0L) ip = val2 else ip += 3
                }
                7 -> {
                    val val1 = getVal(opcodeAndType, ip + 1, 2)
                    val val2 = getVal(opcodeAndType, ip + 2, 1)
                    val loc3 = getVal(opcodeAndType, ip + 3, 0, true)

                    if (val1 < val2) setIndex(loc3, 1) else setIndex(loc3, 0)

                    ip += 4
                }
                8 -> {
                    val val1 = getVal(opcodeAndType, ip + 1, 2)
                    val val2 = getVal(opcodeAndType, ip + 2, 1)
                    val loc3 = getVal(opcodeAndType, ip + 3, 0, true)

                    if (val1 == val2) setIndex(loc3, 1) else setIndex(loc3, 0)

                    ip += 4
                }
                9 -> {
                    val val1 = getVal(opcodeAndType, ip + 1, 2).toInt()
                    base += val1

                    ip += 2
                }
                else -> throw Exception("Invalid IP!")
            }
        }

        return true
    }

    private fun getVal(
        opcodeAndType: String,
        i: Long,
        opcodeIndex: Int,
        direct: Boolean = false
    ): Long {
        val type1 = opcodeAndType[opcodeIndex].toString().toInt()
        val loc1 = getIndex(i)

        return if (direct) {
            when (type1) {
                0 -> loc1
                1 -> loc1
                2 -> loc1 + base
                else -> throw Exception("Invalid val!")
            }
        } else {
            when (type1) {
                0 -> getIndex(loc1)
                1 -> loc1
                2 -> getIndex(loc1 + base)
                else -> throw Exception("Invalid val!")
            }
        }


    }

    private fun getIndex(index: Long): Long {
        return if (index >= newList.size) {
            0
        } else {
            newList[index.toInt()]
        }
    }

    private fun setIndex(index: Long, value: Long) {
        if (index >= newList.size) {
            for (x in 0..(index - newList.size)) {
                newList.add(0L)
            }
        }
        newList[index.toInt()] = value
    }
}
