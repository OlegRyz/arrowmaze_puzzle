import javafx.beans.binding.NumberBinding
import java.lang.RuntimeException
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.math.sqrt

data class PuzzleData(val current: Cell, val currentRow: Int, val currentCol: Int, val data: Array<Array<Cell>>, val isBroken: Boolean = false) {

    init {
//        println("Puzzle Data $current, $currentRow, $currentCol")
//        data.printAsMatrix()
    }
    val isSolved = current.number >= data.size * data.size && !this.isBroken
    fun fillNext():PuzzleData {
        if (isSolved) return this
        if (current.number < 12) {
            println(current.number)
        }
//        println("fill next")
        var nextRow = currentRow + current.nextItemDirectionRow
        var nextCol = currentCol + current.nextItemDirectionCol

        var isFound = false
        while(!isFound && nextRow in data.indices && nextCol in data.indices) {
            if(data[nextRow][nextCol].number == current.number+1) {
                val solution = PuzzleData(
                    data[nextRow][nextCol],
                    nextRow,
                    nextCol,
                    data
                ).fillNext()
                isFound = true
                if (solution.isSolved) {
                    return solution
                }

            }
            nextRow += current.nextItemDirectionRow
            nextCol += current.nextItemDirectionCol
        }
        nextRow = currentRow + current.nextItemDirectionRow
        nextCol = currentCol + current.nextItemDirectionCol
        while(!isFound && nextRow in data.indices && nextCol in data.indices) {
//            println("no move $nextRow, $nextCol : ${current.nextItemDirectionRow}, ${current.nextItemDirectionCol}")
            if (data.couldUpdate(current.number+1, nextRow, nextCol)) {
                val updatedData = data.update(current.number+1, nextRow, nextCol)
                val solution = PuzzleData(
                    updatedData[nextRow][nextCol],
                    nextRow,
                    nextCol,
                    updatedData
                ).fillNext()
                if (solution.isSolved) {
                    return solution
                }
            }
            nextRow += current.nextItemDirectionRow
            nextCol += current.nextItemDirectionCol
        }
        return PuzzleData.Broken
    }

    companion object{
        val Broken = PuzzleData(Cell(0,0,0), 0,0, emptyArray(), true)
    }
}

private fun <T> Array<Array<T>>.printAsMatrix() {
    forEachIndexed { i, row ->
        println()
        print("$i ")
        row.forEachIndexed { j, cell ->
            print(" $cell")
        }
    }
}

private fun Array<Array<Cell>>.update(number: Int, row: Int, col: Int): Array<Array<Cell>> {
    val newData = this.map { it.map { it.copy() }.toTypedArray() }.toTypedArray()
    newData[row][col] = newData[row][col].copy(number = number)
    return newData
}

private fun Array<Array<Cell>>.couldUpdate(number: Int, row: Int, col: Int) = this[row][col].number == 0 || this[row][col].number == number

class PuzzleSolver(inputArray: List<String>, m: Int, n: Int) {
    private val inputData = inputArray.parse(m, n)
    private val currentCell = inputData.first { it.any { it.number == 1 }}.first {it.number == 1}

    fun solve() = PuzzleData(currentCell, 0, 0, inputData).fillNext()
}

data class Cell(val number: Int, val nextItemDirectionCol: Int, val nextItemDirectionRow: Int)

private fun List<String>.parse(m: Int, n: Int) = Array(m) { row ->
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

fun main() {
//    val inputArray = listOf(
//        "1DR", "D", "D", "DR", "L", "L", "D", "DL",
//        "UR", "D", "R", "17DR", "52D", "D", "D", "L",
//        "R", "43L", "U", "14DR", "DL", "UR", "10D", "45L",
//        "D", "R", "UR", "UL", "15UL", "28R", "D", "L",
//        "60R", "L", "26UL", "R", "DR", "L", "D", "61L",
//        "D", "R", "D", "54L", "DR", "R", "UL", "D",
//        "UR", "DR", "56R", "UR", "UL", "L", "U", "U",
//        "R", "UL", "U", "33L", "R", "U", "L", "64"
//    )
//    val inputArray = listOf(
//        "1R", "2R", "3LD",
//        "7R", "6L", "8D",
//        "4R", "5U", "9",
//    )
//    val inputArray = listOf(
//        "1R", "R", "3LD",
//        "R", "6L", "D",
//        "4R", "U", "9",
//    )
//    val inputArray = listOf(
//        "1", "2", "5", "4",
//        "8", "6", "3", "7",
//        "9", "10", "11", "12",
//        "15", "14", "13", "16",
//    )
//    val inputArray = listOf(
//        "1R", "DR", "LD", "L",
//        "8D", "R", "UR", "7L",
//        "R", "R", "R", "LD",
//        "15R", "L", "L", "16",
//        )
//    val inputArray = listOf(
//        "1", "2", "5", "4","19",
//        "8", "6", "3", "7","18",
//        "9", "10", "11", "12","20",
//        "15", "14", "13", "16","17",
//        "22", "24", "21", "23", "25",
//    )
        val inputArray = listOf(
        "1R", "DR", "LD", "L", "D",
        "8D", "R", "UR", "7L", "U",
        "R", "R", "R", "LD", "DL",
        "15R", "L", "L", "R", "U",
        "R", "R", "L", "L", "25"
        )
    println(LocalDateTime.now().toString())
    val side = sqrt(inputArray.size.toDouble()).toInt()
    val solution = PuzzleSolver(inputArray, side, side).solve()
    print(solution)
    solution.data.printAsMatrix()
    println()
    println(LocalDateTime.now().toString())
}