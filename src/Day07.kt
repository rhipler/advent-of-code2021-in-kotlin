import kotlin.math.abs


fun main() {


    fun part1(input: List<String>): Int {
        val numbers = input[0].split(",").map {  x -> x.trim() }.map { x -> Integer.valueOf(x) }

        val max = numbers.maxOrNull() ?: 0

        var minimum = Integer.MAX_VALUE
        for (alignto in 0..max) {

            var cost =0;
            for (num in numbers) {
                cost += abs(num - alignto)
            }

            if (cost < minimum) {
                minimum = cost
            }
        }

        return minimum
    }


    fun part2(input: List<String>): Int {
        val numbers = input[0].split(",").map { x -> x.trim() }.map { x -> Integer.valueOf(x) }
        val max = numbers.maxOrNull() ?: 0

        var minimum = Integer.MAX_VALUE
        for (alignto in 0..max) {

            var cost =0;
            for (num in numbers) {
                val len = abs(num - alignto)
                for (i in 1..len) {
                    cost += i
                }
            }

            if (cost < minimum) {
                minimum = cost
            }
        }

        return minimum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check( part2(testInput) == 168 )


    val input = readInput("Day07")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
