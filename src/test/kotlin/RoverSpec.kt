import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFailsWith

class RoverSpek: Spek({
    describe("given a command ") {

        on("forward command and North direction") {

            it("should rover moves forwards") {
                val command = arrayOf("f")
                val rover = Rover(0, 0, "N")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(0,1)
            }
        }

        on("backward command and North direction") {

            it("should rover moves backwards") {
                val command = arrayOf("b")
                val rover = Rover(0, 0, "N")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(0,-1)
            }
        }

        on("forward command and South direction") {

            it("should rover moves backwards") {
                val command = arrayOf("f")
                val rover = Rover(0, 0, "S")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(0,-1)
            }
        }

        on("backward command and South direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(0, 0, "S")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(0,1)
            }
        }

        on("forward command and East direction") {

            it("should rover moves forwards") {
                val command = arrayOf("f")
                val rover = Rover(0, 0, "E")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(1,0)
            }
        }

        on("backward command and East direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(0, 0, "E")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(-1,0)
            }
        }

        on("forward command and West direction") {

            it("should rover moves forwards") {
                val command = arrayOf("f")
                val rover = Rover(0, 0, "W")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(-1,0)
            }
        }

        on("backward command and West direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(0, 0, "W")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(1,0)
            }
        }

        on("empty array of commands") {

            it("should not move") {
                val command = emptyArray<String>()
                val rover = Rover(0, 0, "S")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(0,0)
            }
        }

        on("wrong direction") {

            it("should throw incorrect position exception") {
                val command = arrayOf("f")
                assertFailsWith<IncorrectPositionException> {
                    val rover = Rover(0, 0, "wrong direction")
                    rover.move(command)
                }
            }
        }

        on("wrong command") {

            it("should throw incorrect command exception") {
                val command = arrayOf("wrong command")
                assertFailsWith<IncorrectCommandException> {
                    val rover = Rover(0, 0, "N")
                    rover.move(command)
                }
            }
        }

    }
})

class IncorrectPositionException : Exception()
class IncorrectCommandException : Exception()

enum class COMMANDS {
    F, B, R, L
}

data class Position(private val x: Int, private val y: Int, private val direction: String)

class Rover(private val x: Int, private val y: Int, private val direction: String) {
    fun move(commands: Array<String>): Pair<Int, Int> {
        if (commands.isEmpty()) return Pair(x, y)

        val moveMap = mapOf("N" to Pair(0,1), "S" to Pair(0, -1), "E" to Pair(1, 0))

        val movement =
                mapOf(
                        "N" to mapOf(
                                Pair(COMMANDS.F, Pair(0,1)),
                                Pair(COMMANDS.B, Pair(0,-1))
                        ),
                        "S" to mapOf(
                                Pair(COMMANDS.F, Pair(0,-1)),
                                Pair(COMMANDS.B, Pair(0,1))
                        ),
                        "E" to mapOf(
                                Pair(COMMANDS.F, Pair(1,0)),
                                Pair(COMMANDS.B, Pair(-1,0))
                        ),
                        "W" to mapOf(
                                Pair(COMMANDS.F, Pair(-1,0)),
                                Pair(COMMANDS.B, Pair(1,0))
                        )
            )


        val validCommands = try { validateCommands(commands) } catch (e: IncorrectCommandException) { throw e}
        val validCardinalPosition = try { validatePosition(movement) } catch (e: IncorrectPositionException) { throw  e}

        return Pair(x + validCardinalPosition[validCommands[0]]!!.first, y + validCardinalPosition[validCommands[0]]!!.second)
    }

    private fun validatePosition(moveMap: Map<String, Map<COMMANDS, Pair<Int, Int>>>): Map<COMMANDS, Pair<Int, Int>> {
        return when (moveMap.containsKey(direction)) {
            true -> moveMap[direction]!!
            else -> throw IncorrectPositionException()
        }
    }

    private fun validateCommands(commands: Array<String>): List<COMMANDS> {
        return try {
            commands.map { COMMANDS.valueOf(it.toUpperCase()) }
        } catch (e: Exception) {
            throw IncorrectCommandException()
        }
    }
}
