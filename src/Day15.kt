

data class Position(var row: Int, var col: Int)


data class QueueEntry(var cost: Int, val position: Position )


class PositionQueue {
    private val queue = mutableListOf<QueueEntry>()
    private val queueMap = mutableMapOf<Position, QueueEntry>()

    fun add(queueEntry: QueueEntry) {
        queue.add(queueEntry)
        queueMap[queueEntry.position] = queueEntry
    }
    fun removeFirst(): QueueEntry? {
        val entry = queue.removeFirstOrNull()
        if (entry!=null) {
            queueMap.remove(entry.position)
        }
        return entry
    }

    fun find(position: Position): QueueEntry? {
        return queueMap[position]
    }
}


fun main() {

    fun parseInput(input: List<String>): Array<IntArray>  {
        return input.map{ it.map{ it.digitToInt()}.toIntArray() }.toTypedArray()
    }


    fun lowestCostPath(riskMap: Array<IntArray>): Int {
        val sizeRow = riskMap[0].size
        val sizeCol = riskMap.size

        fun neighbors(position: Position): List<Position> {
            val list = mutableListOf<Position>()

            if (position.col < sizeCol - 1) list.add(Position(position.row, position.col + 1))
            if (position.row < sizeRow - 1) list.add(Position(position.row + 1, position.col))
            if (position.row > 0) list.add(Position(position.row - 1, position.col))
            if (position.col > 0) list.add(Position(position.row, position.col - 1))
            return list
        }


        val costMap = Array(sizeRow) { IntArray(sizeCol) { -1 } }
        val queue = PositionQueue()

        var currentElement: QueueEntry? = QueueEntry(0, Position(0,0) )

        while (currentElement != null) {

            costMap[currentElement.position.row][currentElement.position.col] = currentElement.cost

            for (neigh in neighbors(currentElement.position)) {
                val neighborCost = currentElement.cost + riskMap[neigh.row][neigh.col]
                val actCost = costMap[neigh.row][neigh.col]

                if (actCost < 0 || (neighborCost < actCost)) {

                    val entry = queue.find(neigh)
                    if (entry == null) {
                        queue.add( QueueEntry(neighborCost, neigh))
                    } else {
                        if (entry.cost > neighborCost) {
                            entry.cost = neighborCost
                        }
                    }
                }
            }

            currentElement = queue.removeFirst()
        }

        return costMap[sizeRow - 1][sizeCol - 1]
    }


    fun part1(input: List<String>): Int {
        val riskMap = parseInput(input)

        return lowestCostPath(riskMap)
    }


    fun makeBigmap(map: Array<IntArray>): Array<IntArray> {
        val sizeRow = map[0].size
        val sizeCol = map.size

        val bigMap = Array(sizeRow*5){ IntArray(sizeCol*5){ 0} }

        fun copyMap(startRow: Int, startCol: Int, add : Int) {
            for (row in 0 until sizeRow) {
                for (col in 0 until sizeCol) {
                    bigMap[startRow+row][startCol+col] = map[row][col]+add
                    if ( bigMap[startRow+row][startCol+col] > 9) {
                        bigMap[startRow+row][startCol+col] -= 9
                    }
                }
            }
        }

        for (i in 0..4) {
            for (j in 0..4) {
                copyMap(0 + i * sizeRow, j*sizeCol, j + i)
            }
        }

        return bigMap
    }


    fun part2(input: List<String>): Int {
        val bigMap = makeBigmap( parseInput(input) )

        return lowestCostPath(bigMap)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)


    val input = readInput("Day15")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
