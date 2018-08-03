import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFailsWith

/*
You’re part of the team that explores Mars by sending remotely controlled vehicles to the surface of the planet.
Develop an API that translates the commands sent from earth to instructions that are understood by the rover.

Requirements
You are given the initial starting point (x,y) of a rover and the direction (N,S,E,W) it is facing.
The rover receives a character array of commands.
Implement commands that move the rover forward/backward (f,b).
Implement commands that turn the rover left/right (l,r).
Implement wrapping from one edge of the grid to another. (planets are spheres after all)
Implement obstacle detection before each move to a new square. If a given sequence of commands encounters an obstacle,
the rover moves up to the last possible point, aborts the sequence and reports the obstacle.

Rules
Hardcore TDD. No Excuses!
Change roles (driver, navigator) after each TDD cycle.
No red phases while refactoring.
Be careful about edge cases and exceptions. We can not afford to lose a mars rover, just because the developers overlooked a null pointer.
*/

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
        on("forward command and South direction") {

            it("should rover moves backwards") {
                val command = arrayOf("f")
                val rover = Rover(0, 0, "S")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(0,-1)
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

        on("backward command and South direction") {

            it("should rover moves forwards") {
                val command = arrayOf("b")
                val rover = Rover(0, 0, "S")
                val roverPosition = rover.move(command)
                roverPosition `should equal` Pair(0,1)
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

            it("should throw exception") {
                val command = emptyArray<String>()
                assertFailsWith<IncorrectPositionException> {
                    val rover = Rover(0, 0, "wrong direction")
                    rover.move(command)
                }
            }
        }

    }
})

class IncorrectPositionException : Exception()

class Rover(private val x: Int, private val y: Int, private val direction: String) {
    fun move(commands: Array<String>): Pair<Int, Int> {
        val moveMap = mapOf("N" to Pair(0,1), "S" to Pair(0, -1) )

        val addedPosition = moveMap[direction]!!
        if (commands.isEmpty()) {
            return Pair(x, y)
        }
        return when (commands[0]) {
            "f" -> Pair(x + addedPosition.first, y + addedPosition.second)
            "b" -> Pair(x + addedPosition.first, y - addedPosition.second)
            else -> throw Exception()
        }
    }
}
