

fun main() {

    data class ParseResult(val position: Int, val versionSum: Int, val result: Long)


    fun bitsToInt(str: List<Int>, startPos: Int, len: Int): Int {
        return str.subList(startPos, startPos + len).reduce { acc, it -> (acc shl 1) + it }
    }


    fun operation(type: Int, values: List<Long>): Long {

        return when (type) {
            0 -> {
                values.reduce { acc, l -> acc + l }
            }
            1 -> {
                values.reduce { acc, l -> acc * l }
            }
            2 -> {
                values.minOf { it }
            }
            3 -> {
                values.maxOf { it }
            }
            5 -> {
                if (values[0] > values[1]) 1 else 0
            }
            6 -> {
                if (values[0] < values[1]) 1 else 0
            }
            7 -> {
                if (values[0] == values[1]) 1 else 0
            }
            else -> throw IllegalArgumentException("Illegal operation type")
        }
    }


    fun parsePacket(str: List<Int>, pos: Int): ParseResult {
        val version = bitsToInt(str, pos, 3)
        val type = bitsToInt(str, pos + 3, 3)
        var p = pos + 6

        if (type==4) {
            //literal value

            var value = 0L
            while (str[p] == 1) {
                value += bitsToInt(str,p+1,4)
                value = value shl 4
                p += 5
            }
            value += bitsToInt(str,p+1,4)
            p += 5

            return ParseResult(p,version,value)
        } else {

            val values = mutableListOf<Long>()
            var versionSum = version

            if (str[p]==0) {
                val lengthInBits = bitsToInt(str,p+1, 15)
                p += 16

                val end = p+lengthInBits
                while (p < end) {
                    val parseResult = parsePacket(str, p)
                    p = parseResult.position
                    versionSum += parseResult.versionSum
                    values.add(parseResult.result)
                }

            } else {
                val lengthInPackets = bitsToInt(str,p+1,11)
                p += 12

                for (i in 1..lengthInPackets) {
                    val parseResult = parsePacket(str, p)
                    p = parseResult.position
                    versionSum += parseResult.versionSum
                    values.add(parseResult.result)
                }
            }

            val result = operation(type, values)

            return ParseResult(p, versionSum, result)
        }
    }


    fun parseInput(input: List<String>): List<Int> {
        return input[0].map { it.digitToInt(16) }
            .flatMap { listOf(it shr 3, (it shr 2) and 1, (it shr 1) and 1, (it and 1)) }
    }


    fun part1(input: List<String>): Int {

        val str = parseInput(input)
        val result = parsePacket(str, 0)

        return result.versionSum
    }


    fun part2(input: List<String>): Long {
        val str =  parseInput(input)
        val result = parsePacket(str,0)

        return result.result
    }



    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 16)

    val testInput2 = readInput("Day16_test2")
    check(part1(testInput2) == 12)

    val testInput3 = readInput("Day16_test3")
    check(part1(testInput3) == 23)

    //part 2
    check(part2(listOf("C200B40A82") ) == 3L)
    check(part2(listOf("04005AC33890") ) == 54L)
    check(part2(listOf("880086C3E88112") ) == 7L)
    check(part2(listOf("9C0141080250320F1802104A08") ) == 1L)


    val input = readInput("Day16")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
