package sim

import java.text.DecimalFormat
import main.Azusa
import java.lang.StringBuilder
import java.lang.Thread

class ComputerDebugging {
    companion object {
        //this is what actually sends our messages
        private lateinit var udpServer: UdpServer

        //this is what we will use to build the messages
        private var messageBuilder = StringBuilder()

        //use this to format decimals
        private val df = DecimalFormat("#.00")

        /**
         * Sends the robot location to the debug computer
         */
        fun sendRobotLocation(azusa: Azusa) {

            //first send the robot location
            messageBuilder.append("ROBOT,")
            messageBuilder.append(df.format(azusa.getXPos()))
            messageBuilder.append(",")
            messageBuilder.append(df.format(azusa.getYPos()))
            messageBuilder.append(",")
            messageBuilder.append(df.format(azusa.getWorldAngle_rad()))
            messageBuilder.append("%")
        }

        /**
         * Sends the location of any point you would like to send
         * @param floatPoint the point you want to send
         */
        fun sendKeyPoint(floatPoint: FloatPoint) {
            messageBuilder.append("P,")
                    .append(df.format(floatPoint.x))
                    .append(",")
                    .append(df.format(floatPoint.y))
                    .append("%")
        }

        /**
         * This is a point you don't want to clear every update
         * @param floatPoint the point you want to send
         */
        fun sendLogPoint(floatPoint: FloatPoint) {
            messageBuilder.append("LP,")
                    .append(df.format(floatPoint.x))
                    .append(",")
                    .append(df.format(floatPoint.y))
                    .append("%")
        }

        /**
         * Used for debugging lines
         * @param point1
         * @param point2
         */
        fun sendLine(point1: FloatPoint, point2: FloatPoint) {
            //return if not using the computer
            messageBuilder.append("LINE,")
                    .append(df.format(point1.x))
                    .append(",")
                    .append(df.format(point1.y))
                    .append(",")
                    .append(df.format(point2.x))
                    .append(",")
                    .append(df.format(point2.y))
                    .append("%")
        }

        /**
         * This kills the udpServer background thread
         */
        fun stopAll() {
            UdpServer.kill = true
        }

        /**
         * Sends the data accumulated over the update by adding it to the udpServer
         */
        fun markEndOfUpdate() {
            messageBuilder.append("CLEAR,%")

//        udpServer.addMessage(messageBuilder.toString());
            udpServer.splitAndSend(messageBuilder.toString())
            messageBuilder = StringBuilder()
        }

        /**
         * Forces a clear log
         */
        fun clearLogPoints() {
            udpServer.splitAndSend("CLEARLOG,%")
        }
    }

    /**
     * Initializes udp server and starts it's thread
     */
    init {
        UdpServer.kill = false
        udpServer = UdpServer(11115)
        val runner = Thread(udpServer)
        runner.start() //go go go
    }
}