package utilities

import java.net.Socket
import java.net.SocketException

object Utils {

  def isPortInUse(port: Int): Boolean = { // Assume no connection is possible.
    var result = false
    try {
      new Socket("0.0.0.0", port).close()
      result = true
    } catch {
      case e: SocketException =>

      // Could not connect.
    }
    result
  }
}
