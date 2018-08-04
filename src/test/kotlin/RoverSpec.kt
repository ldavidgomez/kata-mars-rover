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
                val rover = Rover(Position(0, 0, S))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, S)
            }
        }

        on("wrong command") {

            it("should throw incorrect command exception") {
                val command = arrayOf("wrong command")
                assertFailsWith<IncorrectCommandException> {
                    val rover = Rover(Position(0, 0, N))
                    rover.move(command)
                }
            }
        }

        on("wrong direction") {

            it("should throw incorrect position exception") {
                val command = arrayOf("r")
                assertFailsWith<IncorrectPositionException> {
                    val rover = Rover(Position(0, 0, "A"))
                    rover.move(command)
                }
            }
        }

        on("forward command and North direction") {

            it("should rover moves forwards") {
                val command = arrayOf("f")
                val rover = Rover(Position(0, 0, N))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,1, N)
            }
        }

        on("backward command and North direction") {

            it("should rover moves backwards") {
                val command = arrayOf("b")
                val rover = Rover(Position(0, 0, N))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,-1, N)
            }
        }

        on("forward command and South direction") {

            it("should rover moves backwards") {
                val command = arrayOf("f")
                val rover = Rover(Position(0, 0, S))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,-1, S)
            }
        }

        on("backward command and South direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(Position(0, 0, S))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,1, S)
            }
        }

        on("forward command and East direction") {

            it("should rover moves forwards") {
                val command = arrayOf("f")
                val rover = Rover(Position(0, 0, E))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(1,0, E)
            }
        }

        on("backward command and East direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(Position(0, 0, E))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(-1,0, E)
            }
        }

        on("forward command and West direction") {

            it("should rover moves forwards") {
                val command = arrayOf("f")
                val rover = Rover(Position(0, 0, W))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(-1,0, W)
            }
        }

        on("backward command and West direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(Position(0, 0, W))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(1,0, W)
            }
        }

        on("right command and North direction") {

            it("should rover turn right and direction is East") {
                val command = arrayOf("r")
                val rover = Rover(Position(0, 0, N))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, E)
            }
        }

        on("right command and East direction") {

            it("should rover turn right and direction is South") {
                val command = arrayOf("r")
                val rover = Rover(Position(0, 0, E))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, S)
            }
        }

        on("right command and South direction") {

            it("should rover turn right and direction is West") {
                val command = arrayOf("r")
                val rover = Rover(Position(0, 0, S))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, W)
            }
        }

        on("right command and West direction") {

            it("should rover turn right and direction is North") {
                val command = arrayOf("r")
                val rover = Rover(Position(0, 0, W))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, N)
            }
        }

        on("left command and North direction") {

            it("should rover turn left and direction is West") {
                val command = arrayOf("l")
                val rover = Rover(Position(0, 0, N))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, W)
            }
        }

        on("left command and West direction") {

            it("should rover turn left and direction is South") {
                val command = arrayOf("l")
                val rover = Rover(Position(0, 0, W))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, S)
            }
        }

        on("left command and South direction") {

            it("should rover turn left and direction is East") {
                val command = arrayOf("l")
                val rover = Rover(Position(0, 0, S))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, E)
            }
        }

        on("right left and East direction") {

            it("should rover turn left and direction is North") {
                val command = arrayOf("l")
                val rover = Rover(Position(0, 0, E))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, N)
            }
        }
    }

    describe("given a multiple command ") {

        on("forward right forward command and North direction") {

            it("should rover moves correct position") {
                val command = arrayOf("f","r","f")
                val rover = Rover(Position(0, 0, N))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(1,1, E)
            }
        }
    }

    describe("given a multiple command ") {

        on("forward right forward left back command and South direction") {

            it("should rover moves correct position") {
                val command = arrayOf("f","r","f","l","b")
                val rover = Rover(Position(0, 0, S))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(-1,0, S)
            }
        }
    }

    describe("given a multiple command ") {

        on("forward forward right forward left back command and East direction") {

            it("should rover moves correct position") {
                val command = arrayOf("f","f","r","f","l","b")
                val rover = Rover(Position(0, 0, E))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(2,0, E)
            }
        }
    }

    describe("given a multiple command ") {

        on("forward forward right forward left back command and West direction") {

            it("should rover moves correct position") {
                val command = arrayOf("f","f","r","f","l","b")
                val rover = Rover(Position(0, 0, W))
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(-1, 1, W)
            }
        }
    }

})

