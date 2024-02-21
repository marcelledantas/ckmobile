import java.io.IOException
import java.net.InetAddress


object PingUtil {
    fun ping(ipAddress: String?): Boolean {
        return try {
            val inet = InetAddress.getByName(ipAddress)
            inet.isReachable(5000) // 5000 milliseconds timeout
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}