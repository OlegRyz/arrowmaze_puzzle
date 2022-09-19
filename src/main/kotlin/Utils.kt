import kotlin.math.abs

fun List<String>.parse(m: Int, n: Int) = Array(m) { row ->
    Array(n)
    { col ->
        val item = this[row * n + col]
        Cell(
            number = item.filter { it.isDigit() }.toIntOrNull() ?: 0,
            nextItemDirectionCol = when {
                item.contains("R") -> 1
                item.contains("L") -> -1
                else -> 0
            },
            nextItemDirectionRow = when {
                item.contains("D") -> 1
                item.contains("U") -> -1
                else -> 0
            }
        ).apply { if (abs(nextItemDirectionCol) + abs(nextItemDirectionRow) == 0 && n * m != number) {
                throw RuntimeException("$row, $col")
            }
        }
    }
}

fun List<String>.findNumbers(): List<Int> = map {item -> item.filter { it.isDigit() }.toIntOrNull() ?: 0 }.filter { it > 0 }

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
            row.forEachIndexed { j, _ -> print(String.format("%4d", j))}
            println()
            print("--|")
            row.forEachIndexed { j, _ -> print(String.format("----", j))}
        }
        println()
        print("$i |")
        row.forEach { cell -> print(String.format("%4d", cell.number)) }
    }
}