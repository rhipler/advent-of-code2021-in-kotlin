


fun main() {


    fun simulateDay(numbers: MutableList<Int>) {
        for (index in numbers.indices) {
            if (numbers[index]==0) {
                numbers[index]=6
                numbers.add(8)
            } else {
                numbers[index]--;
            }
        }
    }


    fun simulateDay2(numbers: Array<Long>) {
        val numReproduce = numbers[0]

        for (i in 0..7) {
            numbers[i] = numbers[i+1];
        }
        numbers[8] = numReproduce
        numbers[6] += numReproduce
    }


    fun part1(input: List<String>): Int {
        val numbers = input[0].split(",").map {  x -> x.trim() }.map { x -> Integer.valueOf(x) }.toMutableList()

        for (i in 1..80) {
            simulateDay(numbers)
        }

        return numbers.size
    }


    fun part2(input: List<String>): Long {
        val numbers = input[0].split(",").map { x -> x.trim() }.map { x -> Integer.valueOf(x) }

        val population: Array<Long> = Array(9) { 0 }

        for (num in numbers) {
            if (num<=8) {
                population[num]++
            }
        }

        for (day in 1..256 ) {
            simulateDay2(population)
        }

        var sum : Long = 0
        for (i in 0..8) {
            sum += population[i]
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)


    val input = readInput("Day06")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
