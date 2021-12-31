

fun main() {

    fun printMap(map: Array<IntArray>) {
        map.forEach { row ->
            row.forEach { print("$it ") }
            println()
        }
    }


    fun simulateStep(map: Array<IntArray>): Int {
        var flashes = 0

        val flashed: Array<BooleanArray> = Array(10) { BooleanArray(10) { false } }

        fun neighbors(row: Int, col: Int): List<Pair<Int, Int>> {
            val list = mutableListOf<Pair<Int, Int>>()

            if (row > 0 && col > 0) list.add(Pair(-1, -1))
            if (row > 0) list.add(Pair(-1, 0))
            if (row > 0 && col < 9) list.add(Pair(-1, 1))
            if (col > 0) list.add(Pair(0, -1))
            if (col < 9) list.add(Pair(0, 1))
            if (row < 9 && col > 0) list.add(Pair(1, -1))
            if (row < 9) list.add(Pair(1, 0))
            if (row < 9 && col < 9) list.add(Pair(1, 1))
            return list
        }

        fun flash(row: Int, col: Int) {
            flashed[row][col] = true
            flashes++
            for (neighbor in neighbors(row, col)) {
                map[row + neighbor.first][col + neighbor.second]++
                if (map[row + neighbor.first][col + neighbor.second] > 9 && !flashed[row + neighbor.first][col + neighbor.second]) {
                    flash(row + neighbor.first, col + neighbor.second)
                }
            }
        }


        for (row in 0..9) {
            for (col in 0..9) {
                map[row][col]++
                if (map[row][col] > 9 && !flashed[row][col]) {
                    flash(row, col)
                }
            }
        }

        for (row in 0..9) {
            for (col in 0..9) {
                if (map[row][col] > 9) {
                    map[row][col] = 0
                }
            }
        }

        return flashes
    }



    fun part1(input: List<String>): Int {
        val map = input.map{ it.map{ it.digitToInt()}.toIntArray() }.toTypedArray()

        printMap(map)

        var count=0
        for (i in 0..99) {
            val flashes = simulateStep(map)
            count += flashes
            println(" $flashes flashes")
        }

        printMap(map)
        println()

        return count
    }



    fun part2(input: List<String>): Int {
        val map = input.map{ it.map{ it.digitToInt()}.toIntArray() }.toTypedArray()

        //printMap(map)
        var flashes = 0
        var step=0
        while (flashes < 100) {
            flashes = simulateStep(map)
            step++
        }

        return step
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check( part2(testInput) == 195 )


    val input = readInput("Day11")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
