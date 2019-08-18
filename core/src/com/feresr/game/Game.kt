package com.feresr.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class Game : ApplicationAdapter() {

    private val graphics: Graphics by lazy { Graphics(IMG_WIDTH, IMG_HEIGHT) }

    override fun create() {
        graphics.init()
    }

    private var playerX: Float = 1.5f
    private var playerY: Float = 1.5f
    private var playerA: Float = 0f
    private val fov = PI.toFloat() / 4f

    private var t1 = System.currentTimeMillis()
    private var t2 = System.currentTimeMillis()
    private var elapsed = 0L

    private val map: List<List<Char>> =
            listOf(
                    listOf('#', '#', '#', '#', '#', '#', '#', '#'),
                    listOf('#', '_', '_', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '#', '_', '#'),
                    listOf('#', '#', '_', '#', '#', '#', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '#', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '#', '_', '_', '_', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '#', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '#', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '#', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '#', '_', '#'),
                    listOf('#', '_', '_', '_', '_', '#', '_', '#'),
                    listOf('#', '#', '#', '#', '#', '#', '#', '#')
            )

    override fun render() {
        t2 = System.currentTimeMillis()
        elapsed = t2 - t1
        t1 = t2

        doMovements()

        var rayAngle = playerA - fov / 2
        val angleStep = fov / IMG_WIDTH
        for (x in 0 until IMG_WIDTH) {
            var distance = 100f
            val distanceStep = .1f
            for (i in 1..200) {
                val rayX = playerX + i * distanceStep * cos(rayAngle)
                val rayY = playerY + i * distanceStep * sin(rayAngle)

                if (rayX < 0 || rayX >= map[0].size) break
                if (rayY < 0 || rayY >= map.size) break
                if (map[rayY.toInt()][rayX.toInt()] == '#') {
                    val rx = i * distanceStep * cos(rayAngle)
                    val ry = i * distanceStep * sin(rayAngle)
                    distance = sqrt(rx * rx + ry * ry)
                    break
                }
            }

            val ceiling = ((IMG_HEIGHT / 2) - IMG_HEIGHT / distance).toInt()
            val floor = IMG_HEIGHT - ceiling

            val wallPaint = (floor - ceiling).coerceIn(0, 255)

            for (y in 0 until ceiling) {
                if (y in 0 until IMG_HEIGHT) {
                    val paint = (120  - y).coerceIn(0, 255)
                    graphics.setPixel(x, y, paint, paint, paint)
                }
            }
            for (y in ceiling until floor) {
                if (y in 0 until IMG_HEIGHT) {
                    graphics.setPixel(x, y, wallPaint, wallPaint - 20, wallPaint - 20)
                }
            }
            for (y in floor until IMG_HEIGHT) {
                if (y in 0 until IMG_HEIGHT) {
                    val paint = ((y - 120)).coerceIn(0, 255)
                    graphics.setPixel(
                            x,
                            y,
                            (paint - 20).coerceIn(0, 255),
                            paint.coerceIn(0, 255),
                            (paint + 20).coerceIn(0, 255))
                }
            }

            rayAngle += angleStep
        }
        graphics.render()
    }


    private fun doMovements() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            val futureX = playerX + cos(playerA) * .004f * elapsed
            val futureY = playerY + sin(playerA) * .004f * elapsed
            if (map[futureY.toInt()][futureX.toInt()] != '#') {
                playerX = futureX
                playerY = futureY
            }
            println("x:$playerX, y:$playerY")
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            val futureX = playerX - cos(playerA) * .004f * elapsed
            val futureY = playerY - sin(playerA) * .004f * elapsed
            if (map[futureY.toInt()][futureX.toInt()] != '#') {
                playerX = futureX
                playerY = futureY
            }
            println("x:$playerX, y:$playerY")
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) playerA -= .001f * elapsed
        if (Gdx.input.isKeyPressed(Input.Keys.D)) playerA += .001f * elapsed
    }

    override fun dispose() {
        graphics.dispose()
    }

    companion object {
        private const val IMG_WIDTH = 256
        private const val IMG_HEIGHT = 256
    }

}
