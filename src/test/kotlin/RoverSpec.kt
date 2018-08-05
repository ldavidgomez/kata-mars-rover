import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.file.InvalidPathException
import kotlin.test.assertFailsWith

class RoverSpek: Spek({

    describe("given a map") {

        on("send a wrong size") {

            it("should throw IncorrectSizeException") {
                val end = Pair(-5, -30)
                val obstacles = ArrayList<Pair<Int, Int>>()
                obstacles.add(Pair(20,20))
                assertFailsWith<IncorrectSizeException> {
                    Mapping(end, obstacles)
                }
            }
        }

        on("send a wrong coordinates") {

            it("should throw IncorrectPositionException") {
                val end = Pair(50, 30)
                val obstacles = ArrayList<Pair<Int, Int>>()
                obstacles.add(Pair(70,70))
                assertFailsWith<IncorrectPositionException> {
                    Mapping(end, obstacles)
                }
            }
        }

        on("send a correct coordinates") {

            it("should generate a map") {

                val resultMap = arrayListOf<ArrayList<String>>()

                resultMap.add(arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "))
                resultMap.add(arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "))
                resultMap.add(arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "))
                resultMap.add(arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "))
                resultMap.add(arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "))
                resultMap.add(arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "))
                resultMap.add(arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "))
                resultMap.add(arrayListOf(" ", "O", " ", " ", " ", " ", "O", " ", " ", " "))
                resultMap.add(arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "))
                resultMap.add(arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "))

                val end = Pair(10, 10)
                val obstacles = ArrayList<Pair<Int, Int>>()
                obstacles.add(Pair(2,3))
                obstacles.add(Pair(7,3))
                resultMap.joinToString(separator = "") `should equal` Mapping(end,  obstacles).toString()
            }
        }

        val obstaclesTestCase = arrayListOf(
                Pair(1,1) to " ",
                Pair(6,3) to " ",
                Pair(7,9) to " ",
                Pair(2,3) to "O",
                Pair(7,3) to "O",
                Pair(8,9) to "O"
        )

        obstaclesTestCase.forEach {
            on("send a correct clear coordinates point") {

                it("should return position state clear") {
                    val end = Pair(10, 10)
                    val obstacles = ArrayList<Pair<Int, Int>>()
                    obstacles.add(Pair(2,3))
                    obstacles.add(Pair(7,3))
                    obstacles.add(Pair(8,9))
                    val resultMap = Mapping(end, obstacles)
                    val resultPoint = resultMap.pointState(it.first.first, it.first.second)
                    resultPoint `should equal` it.second

                }
            }
        }
    }

    describe("given a multiple command ") {

        val end = Pair(10, 10)
        val obstacles = ArrayList<Pair<Int, Int>>()
        obstacles.add(Pair(2,3))
        obstacles.add(Pair(7,3))
        obstacles.add(Pair(8,9))
        val mapTestCase = Mapping(end, obstacles)

        on("empty array of commands") {

            it("should not move") {
                val command = emptyArray<String>()
                val rover = Rover(Position(0, 0, S), mapTestCase)
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(0,0, S)
            }
        }

        on("wrong command") {

            it("should throw incorrect command exception") {
                val command = arrayOf("wrong command")
                assertFailsWith<IncorrectCommandException> {
                    val rover = Rover(Position(0, 0, N), mapTestCase)
                    rover.move(command)
                }
            }
        }

        on("wrong cardinalDirection") {

            it("should throw incorrect position exception") {
                val command = arrayOf("r")
                assertFailsWith<IncorrectPositionException> {
                    val rover = Rover(Position(0, 0, "A"), mapTestCase)
                    rover.move(command)
                }
            }
        }

        on("forward right forward command and North cardinalDirection") {

            it("should rover moves correct position") {
                val command = arrayOf("f","r","f")
                val rover = Rover(Position(0, 0, N), mapTestCase)
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(1,1, E)
            }
        }

        on("forward right forward left back command and South cardinalDirection") {

            it("should rover moves correct position") {
                val command = arrayOf("f","r","f","l","b")
                val rover = Rover(Position(0, 0, S), mapTestCase)
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(9,0, S)
            }
        }

        on("forward forward right forward left back command and East cardinalDirection") {

            it("should rover moves correct position") {
                val command = arrayOf("f","f","r","f","l","b")
                val rover = Rover(Position(0, 0, E), mapTestCase)
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(1,9, E)
            }
        }

        on("forward forward right forward left back command and West cardinalDirection") {

            it("should rover moves correct position") {
                val command = arrayOf("f","f","r","f","l","b")
                val rover = Rover(Position(0, 0, W), mapTestCase)
                val roverPosition = rover.move(command)
                roverPosition `should equal` Position(9, 1, W)
            }
        }

        on("forward to position with obstacle") {

            it("should rover not moves and throws IncorrectPathException") {
                val command = arrayOf("f","r","f")
                assertFailsWith<IncorrectPathException> {
                    val rover = Rover(Position(2, 2, N), mapTestCase)
                    rover.move(command)
                    rover.whereAreYou `should equal` Position(2, 2, N)
                }
            }
        }
    }

})


class IncorrectPositionException : Exception()
class IncorrectCommandException : Exception()
class IncorrectSizeException : Exception()
class UnexpectedPointStateException : Exception()
class IncorrectPathException(point: Pair<Int, Int>) : Exception()


enum class COMMANDS {
    F, B, R, L
}

private const val N = "N"
private const val E = "E"
private const val S = "S"
private const val W = "W"

private val CARDINAL_POINTS = mapOf(N to 0, E to 1, S to 2, W to 3)

data class Position(val x: Int, val y: Int, val cardinalDirection: String) {
    init {
        if (CARDINAL_POINTS[cardinalDirection] == null)
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

class Mapping(val size: Pair<Int, Int>, private val obstacles: ArrayList<Pair<Int, Int>>) {

    private val map = arrayListOf<ArrayList<String>>()

    init {
        if(size.first < 0 || size.second < 0)
            throw IncorrectSizeException()

        obstacles.forEach{
            if(it.first < 0 || it.second < 0)
                throw IncorrectPositionException()

            if(it.first > size.first || it.second > size.second)
                throw IncorrectPositionException()
        }

        generateMap()
        print()
    }

    override fun toString(): String {
        return map.joinToString(separator = "")
    }

    private fun generateMap() {
        for(y in size.second downTo 1) {
            val line = ArrayList<String>()
            for(x in 1..size.first) {
                line.add(when(x) {
                    in 1..size.first -> when(obstacles.contains(Pair(x,y))){
                        true -> "O"
                        else -> " "
                    }
                    else -> throw IncorrectSizeException()
                })
            }
            map.add(line)
        }
    }

    private fun print() {
        println()
        println()
        println()
        println("Map starts at x=0 y=0 ends at x=${size.first} y=${size.second}")
        println()
        val limit = ArrayList<String>()
        for(i in 0..size.first + 1)
            limit.add("-")

        println(limit.joinToString(separator = ""))
        map.map {
            println("|${it.joinToString(separator = "")}|")
        }
        println(limit.joinToString(separator = ""))
    }

    fun pointState(x: Int, y: Int): String {
        return when(map[x][y]){
            " " -> " "
            "O" -> "O"
            else -> throw UnexpectedPointStateException()
        }
    }
}


class Rover(private val position: Position, private val mapping: Mapping) {

    private val path = ArrayList<Position>()
    val whereAreYou: Position


    init {
        path.add(position)
        whereAreYou = this.path.last()
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

            val nextPosition = nextPosition(it, validCardinalPosition)

            try {
                isValidPosition(nextPosition)
                path.add(nextPosition)
            } catch(e: InvalidPathException) {
                throw e
            }
        }

        return path.last()
    }

    private fun nextPosition(it: COMMANDS, validCardinalPosition: Map<COMMANDS, Pair<Int, Int>>): Position {
        val newDirection = calculateDirection(it, path.last().cardinalDirection)

        val newPosition = Position(path.last().x + validCardinalPosition[it]!!.first, path.last().y + validCardinalPosition[it]!!.second, newDirection)
        val x = when{
            newPosition.x < 0 -> mapping.size.first - 1
            newPosition.x > mapping.size.first - 1 -> 0
            else -> newPosition.x
        }
        val y = when{
            newPosition.y < 0 -> mapping.size.second - 1
            newPosition.y > mapping.size.second - 1 -> 0
            else -> newPosition.y
        }


        return Position(x, y, newDirection)
    }

    private fun isValidPosition(position: Position): Boolean {
        val correctedX = mapping.size.first -1  - position.x
        val correctedY = mapping.size.second -1 - position.y
        return when(mapping.pointState(correctedX, correctedY)) {
            " " -> true
            else -> throw IncorrectPathException(Pair(position.x, position.y))
        }
    }

    private fun calculateDirection(commands: COMMANDS, direction: String): String {

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
        return when (moveMap.containsKey(path.last().cardinalDirection)) {
            true -> moveMap[path.last().cardinalDirection]!!
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
