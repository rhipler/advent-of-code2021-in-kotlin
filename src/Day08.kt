

fun main() {


    fun part1(input: List<String>): Int {

        var count=0

        for (line in input) {
            val (signalPatterns, outputValues) = line.split("|").map { x -> x.trim() }.map { x -> x.split(" ") }

            for (num in outputValues) {
                if (num.length in arrayOf(2,3,4,7)) {
                    count++
                }
            }
        }

        return count
    }


    fun containsChars(str: String, substr: String): Boolean {
        for (a in substr) {
            if (!str.contains(a)) {
                return false
            }
        }
        return true
    }

    fun decode(signalPatterns: List<String>): Map<String,Int> {

        var mapping = mutableMapOf<String,Int>()

        val valueforOne =  signalPatterns.find { x -> x.length==2 } ?: " "
        mapping[valueforOne] = 1

        val valueforSeven = signalPatterns.find{ x -> x.length==3} ?: " "
        mapping[valueforSeven] = 7

        val valueforFour = signalPatterns.find{ x -> x.length==4} ?: " "
        mapping[valueforFour] = 4

        val valueForEight =  signalPatterns.find{ x -> x.length==7} ?: " "
        mapping[valueForEight] = 8

        val valueforThree = signalPatterns.find{ x -> x.length==5 && containsChars(x, valueforOne) } ?: " "
        mapping[valueforThree] = 3

        var fourMinusOne = valueforFour
        for (a in valueforOne) {
            fourMinusOne = fourMinusOne.replace( ""+a, "")
        }
        val valueforFive = signalPatterns.find{ x -> x.length==5 && containsChars(x, fourMinusOne)} ?: " "
        mapping[valueforFive] = 5

        val valueforTwo = signalPatterns.find{ x -> x.length==5 && !x.equals(valueforThree) && ! x.equals(valueforFive)} ?: " "
        mapping[valueforTwo] = 2

        val valueforNine = signalPatterns.find{ x -> x.length==6 && containsChars(x,valueforOne) && containsChars(x, valueforFour) } ?: " "
        mapping[valueforNine] = 9

        val valueforSix = signalPatterns.find{ x -> x.length==6 && !containsChars(x,valueforOne) && !containsChars(x, valueforFour) } ?: " "
        mapping[valueforSix] = 6

        val valueforZero = signalPatterns.find{ x -> x.length==6 && !x.equals(valueforNine) && !x.equals(valueforSix)  } ?: " "
        mapping[valueforZero] = 0

        return mapping
    }

    fun part2(input: List<String>): Int {

        var summ = 0

        for (line in input) {
            val (val1, val2) = line.split("|").map { x -> x.trim() }.map { x -> x.split(" ") }

            val signalPatterns = val1.map{x -> x.trim()}.map{ x -> x.toCharArray().sorted().joinToString("") }
            val outputValues = val2.map{x -> x.trim()}.map{ x -> x.toCharArray().sorted().joinToString("") }

            val mapping = decode(signalPatterns)

            var number =0
            for (digit in outputValues) {
                number *= 10
                number += mapping[digit] ?: 0
            }

            summ += number
        }

        return summ
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check( part2(testInput) == 61229 )


    val input = readInput("Day08")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
