package info.itseminar.lego.server

import java.lang.Math.max
import java.lang.Math.min

class Train(val id: Int) : Runnable {
    companion object {
        private val trains = mutableMapOf<Int, Train>()
        operator fun get(trainId: Int): Train =  trains[trainId] ?: Train(trainId)
        }

    var actualSpeed = 0
    var targetSpeed = 0
    var service: TrainService? = null

    override fun toString() = "Train #$id running $actualSpeed aiming on $targetSpeed"

    init {
        trains[id] = this
        }

    override fun run() {
        //val service = this.service
        while (service != null) {
            Thread.sleep(500)
            if (targetSpeed > actualSpeed) actualSpeed = min(actualSpeed + 10, targetSpeed)
            if (targetSpeed < actualSpeed) actualSpeed = max(actualSpeed - 10, targetSpeed)
            service?.information(actualSpeed, 0, 0, 1u)
            }
        }

    }