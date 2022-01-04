
class Polymer(template: String, val rules: List<Rule>) {
    var pairs = mutableMapOf<String,Long>()

    val elementQuantity = mutableMapOf<Char,Long>()

    init {
        for (i in 0..template.length-2) {
            val pair = template.substring(i,i+2)
            pairs[pair] = (pairs[pair] ?: 0) + 1
        }

        for (c in template) {
            elementQuantity[c] = (elementQuantity[c] ?: 0) + 1
        }
    }


    fun applyRules() {

        val newPairs = mutableMapOf<String,Long>()

        for (pair in pairs) {
            val rule = rules.find { rule -> rule.d1 == pair.key }
            if (rule != null) {
                val newPair1 = pair.key[0] + rule.d2.toString()
                val newPair2 = rule.d2.toString() + pair.key[1]

                newPairs[newPair1] = (newPairs[newPair1] ?: 0) + pair.value
                newPairs[newPair2] = (newPairs[newPair2] ?: 0) + pair.value
                elementQuantity[rule.d2] = (elementQuantity[rule.d2] ?: 0) + pair.value
            } else {
                newPairs[pair.key] = pair.value
            }
        }

        pairs = newPairs
    }


    fun printQuantities() {
        for (element in elementQuantity) {
            println(" ${element.key}:  ${element.value}")
        }
    }

    fun printPairs() {
        for (value in pairs) {
            println("Pair ${value.key}  ${value.value} ")
        }
    }

}

data class Rule(val d1 : String, val d2: Char) {
}


fun main() {

    fun readInput(input: List<String>): Polymer {
        val reg = """(\w+) -> (\w)""".toRegex()

        val rules = mutableListOf<Rule>()
        val polymerTemplate = (input[0].trim())
        for (line in input) {
            if (line.matches(reg)) {
                val (d1,d2) = reg.matchEntire(line)?.destructured ?: throw IllegalArgumentException("illegal input line")
                rules.add( Rule(d1,d2[0]))
            }
        }

        return Polymer(polymerTemplate, rules)
    }


    fun part1(input: List<String>): Long {
        val polymer = readInput(input)

        for (i in 1..10) {
            polymer.applyRules()
        }

        polymer.printPairs()
        polymer.printQuantities()

        return (polymer.elementQuantity.values.maxOrNull() ?: 0) - (polymer.elementQuantity.values.minOrNull() ?: 0)
    }


    fun part2(input: List<String>): Long {
        val polymer = readInput(input)

        for (i in 1..40) {
            polymer.applyRules()
        }

        polymer.printPairs()
        polymer.printQuantities()

        return (polymer.elementQuantity.values.maxOrNull() ?: 0) - (polymer.elementQuantity.values.minOrNull() ?: 0)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529L)


    val input = readInput("Day14")
    println("Solution part1: " + part1(input))
    println("Solution part2: " + part2(input))
}
