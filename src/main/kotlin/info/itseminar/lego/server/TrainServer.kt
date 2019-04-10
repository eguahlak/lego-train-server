package info.itseminar.lego.server

import info.itseminar.lego.protocol.Command
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

var running = true
val PORT = 4711

fun main(args: Array<String>) {
    println("Connecting train server ...")
    val server = ServerSocket(PORT)
    println(InetAddress.getLocalHost())
    println("Listening on port $PORT")
    while (running) {
        val socket = server.accept()
        println("  Client connected")
        Thread(TrainService(socket)).start()
        }
    }

class TrainService(socket: Socket) : Runnable {
    private val input = socket.getInputStream()
    private val output = socket.getOutputStream()
    private var train: Train? = null
    private var running = true

    override fun run() {
        while (running) {
            val command = Command.from(input)
            println(command)
            when (command) {
                is Command.Connect -> {
                    if (train == null) train = connect(Train[command.trainId])
                    else {
                        train = null
                        running = false
                        }
                    }
                is Command.TrainControl -> {
                    if (train == null) Command.NotConnected.to(output)
                    else {
                        train?.targetSpeed = command.speed
                        }
                    }
                is Command.Nothing -> running = false
                }
            }

        }

    fun connect(train: Train): Train? {
        if (train.service == null) {
            train.service = this
            // TODO: Consider this, train should run at all times?
            Thread(train).start()
            return train
            }
        Command.AlreadyConnected.to(output)
        return null
        }

    fun information(speed: Int, trackId: Int, distanceToLight: Int, light: UByte) {
        Command.TrainInformation(speed, trackId, distanceToLight, light).to(output)
        }

    }

