package info.itseminar.lego.server

import java.lang.Math.max
import java.lang.Math.min

class Train(val id: Int, val world: World) : Runnable {
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
            if (targetSpeed > actualSpeed) actualSpeed = min(actualSpeed + accelleration, targetSpeed)
            if (targetSpeed < actualSpeed) actualSpeed = max(actualSpeed - accelleration, targetSpeed)
            if (actualSpeed == 0) isBreaking = false
            service?.information(actualSpeed, 0, 0, 1u)
            }
        }

    }