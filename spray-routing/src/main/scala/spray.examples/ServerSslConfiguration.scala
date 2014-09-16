package spray.examples

import java.io.FileInputStream
import java.security.{SecureRandom, KeyStore}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import spray.io._


/**
 * Created by Alexandre on 01/08/2014.
 */
trait ServerSslConfiguration {

  implicit def sslContext : SSLContext = {
    val keyStorePath = "/keystore.jks"
    val keyStorePassword = "password"

    val keyStore = KeyStore.getInstance("jks")
    val keyStoreStream = getClass.getResourceAsStream(keyStorePath) match {
      case null => new FileInputStream(keyStorePath)
      case resourceStream => resourceStream
    }

    try {
      keyStore.load(keyStoreStream, keyStorePassword.toCharArray)
    } finally {
      keyStoreStream.close()
    }

    //keyStore.load(getClass.getResourceAsStream(keyStoreResource), keyStorePassword.toCharArray)
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(keyStore, keyStorePassword.toCharArray)
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    trustManagerFactory.init(keyStore)

    val context = SSLContext.getInstance("TLS")
    context.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)
    context
  }

  implicit def sslEngineProvider: ServerSSLEngineProvider = {
    ServerSSLEngineProvider { engine =>
      //engine.setNeedClientAuth(true)
      //engine.setWantClientAuth(true)
      engine
    }
  }
}
