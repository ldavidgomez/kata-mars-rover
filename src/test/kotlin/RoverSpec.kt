import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFailsWith

class RoverSpek: Spek({
    describe("given a simple command ") {

        on("empty array of commands") {

            it("should not move") {
                val command = emptyArray<String>()
                val rover = Rover(Position(0, 0, "S"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, "S")
            }
        }

        on("wrong direction") {

            it("should throw incorrect position exception") {
                val command = arrayOf("f")
                assertFailsWith<IncorrectPositionException> {
                    val rover = Rover(Position(0, 0, "wrong direction"))
                    rover.move(command)
                }
            }
        }

        on("wrong command") {

            it("should throw incorrect command exception") {
                val command = arrayOf("wrong command")
                assertFailsWith<IncorrectCommandException> {
                    val rover = Rover(Position(0, 0, "N"))
                    rover.move(command)
                }
            }
        }

        on("forward command and North direction") {

            it("should rover moves forwards") {
                val command = arrayOf("f")
                val rover = Rover(Position(0, 0, "N"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,1, "N")
            }
        }

        on("backward command and North direction") {

            it("should rover moves backwards") {
                val command = arrayOf("b")
                val rover = Rover(Position(0, 0, "N"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,-1, "N")
            }
        }

        on("forward command and South direction") {

            it("should rover moves backwards") {
                val command = arrayOf("f")
                val rover = Rover(Position(0, 0, "S"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,-1, "S")
            }
        }

        on("backward command and South direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(Position(0, 0, "S"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,1, "S")
            }
        }

        on("forward command and East direction") {

            it("should rover moves forwards") {
                val command = arrayOf("f")
                val rover = Rover(Position(0, 0, "E"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(1,0, "E")
            }
        }

        on("backward command and East direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(Position(0, 0, "E"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(-1,0, "E")
            }
        }

        on("forward command and West direction") {

            it("should rover moves forwards") {
                val command = arrayOf("f")
                val rover = Rover(Position(0, 0, "W"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(-1,0, "W")
            }
        }

        on("backward command and West direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(Position(0, 0, "W"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(1,0, "W")
            }
        }

        on("right command and North direction") {

            it("should rover turn right and direction is East") {
                val command = arrayOf("r")
                val rover = Rover(Position(0, 0, "N"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, "E")
            }
        }

        on("right command and East direction") {

            it("should rover turn right and direction is South") {
                val command = arrayOf("r")
                val rover = Rover(Position(0, 0, "E"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, "S")
            }
        }

        on("right command and South direction") {

            it("should rover turn right and direction is West") {
                val command = arrayOf("r")
                val rover = Rover(Position(0, 0, "S"))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, "W")
            }
        }
    }
})

class IncorrectPositionException : Exception()
class IncorrectCommandException : Exception()

enum class COMMANDS {
    F, B, R, L
}

enum class CARDINAL_POSITION(val value: Int) {
    N(1),
    E(2),
    S(3),
    W(4)
}

data class Position(val x: Int, val y: Int, val direction: String)

private val movementMap =
        mapOf(
                "N" to mapOf(
                        Pair(COMMANDS.F, Pair(0,1)),
                        Pair(COMMANDS.B, Pair(0,-1)),
                        Pair(COMMANDS.R, Pair(0,0)),
                        Pair(COMMANDS.L, Pair(0,0))
                ),
                "S" to mapOf(
                        Pair(COMMANDS.F, Pair(0,-1)),
                        Pair(COMMANDS.B, Pair(0,1)),
                        Pair(COMMANDS.R, Pair(0,0)),
                        Pair(COMMANDS.L, Pair(0,0))
                ),
                "E" to mapOf(
                        Pair(COMMANDS.F, Pair(1,0)),
                        Pair(COMMANDS.B, Pair(-1,0)),
                        Pair(COMMANDS.R, Pair(0,0)),
                        Pair(COMMANDS.L, Pair(0,0))
                ),
                "W" to mapOf(
                        Pair(COMMANDS.F, Pair(-1,0)),
                        Pair(COMMANDS.B, Pair(1,0)),
                        Pair(COMMANDS.R, Pair(0,0)),
                        Pair(COMMANDS.L, Pair(0,0))
                )
        )

class Rover(private val position: Position) {
    fun move(commands: Array<String>): Position {
        if (commands.isEmpty()) return position

        val validCommands = try { validateCommands(commands) } catch (e: IncorrectCommandException) { throw e}
        val validCardinalPosition = try { validatePosition(movementMap) } catch (e: IncorrectPositionException) { throw  e}

        val newDirection = calculateDirection(validCommands[0], position.direction)
        return Position(position.x + validCardinalPosition[validCommands[0]]!!.first, position.y + validCardinalPosition[validCommands[0]]!!.second, newDirection)
    }

    private fun calculateDirection(commands: COMMANDS, direction: String): String {
        return when(commands) {
            COMMANDS.R -> when(direction) {
                "N" -> "E"
                "E" -> "S"
                "S" -> "W"
                else -> "N"
            }
            COMMANDS.L -> when(direction) {
                "N" -> "W"
                "W" -> "S"
                "S" -> "E"
                else -> "N"
            }
            else -> direction
        }
    }

    private fun validatePosition(moveMap: Map<String, Map<COMMANDS, Pair<Int, Int>>>): Map<COMMANDS, Pair<Int, Int>> {
        return when (moveMap.containsKey(position.direction)) {
            true -> moveMap[position.direction]!!
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
