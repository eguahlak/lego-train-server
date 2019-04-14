package info.itseminar.lego.server

import info.itseminar.lego.protocol.Command
import info.itseminar.lego.protocol.Driver
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

var running = true
val PORT = 4711

fun main(args: Array<String>) {
    val world = World()
    world.createTrain(7)
    world.createTrain(9)
    world.createTrain(13)
    world.createTrain(17)
    println("Connecting train server ...")
    val server = ServerSocket(PORT)
    println(InetAddress.getLocalHost())
    println("Listening on port $PORT")
    while (running) {
        val socket = server.accept()
        println("  Client connected")
        Thread(TrainService(socket, world)).start()
        }
    }

class TrainService(socket: Socket, private val world: World) : Runnable {
    private val id = world.nextServiceId()
    private val input = socket.getInputStream()
    private val output = socket.getOutputStream()
    private var train: Train? = null
    private var running = true

    override fun run() {
        while (running) {
            if (train == null) list()
            val command = Command.from(input)
            println(command)
            when (command) {
                is Command.Connect -> connect(command.trainId)
                is Command.TrainControl -> {
                    if (train == null) Command.NotConnected.to(output)
                    else {
                        train?.targetSpeed = command.speed
                        }
                    }
                is Command.TrainBreak -> {
                    if (train == null) Command.NotConnected.to(output)
                    else train?.isBreaking = true
                    }
                is Command.Nothing -> running = false
                }
            }

        }

    fun disconnect() {}

    fun connect(trainId: Int) {
        train = world.trains[trainId]
        if (train == null || train?.service != null) Command.AlreadyConnected.to(output)
        else {
            train!!.service = this
            Thread(train).start()
            }
        }

    fun information(speed: Int, trackId: Int, distanceToLight: Int, light: UByte) {
        Command.TrainInformation(speed, trackId, distanceToLight, light).to(output)
        }

    fun list() {
        Command.TrainList(
          world.trains.values.map { info.itseminar.lego.protocol.Train(it.id, Driver(it.service?.id ?: 0)) }
          ).to(output)
        }

    }

