package info.itseminar.lego.client

import info.itseminar.lego.protocol.Command
import java.net.Socket

fun main(args: Array<String>) {
    val PORT = 4711
    val HOST = "localhost"
    val socket = Socket(HOST, PORT)
    println("connected to $HOST:$PORT")
    val input = socket.getInputStream()
    val output = socket.getOutputStream()
    var running = true

    Thread {
        while (running) {
            val command = Command.from(input)
            println(command)
            if (command is Nothing) running = false
            }
        }.start()

    print(">")
    var line = readLine()
    while (running && line != null) {
        val parts = line.split(" ", limit = 2)
        val command = parts[0]
        val value = if (parts.size > 1) parts[1].toInt() else 0
        when (command) {
            "C" -> Command.Connect(value).to(output)
            "S" -> Command.TrainControl(value).to(output)
            "X" -> running = false
            }
        print(">")
        line = readLine()
        }

    }