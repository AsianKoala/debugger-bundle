package sim

import java.lang.Runnable
import java.util.concurrent.Semaphore
import sim.UdpServer
import java.lang.InterruptedException
import java.net.DatagramSocket
import java.net.DatagramPacket
import java.io.IOException
import java.net.InetAddress

class UdpServer(  //the port of the client
        private val clientPort: Int) : Runnable {
    //guards thread collisions
    private val sendLock = Semaphore(1)

    //this is the time of the last update in milliseconds
    private var lastSendMillis: Long = 0

    /**
     * This runs repeatedly (it's own thread). It looks to see if there are any messages to send
     * and if so which to send.
     */
    override fun run() {
        while (true) {
            if (kill) {
                break
            }
            try {
                //never send data too fast
                if (System.currentTimeMillis() - lastSendMillis < 50) {
                    continue
                }
                //set the last send time
                lastSendMillis = System.currentTimeMillis()

                //wait for semaphore to be available
                sendLock.acquire()


                //We will send either the current update or the last update depending on
                //if we are using the currentUpdate String or not
                if (currentUpdate.length > 0) {
                    //send the current update
                    splitAndSend(currentUpdate)
                    //now we scrap everything in currentUpdate to flag it is empty
                    currentUpdate = ""
                } else {
//                    //if we are here, the currentUpdate is empty
//                    if(lastUpdate.length() > 0){
//                        splitAndSend(lastUpdate);
//                        //now we scrap everything in lastUpdate to flag it is empty
//                        lastUpdate = "";
//                    }
                }

                //release the semaphore
                sendLock.release()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * This method uses sendUdpRAW but will split up the message if it is too long
     * @param message the message you wish to send
     */
    fun splitAndSend(message: String) {
//        System.out.println("sending: " + message);
        //these are the ranges we are sending over
        var startIndex = 0
        var endIndex: Int
        do {
            //We will start the end index approximately 600 away. But can't be greater
            //than the message length
            endIndex = Range.clip(startIndex + 600, 0, message.length - 1)


            //Now we will move backwards scanning for the end of this message
            //if this character is a separator, we can mark the end index
            while (message[endIndex] != '%') {
                endIndex-- //move backwards searching for the separator
            }

            //need to add one to the end bound to be inclusive
            sendUdpRAW(message.substring(startIndex, endIndex + 1))

            //start at the next character
            startIndex = endIndex + 1
        } while (endIndex != message.length - 1) //terminate if we have reached the end
    }

    /**
     * This is a prate method to actually send a message over the udp protocol
     * @param message the message you wish to send
     */
    private fun sendUdpRAW(message: String) {
        try {
            DatagramSocket().use { serverSocket ->
                val datagramPacket = DatagramPacket(
                        message.toByteArray(),
                        message.length,
                        InetAddress.getByName("127.0.0.1"),  //194"),
                        clientPort)
                serverSocket.send(datagramPacket)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //These are the double buffering system
    private var lastUpdate = ""
    private var currentUpdate = ""

    /**
     * This will queue a message for sending, utilizing the double buffer
     * @param string the message you wish to send
     */
    fun addMessage(string: String) {
        //depending on the state of the semaphore we can do two things
        if (!sendLock.tryAcquire()) {
            //if it is being used, set the last update
            lastUpdate = string
        } else {
            //we can update the current update if we got past the semaphore
            currentUpdate = string
            //release the semaphore since we have acquired
            sendLock.release()
        }
    }

    companion object {
        var kill = false
    }
}