class IncorrectPositionException : Exception()
class IncorrectCommandException : Exception()

enum class COMMANDS {
    F, B, R, L
}

private const val N = "N"
private const val E = "E"
private const val S = "S"
private const val W = "W"

private val CARDINAL_POINTS = mapOf(N to 0, E to 1, S to 2, W to 3)

data class Position(val x: Int, val y: Int, val direction: String) {
    init {
        if (CARDINAL_POINTS[direction] == null)
            throw IncorrectPositionException()
    }
}

private val movementMap =
        mapOf(
                N to mapOf(
                        Pair(COMMANDS.F, Pair(0,1)),
                        Pair(COMMANDS.B, Pair(0,-1)),
                        Pair(COMMANDS.R, Pair(0,0)),
                        Pair(COMMANDS.L, Pair(0,0))
                ),
                S to mapOf(
                        Pair(COMMANDS.F, Pair(0,-1)),
                        Pair(COMMANDS.B, Pair(0,1)),
                        Pair(COMMANDS.R, Pair(0,0)),
                        Pair(COMMANDS.L, Pair(0,0))
                ),
                E to mapOf(
                        Pair(COMMANDS.F, Pair(1,0)),
                        Pair(COMMANDS.B, Pair(-1,0)),
                        Pair(COMMANDS.R, Pair(0,0)),
                        Pair(COMMANDS.L, Pair(0,0))
                ),
                W to mapOf(
                        Pair(COMMANDS.F, Pair(-1,0)),
                        Pair(COMMANDS.B, Pair(1,0)),
                        Pair(COMMANDS.R, Pair(0,0)),
                        Pair(COMMANDS.L, Pair(0,0))
                )
        )

class Rover(private val position: Position) {

    private val path = ArrayList<Position>()

    init {
        path.add(position)
    }

    fun move(commands: Array<String>): Position {
        if (commands.isEmpty()) return path.last()

        val validCommands = try {
            validateCommands(commands)
        } catch (e: IncorrectCommandException) {
            throw e
        }

        validCommands.map {
            val validCardinalPosition = try {
                validatePosition(movementMap)
            } catch (e: IncorrectPositionException) {
                throw  e
            }

            val newDirection = calculateDirection(it, path.last().direction)
            val newPosition = Position(path.last().x + validCardinalPosition[it]!!.first, path.last().y + validCardinalPosition[it]!!.second, newDirection)

            path.add(newPosition)
        }

        return path.last()
    }

    private fun calculateDirection(commands: COMMANDS, direction: String): String {

//        val correction = when(commands) {
//            COMMANDS.R -> 1
//            COMMANDS.L -> -1
//            else -> 0
//        }
//
//        val directionValue = CARDINAL_POINTS[direction]!!.plus(correction)
//
//        val newDirection = CARDINAL_POINTS.forEach {
//            if(it.value == directionValue)
//                return it.key
//        }
//
//        return when(directionValue) {
//            0, 1, 2, 3 -> newDirection as String
//            -1 -> W
//            else -> N
//        }

        return when(commands) {
            COMMANDS.R -> when(direction) {
                N -> E
                E -> S
                S -> W
                else -> N
            }
            COMMANDS.L -> when(direction) {
                N -> W
                W -> S
                S -> E
                else -> N
            }
            else -> direction
        }
    }

    private fun validatePosition(moveMap: Map<String, Map<COMMANDS, Pair<Int, Int>>>): Map<COMMANDS, Pair<Int, Int>> {
        return when (moveMap.containsKey(path.last().direction)) {
            true -> moveMap[path.last().direction]!!
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
