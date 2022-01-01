

class Cave(val name: String) {
    val big
        get() = name[0].isUpperCase()

    val neighbors = mutableListOf<Cave>()
}


fun main() {


    fun readInput(input: List<String>): Map<String,Cave> {

        val caves = mutableMapOf<String,Cave>()

        for (line in input) {
            val (name1, name2) = line.split("-")
            val c1 = caves[name1] ?: Cave(name1).also { caves[name1] = it }
            val c2 = caves[name2] ?: Cave(name2).also { caves[name2] = it }
            c1.neighbors.add(c2)
            c2.neighbors.add(c1)
        }

        return caves
    }


    fun printPath(path: MutableList<Cave>) {
        for (c in path) {
            print("${c.name} ")
        }
        println()
    }


    fun distinctPaths(path: MutableList<Cave>, smallCaveTwice: Boolean): Int {
        val curr = path.last()

        var countPaths = 0
        for (neigh in curr.neighbors) {
            var scTwice = smallCaveTwice

            if (neigh.name=="start") {
                continue
            }

            if (!neigh.big && ( path.find { it.name == neigh.name} != null) ) {
                if (smallCaveTwice) {
                    scTwice=false
                } else {
                    continue
                }
            }

            val newPath = path.toMutableList()
            newPath.add(neigh)

            if (neigh.name=="end") {
                //printPath(newPath)
                countPaths++
            } else {
                countPaths += distinctPaths(newPath, scTwice)
            }
        }

        return countPaths
    }


    fun part1(input: List<String>): Int {
        val caves = readInput(input)

        val start = caves["start"] ?: return 0
        val path = mutableListOf(start)
        return distinctPaths(path, false)
    }


    fun part2(input: List<String>): Int {
        val caves = readInput(input)

        val start = caves["start"] ?: return 0
        val path = mutableListOf(start)
        return distinctPaths(path, true)
    }



    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 226)
    check(part2(testInput) == 3509)


    val input = readInput("Day12")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
