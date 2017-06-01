package bminjection.encrypt

import java.security.interfaces.{RSAPrivateKey, RSAPublicKey}
import java.security.{PrivateKey, PublicKey}
import javax.crypto.Cipher

/**
  * Created by alfredyang on 01/06/2017.
  */
trait EncryptTrait {
    val publicKey : PublicKey
    val privateKey : PrivateKey
}
