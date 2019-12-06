import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

data class Tree(
    val node: String
) {
    val children: MutableSet<Tree> = HashSet()
}

class Day6 {
    companion object {
        fun solveDay6Part1(input: List<String>): Int {
            val treeMap = buildTreeMap(input)
            return getAmountOfOrbits(treeMap["COM"]!!, 0)
        }

        fun solveDay6Part2(input: List<String>): Int {
            val treeMap = buildTreeMap(input)

            val santaTree = getStepTree(treeMap["COM"]!!, treeMap["SAN"]!!)!!.reversed()
            val myTree = getStepTree(treeMap["COM"]!!, treeMap["YOU"]!!)!!.reversed()

            santaTree.forEachIndexed { indexSanta, treeSanta ->
                myTree.forEachIndexed { indexMy, treeMy ->
                    if (treeSanta == treeMy) {
                        return indexMy + indexSanta - 2
                    }
                }
            }

            return -1
        }

        private fun getStepTree(currentNode: Tree, targetNode: Tree): List<Tree>? {

            if (currentNode == targetNode) {
                return listOf(targetNode)
            }

            val elements = currentNode.children.mapNotNull {
                getStepTree(it, targetNode)
            }

            if (elements.isEmpty()) {
                return null
            }

            return listOf(currentNode, *elements.first().toTypedArray())
        }

        private fun getAmountOfOrbits(currentNode: Tree, currentIterations: Int): Int {
            return currentIterations + currentNode.children.map {
                getAmountOfOrbits(it, currentIterations + 1)
            }.sum()
        }

        private fun buildTreeMap(input: List<String>): Map<String, Tree> {
            val treeIdentifierMap: MutableMap<String, Tree> = HashMap()
            for (currInput in input) {
                val leftPart = currInput.split(")")[0]
                val rightPart = currInput.split(")")[1]

                val rightTree = if (treeIdentifierMap.containsKey(rightPart)) treeIdentifierMap[rightPart]!! else Tree(rightPart)
                val leftTree = if (treeIdentifierMap.containsKey(leftPart)) treeIdentifierMap[leftPart]!! else Tree(leftPart)

                leftTree.children.add(rightTree)
                treeIdentifierMap[leftPart] = leftTree
                treeIdentifierMap[rightPart] = rightTree
            }

            return treeIdentifierMap
        }
    }
}

fun main() {

    val input = Scanner(Day1::class.java.getResourceAsStream("Day6Input"))

    val inputList: MutableList<String> = ArrayList()
    while (input.hasNext()) {
        inputList.add(input.next())
    }

    println(Day6.solveDay6Part1(inputList))
    println(Day6.solveDay6Part2(inputList))
}
