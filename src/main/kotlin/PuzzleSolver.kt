import java.time.LocalDateTime
import kotlin.math.sqrt
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

data class PuzzleData(
    val currentRow: Int, val currentCol: Int, val data: Array<Array<Cell>>,
    val presetNumbers: List<Int>,
    val isBroken: Boolean = false
) {
    private val cell = data[currentRow][currentCol]
    val isSolved = cell.number >= data.size * data.size && !this.isBroken

    fun solve(): PuzzleData {
        if (isSolved) {
            return this
        }
        val nextNumber = cell.number + 1


        val searchNumber = if (presetNumbers.contains(nextNumber)) {
            nextNumber
        } else {
            0
        }

        var nextRow = currentRow + cell.nextItemDirectionRow
        var nextCol = currentCol + cell.nextItemDirectionCol
        while (nextRow in data.indices && nextCol in data.indices) {
            if (data[nextRow][nextCol].number == searchNumber) {
                val updatedData = data.update(nextNumber, nextRow, nextCol)
                val solution = PuzzleData(
                    nextRow,
                    nextCol,
                    updatedData,
                    presetNumbers
                )
                    .solve()

                if (solution.isSolved) {
                    return solution
                }

            }
            nextRow += cell.nextItemDirectionRow
            nextCol += cell.nextItemDirectionCol
        }

        return Broken
    }

    companion object {
        val Broken = PuzzleData(
            0, 0,
            arrayOf(arrayOf(Cell(0, 1, 1))),
            listOf(),
            true
        )
    }
}

data class Cell(val number: Int, val nextItemDirectionCol: Int, val nextItemDirectionRow: Int)

@ExperimentalTime
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
    val executionTime = measureTime {
        val side = sqrt(inputArray.size.toDouble()).toInt()

        val solution = PuzzleData(
            0, 0,
            inputArray.parse(side, side),
            inputArray.findNumbers()
        )
            .solve()
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