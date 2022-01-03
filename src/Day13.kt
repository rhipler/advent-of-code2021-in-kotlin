
data class Point(val x: Int, val y: Int) {

    fun foldAlongY(foldY: Int): Point {
        val newY =
            if (y > foldY) {
                foldY * 2 - y
            } else {
                y
            }
        return Point(x, newY)
    }

    fun foldAlongX(foldX: Int): Point {
        val newX =
            if (x > foldX) {
                foldX * 2 - x
            } else {
                x
            }
        return Point(newX, y)
    }
}

enum class FoldType {
    ALONG_X, ALONG_Y
}

data class Fold(val value: Int, val type: FoldType) {

    fun doFold(p: Point): Point {
        return when(type) {
            FoldType.ALONG_X -> p.foldAlongX(value)
            FoldType.ALONG_Y -> p.foldAlongY(value)
        }
    }
}


fun printPoints(points: Set<Point>) {
    val maxY = points.maxOf { point -> point.y }
    val maxX = points.maxOf { point -> point.x }

    for (y in 0..maxY) {
        for (x in 0..maxX) {
            if (points.contains(Point(x,y))) {
                print("# ")
            } else {
                print(". ")
            }
        }
        println()
    }
}

class TransparentPaper(val points: Set<Point>, private val folds: List<Fold>) {

    fun doFirstFold(): Set<Point> {
        val newPoints = mutableSetOf<Point>()
        val fold = folds[0]

        for (p in points) {
            newPoints.add(fold.doFold(p))
        }
        return newPoints
    }

    fun doAllFolds(): Set<Point> {
        val newPoints = mutableSetOf<Point>()

        for (p in points) {
            var newP = p.copy()
            for (fold in folds) {
                newP = fold.doFold(newP)
            }
            newPoints.add(newP)
        }

        return newPoints
    }

}


fun main() {

    fun readInput(input: List<String>): TransparentPaper {
        val reg = """(\d+),(\d+)""".toRegex()
        val foldRegX = """fold along x=(\d+)""".toRegex()
        val foldRegY = """fold along y=(\d+)""".toRegex()

        val points = mutableSetOf<Point>()
        val folds = mutableListOf<Fold>()

        for (line in input) {
            if ( line.matches(reg) )  {
                val (d1, d2) = reg.matchEntire(line)?.destructured ?: throw IllegalArgumentException("illegal input line")
                points.add(Point(d1.toInt(),d2.toInt()))
            }
            if (line.matches(foldRegX)) {
                val (d1 ) = foldRegX.matchEntire(line)?.destructured ?: throw IllegalArgumentException("illegal input")
                val fold = Fold(d1.toInt(),FoldType.ALONG_X)
                folds.add(fold)
            }
            if (line.matches(foldRegY)) {
                val (d1 ) = foldRegY.matchEntire(line)?.destructured ?: throw IllegalArgumentException("illegal input")
                val fold = Fold(d1.toInt(),FoldType.ALONG_Y)
                folds.add(fold)
            }
        }

        return TransparentPaper(points,folds)
    }


    fun part1(input: List<String>): Int {
        val transparentPaper = readInput(input)

        val newPoints = transparentPaper.doFirstFold()
        println()
        printPoints(newPoints)

        return newPoints.size
    }


    fun part2(input: List<String>): Int {
        val transparentPaper = readInput(input)

        val newPoints = transparentPaper.doAllFolds()
        println()
        printPoints(newPoints)

        return newPoints.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    check(part2(testInput) == 16)


    val input = readInput("Day13")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
