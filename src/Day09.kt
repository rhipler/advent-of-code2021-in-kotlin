

fun main() {


    fun readInMap(input: List<String>) : Array<IntArray> {
        val numRows = input.size
        val numCols = input[0].length

        val map = Array(numRows) { IntArray(numCols) }

        for ((row, line) in input.withIndex()) {
            for ( (col,c) in line.withIndex()) {
                map[row][col] = c.digitToInt()
            }
        }

        return map
    }


    fun part1(input: List<String>): Int {
        val numRows = input.size
        val numCols = input[0].length

        val map = readInMap(input)

        var sum=0

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val value = map[row][col]

                if ( (row==0  || (map[row-1][col] > value)) && (col==0 || (map[row][col-1] > value))
                    && (col >= numCols-1 || (map[row][col+1] > value) ) && (row == numRows-1 || (map[row+1][col] > value ))  ) {
                    //low point
                    sum += value+1
                }
            }
        }

        return sum
    }




    fun part2(input: List<String>): Int {
        val numRows = input.size
        val numCols = input[0].length

        val map = readInMap(input)


        fun basinSize(row: Int, col: Int): Int {

            val basin = Array(numRows) { BooleanArray(numCols) { false } }

            fun basinDfs(row: Int, col: Int): Int {
                var size=1
                basin[row][col] = true
                if (col>0 && map[row][col-1]<9 && (!basin[row][col-1]) ) {
                    //left
                    size += basinDfs(row, col-1)
                }
                if (row>0 && map[row-1][col]<9 && (!basin[row-1][col]) ) {
                    //top
                    size += basinDfs(row-1,col)
                }
                if (col<numCols-1 && (map[row][col+1]<9) && ( !basin[row][col+1]) ) {
                    //right
                    size += basinDfs(row,col+1)
                }
                if (row<numRows-1 && (map[row+1][col]<9) && (!basin[row+1][col]) ) {
                    //down
                    size += basinDfs(row+1,col)
                }

                return size
            }

            return basinDfs(row,col)
        }


        val basinSizes = MutableList(0){ 0 }

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val value = map[row][col]

                if ( (row==0  || (map[row-1][col] > value)) && (col==0 || (map[row][col-1] > value))
                    && (col >= numCols-1 || (map[row][col+1] > value) ) && (row == numRows-1 || (map[row+1][col] > value ))  ) {
                    //low point
                    basinSizes.add( basinSize(row, col))
                }
            }
        }

        basinSizes.sortDescending()

        return basinSizes[0] * basinSizes[1] * basinSizes[2]
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check( part2(testInput) == 1134 )


    val input = readInput("Day09")
    println("Solution part1: " + part1(input))  //548
    println("Solution part2: " + part2(input)) // 786048
}
