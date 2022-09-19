import java.time.LocalDateTime
import kotlin.math.sqrt
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

data class PuzzleData(val current: Cell, val currentRow: Int, val currentCol: Int, val data: Array<Array<Cell>>,
                      val presetNumbers: List<Int>,
                      val isBroken: Boolean = false) {

    val isSolved = current.number >= data.size * data.size && !this.isBroken

    fun fillNext():PuzzleData {
        if (isSolved) return this
        var nextRow = currentRow + current.nextItemDirectionRow
        var nextCol = currentCol + current.nextItemDirectionCol

        var isFound = false
        if (presetNumbers.contains(current.number+1)) {
            while (!isFound && nextRow in data.indices && nextCol in data.indices) {
                if (data[nextRow][nextCol].number == current.number + 1) {
                    val solution = PuzzleData(
                        data[nextRow][nextCol],
                        nextRow,
                        nextCol,
                        data,
                        presetNumbers
                    ).fillNext()
                    isFound = true
                    if (solution.isSolved) {
                        return solution
                    }

                }
                nextRow += current.nextItemDirectionRow
                nextCol += current.nextItemDirectionCol
            }

            if (!isFound) {
                return Broken
            }
        }
        nextRow = currentRow + current.nextItemDirectionRow
        nextCol = currentCol + current.nextItemDirectionCol
        while(nextRow in data.indices && nextCol in data.indices) {
            if (data.couldUpdate(current.number+1, nextRow, nextCol)) {
                val updatedData = data.update(current.number+1, nextRow, nextCol)
                val solution = PuzzleData(
                    updatedData[nextRow][nextCol],
                    nextRow,
                    nextCol,
                    updatedData,
                    presetNumbers
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
        val Broken = PuzzleData(Cell(0,0,0), 0,0, emptyArray(), listOf(), true)
    }
}

private fun Array<Array<Cell>>.couldUpdate(number: Int, row: Int, col: Int) = this[row][col].number == 0 || this[row][col].number == number

class PuzzleSolver(inputArray: List<String>, m: Int, n: Int) {
    private val inputData = inputArray.parse(m, n)
    private val currentCell = inputData.first { it.any { it.number == 1 }}.first {it.number == 1}
    private val presetNumbers = inputArray.findNumbers()
    fun solve() = PuzzleData(currentCell, 0, 0, inputData, presetNumbers).fillNext()
}

data class Cell(val number: Int, val nextItemDirectionCol: Int, val nextItemDirectionRow: Int)

@ExperimentalTime
fun main() {
    val inputArray = listOf(
        "1DR", "D", "D", "DR", "L", "L", "D", "DL",
        "UR", "D", "R", "17DR", "52D", "D", "D", "L",
        "R", "43L", "U", "14DR", "DL", "UR", "10D", "45L",
        "D", "R", "UR", "UL", "15UL", "28R", "D", "L",
        "60R", "L", "26UL", "R", "DR", "L", "D", "61L",
        "D", "R", "D", "54L", "DR", "R", "UL", "D",
        "UR", "DR", "56R", "UR", "UL", "L", "U", "U",
        "R", "UL", "U", "33L", "R", "U", "L", "64"
    )
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
//        val inputArray = listOf(
//        "1R", "DR", "LD", "L", "D",
//        "8D", "R", "UR", "7L", "U",
//        "R", "R", "R", "LD", "DL",
//        "15R", "L", "L", "R", "U",
//        "R", "R", "L", "L", "25"
//        )
    println(LocalDateTime.now().toString())
    val executionTime = measureTime {
        val side = sqrt(inputArray.size.toDouble()).toInt()
        val solution = PuzzleSolver(inputArray, side, side).solve()
        if (solution.isSolved) {
            print("Solved")
            solution.data.printAsMatrix()
        } else {
            print("Cannot solve")
        }
    }

    println()
    println(executionTime)
}