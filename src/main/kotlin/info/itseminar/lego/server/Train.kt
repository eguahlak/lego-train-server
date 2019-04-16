package info.itseminar.lego.server

import java.lang.Math.max
import java.lang.Math.min
import java.util.*

class Train(val id: Int, val world: World) : Runnable {
    private val GREEN_GREEN: UByte = 6u
    private val RED_GREEN: UByte = 3u
    private val RED: UByte = 1u
    private var nextLight: UByte = GREEN_GREEN
    private val random = Random()
    private var distanceToLight = 0
    var actualSpeed = 0
    var targetSpeed = 0
    var isBreaking: Boolean = false
        get() = field
        set(value) {
            field = value
            if (value) targetSpeed = 0
            }

    var service: TrainService? = null

    override fun toString() = "Train #$id running $actualSpeed aiming on $targetSpeed"

    override fun run() {
        while (service != null) {
            val accelleration = if (isBreaking) 50 else 10
            Thread.sleep(500)
            if (distanceToLight <= 0) {
              distanceToLight = random.nextInt(2000) + 1000
              nextLight = when (nextLight) {
                GREEN_GREEN -> RED_GREEN
                RED_GREEN -> RED
                else -> GREEN_GREEN
                }
              }
            distanceToLight -= actualSpeed
            if (targetSpeed > actualSpeed) actualSpeed = min(actualSpeed + accelleration, targetSpeed)
            if (targetSpeed < actualSpeed) actualSpeed = max(actualSpeed - accelleration, targetSpeed)
            if (actualSpeed == 0) isBreaking = false
            service?.information(actualSpeed, 0, distanceToLight/100, nextLight)
            }
        }

    }