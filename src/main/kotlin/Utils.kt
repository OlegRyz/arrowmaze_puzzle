import kotlin.math.abs

fun List<String>.parse(m: Int, n: Int) = Array(m) { row ->
    Array(n)
    { col ->
        val item = this[row * n + col]
        Cell(
            number = item.filter { it.isDigit() }.toIntOrNull() ?: 0,
            arrowDirectionCol = when {
                item.contains("R") -> 1
                item.contains("L") -> -1
                else -> 0
            },
            arrowDirectionRow = when {
                item.contains("D") -> 1
                item.contains("U") -> -1
                else -> 0
            }
        ).apply {
            if (abs(arrowDirectionCol) + abs(arrowDirectionRow) == 0 && n * m != number) {
                throw RuntimeException("$row, $col")
            }
        }
    }
}

fun List<String>.findPresetNumbers(): List<Int> =
    map { item -> item.filter { it.isDigit() }.toIntOrNull() ?: 0 }.filter { it > 0 }

fun Array<Array<Cell>>.update(number: Int, row: Int, col: Int): Array<Array<Cell>> {
    val newData = this.map { it.map { it.copy() }.toTypedArray() }.toTypedArray()
    newData[row][col] = newData[row][col].copy(number = number)
    return newData
}

fun Array<Array<Cell>>.printAsMatrix() {

    forEachIndexed { i, row ->
        if (i == 0) {
            println()
            print("  |")
            row.forEachIndexed { j, _ -> print(String.format("%4d", j)) }
            println()
            print("--|")
            row.forEachIndexed { j, _ -> print(String.format("----", j)) }
        }
        println()
        print("$i |")
        row.forEach { cell -> print(String.format("%4d", cell.number)) }
    }
    println()
}

val originalMaze = listOf(
    "1DR", "D", "D", "DR", "L", "L", "D", "DL",
    "UR", "D", "R", "17DR", "52D", "D", "D", "L",
    "R", "43L", "U", "14DR", "DL", "UR", "10D", "45L",
    "D", "R", "UR", "UL", "15UL", "28R", "D", "L",
    "60R", "L", "26UL", "R", "DR", "L", "D", "61L",
    "D", "R", "D", "54L", "DR", "R", "UL", "D",
    "UR", "DR", "56R", "UR", "UL", "L", "U", "U",
    "R", "UL", "U", "33L", "R", "U", "L", "64"
)

//    val solution = listOf(
//        "1R", "2R", "3LD",
//        "7R", "6L", "8D",
//        "4R", "5U", "9",
//    )
val testMaze3 = listOf(
    "1R", "R", "3LD",
    "R", "6L", "D",
    "4R", "U", "9",
)

//    val inputArray = listOf(
//        "1", "2", "5", "4",
//        "8", "6", "3", "7",
//        "9", "10", "11", "12",
//        "15", "14", "13", "16",
//    )
val testMaze4 = listOf(
    "1R", "DR", "LD", "L",
    "8D", "R", "UR", "7L",
    "R", "R", "R", "LD",
    "15R", "L", "L", "16",
)

//    val inputArray = listOf(
//        "1", "2", "5", "4","19",
//        "8", "6", "3", "7","18",
//        "9", "10", "11", "12","20",
//        "15", "14", "13", "16","17",
//        "22", "24", "21", "23", "25",
//    )
val testMaze5 = listOf(
    "1R", "DR", "LD", "L", "D",
    "8D", "R", "UR", "7L", "U",
    "R", "R", "R", "LD", "DL",
    "15R", "L", "L", "R", "U",
    "R", "R", "L", "L", "25"
)