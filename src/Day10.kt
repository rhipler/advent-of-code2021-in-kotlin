

fun main() {

    val opening : Array<Char> = arrayOf('(','[','{','<')
    val closing = arrayOf(')',']','}','>')


    fun part1(input: List<String>): Int {
        val score = arrayOf(3, 57, 1197, 25137)

        var highScore = 0

        for (line in input) {
            val chunkStack  = MutableList(0) { ' '}

            loop@ for (char in line) {
                val openingIdx = opening.indexOf(char)
                val closingIdx = closing.indexOf(char)
                if (openingIdx >- 1) {
                    chunkStack.add(closing[openingIdx])
                }
                if (closingIdx > -1) {
                    val closingBracket = chunkStack.removeLast()
                    if (closingBracket != char) {
                        // wrong closing bracket
                        highScore += score[closingIdx]
                        break@loop
                    }
                }
            }
        }

        return highScore
    }




    fun part2(input: List<String>): Long {
        val score = arrayOf(1, 2, 3, 4)

        val lineScores = MutableList<Long>(0){ 0}

        loop@ for (line in input) {
            val chunkStack  = MutableList(0) { ' '}

            for (char in line) {
                val openingIdx = opening.indexOf(char)
                val closingIdx = closing.indexOf(char)
                if (openingIdx >- 1) {
                    chunkStack.add(closing[openingIdx])
                }
                if (closingIdx > -1) {
                    val closingBracket = chunkStack.removeLast()
                    if (closingBracket != char) {
                        // wrong closing bracket
                        continue@loop
                    }
                }
            }

            if (chunkStack.isNotEmpty()) {
                var lineScore = 0L
                do {
                    val idx = closing.indexOf(chunkStack.removeLast() )
                    lineScore *= 5
                    lineScore += score[idx]
                } while (chunkStack.isNotEmpty())

                lineScores.add(lineScore)
            }

        }

        lineScores.sort()
        return lineScores[lineScores.size / 2]
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check( part2(testInput) == 288957L )


    val input = readInput("Day10")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
