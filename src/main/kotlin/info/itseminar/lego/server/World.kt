package info.itseminar.lego.server

class World {
    val trains = mutableMapOf<Int, Train>()
    private var lastServiceId = 0

    fun nextServiceId() = ++lastServiceId

    fun createTrain(id: Int): Train {
        val train = Train(id, this)
        trains[id] = train
        return train
        }

    fun removeTrain(id: Int) {
        val train = trains[id]
        if (train != null) {
            train.service?.disconnect()
            }
        }

    }