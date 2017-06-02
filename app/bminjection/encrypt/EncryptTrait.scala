package bminjection.encrypt

import java.security.interfaces.{RSAPrivateKey, RSAPublicKey}

/**
  * Created by alfredyang on 01/06/2017.
  */
trait EncryptTrait {
    // RSA
    val publicKey : String
    val privateKey : String

    // DES
    val desKey : String = "Fuck PIC"
}
