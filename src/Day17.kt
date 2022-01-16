import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {


    class TargetRange(val x1: Int, val x2: Int, val y1: Int, val y2: Int)


    class Probe(var positionX: Int, var positionY: Int, var xVelocity: Int, var yVelocity: Int) {

        fun inTarget(targetRange: TargetRange): Boolean {
            return (positionX in targetRange.x1..targetRange.x2) && (positionY in targetRange.y1..targetRange.y2)
        }

        fun missed(targetRange: TargetRange): Boolean {
            return ((positionX > targetRange.x2 && xVelocity >= 0) || (positionX < targetRange.x1 && xVelocity <= 0)
                    || (positionY < targetRange.y1 && yVelocity <= 0) )
        }


        fun doStep() {
            positionX += xVelocity
            positionY += yVelocity
            if (xVelocity>0) {
                xVelocity -= 1
            } else if (xVelocity<0) {
                xVelocity += 1
            }
            yVelocity -= 1
        }


        fun shoot(targetRange: TargetRange): Int? {
            var highest: Int? = null
            var targetHit = false

            while ( !targetHit && ! missed(targetRange)) {
                doStep()

                highest = if (highest!=null) max(positionY, highest) else positionY
                targetHit = inTarget(targetRange)
            }

            return if (targetHit) highest else null
        }
    }


    fun parseInput(input: List<String>): TargetRange {
        val regex = """target area: x=(\d+)..(\d+), y=([-\d]+)..([-\d]+)""".toRegex()

        val (x1,x2, y1,y2) = regex.matchEntire(input[0])?.destructured ?: throw IllegalArgumentException("wrong input ")
        return TargetRange(x1.toInt(), x2.toInt(), y1.toInt(), y2.toInt())
    }



    fun doForAllTargetHits(targetRange: TargetRange, fn: (xVel: Int, yVel: Int, highest: Int)->Unit ) {
        val miny = min(targetRange.y1,0)
        val maxy = abs(targetRange.y1)
        for (yVelocity in  miny ..maxy) {
            for (xVelocity in 1..targetRange.x2) {

                val res = Probe(0,0,xVelocity, yVelocity).shoot(targetRange)
                if (res != null) {
                    fn(xVelocity,yVelocity, res)
                }

            }
        }

    }


    fun part1(input: List<String>): Int {
        val targetRange = parseInput(input)

        println(" ${targetRange.y1}  ${targetRange.y2} ")
        var highest = 0

        doForAllTargetHits(targetRange) { xv, yv, res ->
            if (res > highest) {
                highest = res
            }
        }

        return highest
    }


    fun part2(input: List<String>): Int {
        val targetRange = parseInput(input)

        val initialVelocities = mutableListOf<Pair<Int,Int>>()

        doForAllTargetHits(targetRange) { x, y, _ ->
            initialVelocities.add(Pair(x,y))
        }

        return initialVelocities.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)


    val input = readInput("Day17")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
