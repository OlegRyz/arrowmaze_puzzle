import java.time.LocalDateTime
import kotlin.math.sqrt
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

data class PuzzleData(
    val row: Int, val col: Int,
    val data: Array<Array<Cell>>,
    val presetNumbers: List<Int>,
) {
    val cell = data[row][col]
    val isSolved = cell.number >= data.size * data.size
    val nextNumber = cell.number + 1

    fun solve(): PuzzleData {
        if (isSolved) {
            return this
        }

        val numberToBeReplaced = if (nextNumber in presetNumbers) {
            nextNumber
        } else {
            0
        }

        var nextRow = row + cell.arrowDirectionRow
        var nextCol = col + cell.arrowDirectionCol
        while (nextRow in 0..data.size-1 && nextCol in 0..data.size-1) {
            if (data[nextRow][nextCol].number == numberToBeReplaced) {
                val updatedData = data.update(nextNumber, nextRow, nextCol)
                val solution = PuzzleData(nextRow, nextCol,
                    updatedData,
                    presetNumbers
                )
                    .solve()

                if (solution.isSolved) {
                    return solution
                }

            }
            nextRow += cell.arrowDirectionRow
            nextCol += cell.arrowDirectionCol
        }

        return Broken
    }

    companion object {
        val Broken = PuzzleData(
            0, 0,
            arrayOf(arrayOf(Cell(0, 1, 1))),
            listOf(),
        )
    }
}

data class Cell(val number: Int, val arrowDirectionCol: Int, val arrowDirectionRow: Int)

@ExperimentalTime
fun main() {
    println(LocalDateTime.now().toString())

    val inputArray = originalMaze
    val side = sqrt(inputArray.size.toDouble()).toInt()

    val executionTime = measureTime {

        val data = inputArray.parse(side, side)
        data.printAsMatrix()
        val puzzle = PuzzleData(
            0, 0,
            data,
            inputArray.findPresetNumbers()
        )

        val solution = puzzle.solve()
        if (solution.isSolved) {
            print("Solved")
            solution.data.printAsMatrix()
        } else {
            print("Cannot solve")
        }
    }
    println("$executionTime spent to solve it")
}