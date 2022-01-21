import kotlin.math.max


abstract class SnailfishNumber(open val left: SnailfishNumber?, open val right: SnailfishNumber?) {

    abstract override fun toString(): String

    abstract fun magnitude(): Int

    fun add(pair2: SnailfishNumber): PairNumber {
        return PairNumber(this,pair2)
    }

    abstract fun split(): SnailfishNumber

    abstract fun addLeftmost(add: Int): SnailfishNumber

    abstract fun addRightmost(add: Int): SnailfishNumber
}


class SimpleNumber(val value: Int) : SnailfishNumber(null,null) {

    override fun toString(): String {
        return "$value"
    }

    override fun magnitude(): Int {
        return value
    }

    override fun split(): SnailfishNumber {
        if (value >= 10) {
            return PairNumber( SimpleNumber(value / 2), SimpleNumber((value+1) / 2))
        }
        return this
    }

    override fun addLeftmost(add: Int): SimpleNumber {
        return SimpleNumber(value+add)
    }

    override fun addRightmost(add: Int): SimpleNumber {
        return SimpleNumber(value+add)
    }
}

class PairNumber(override val left: SnailfishNumber, override val right: SnailfishNumber) : SnailfishNumber(left, right) {

    override fun toString(): String {
        return "[$left,$right]"
    }

    override fun magnitude(): Int {
        val left = left.magnitude()
        val right = right.magnitude()

        return 3*left + 2*right
    }

    override fun addLeftmost(add: Int): PairNumber {
        return PairNumber(left.addLeftmost(add), right)
    }

    override fun addRightmost(add: Int): PairNumber {
        return PairNumber(left, right.addRightmost(add))
    }

    private fun explode(nested: Int): Triple<SnailfishNumber?,Int,Int> {

        if (left is SimpleNumber && right is SimpleNumber) {

            if (nested >= 5) {
                val addLeft = left.value
                val addRight = right.value
                return Triple( SimpleNumber(0), addLeft, addRight)
            }
            //no change, nothing to explode here
            return Triple(null, 0, 0)
        } else {
            if (left is PairNumber) {
                val (replaceLeft, addLeft, addRight) = left.explode(nested + 1)
                if (replaceLeft != null) {
                    val new =  PairNumber(replaceLeft, right.addLeftmost(addRight) )
                    return Triple(new, addLeft, 0)
                }
            }

            if (right is PairNumber) {
                val (replaceRight, addLeft, addRight) = right.explode(nested+1)
                if (replaceRight != null) {
                    val new = PairNumber(left.addRightmost(addLeft), replaceRight)
                    return Triple(new, 0,addRight)
                }
            }

            //no change, nothing to explode here
            return Triple(null,0,0)
        }
    }

    fun explode(): PairNumber {
        val (res, _,_) = explode(1)
        return if (res != null) {
            res as PairNumber
        } else {
            this
        }
    }


    override fun split(): PairNumber {
        val replaceLeft = left.split()
        if (replaceLeft !== left) {
            return PairNumber(replaceLeft, right)
        }

        val replaceRight = right.split()
        if (replaceRight !==  right) {
            return PairNumber(left, replaceRight)
        }

        return this
    }


    fun reduce(): PairNumber {
        var reduced = this
        var current: PairNumber

        do {
            current = reduced

            reduced = current.explode()
            if (reduced === current) {
                reduced = current.split()
            }

        } while (reduced !== current)

        return reduced
    }
}


class NumberParser {

    private class ParseInput(val str: String) {
        private var pos: Int = 0

        fun currentChar() :Char {
            return str[pos]
        }

        fun currentStr(): String {
            return str.substring(pos)
        }
        fun nextChar() {
            pos += 1
        }
        fun nextChar(len: Int) {
            pos += len
        }
    }

    companion object {
        private val regexNumber = """^(\d+).*""".toRegex()

        fun parseNumber(line: String): PairNumber {
            val res = parseNumberRecursive(ParseInput(line))
            return res as PairNumber
        }


        private fun parseNumberRecursive(parseInput: ParseInput): SnailfishNumber {

            if (parseInput.currentChar() == '[') {

                parseInput.nextChar()
                val left = parseNumberRecursive(parseInput)

                if (parseInput.currentChar() != ',') {
                    throw IllegalArgumentException(" parse error. expecting ,")
                }
                parseInput.nextChar()
                val right = parseNumberRecursive(parseInput)
                parseInput.nextChar()
                return PairNumber(left, right)

            } else {
                val substr = parseInput.currentStr()
                if (substr.matches(regexNumber)) {
                    val (number) = regexNumber.matchEntire(substr)?.destructured
                        ?: throw IllegalArgumentException(" no match")
                    parseInput.nextChar(number.length)
                    return SimpleNumber(number.toInt())
                } else {
                    throw IllegalArgumentException(" parse error")
                }
            }
        }

    }
}


fun main() {


    fun part1(input: List<String>): Int {

        var result : PairNumber? = null

        for (line in input) {
            result = result?.add(NumberParser.parseNumber(line))?.reduce() ?: NumberParser.parseNumber(line)
        }

        return result?.magnitude() ?: 0
    }


    fun part2(input: List<String>): Int {
        val numbers = mutableListOf<PairNumber>()

        for (line in input) {
            numbers.add(NumberParser.parseNumber(line))
        }

        var maxmag = 0

        for (number in numbers) {
            for (number2 in numbers) {
                if (number2 === number) {
                    continue
                }

                val n = number.add(number2).reduce()
                maxmag = max(maxmag, n.magnitude())
            }
        }

        return maxmag
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    check( NumberParser.parseNumber("[[[[[9,8],1],2],3],4]").explode().toString() == "[[[[0,9],2],3],4]")
    check( NumberParser.parseNumber("[7,[6,[5,[4,[3,2]]]]]").explode().toString() == "[7,[6,[5,[7,0]]]]")
    check( NumberParser.parseNumber("[[6,[5,[4,[3,2]]]],1]").explode().toString() == "[[6,[5,[7,0]]],3]")
    check( NumberParser.parseNumber("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").explode().toString() == "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    check( NumberParser.parseNumber("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]").explode().toString() == "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")


    val input = readInput("Day18")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
