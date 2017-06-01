package bminjection.token.PICToken

import bminjection.encrypt.RSA.RSAEncryptTrait
import bminjection.token.AuthTokenTrait
import play.api.libs.json.JsValue

/**
  * Created by alfredyang on 01/06/2017.
  */
trait PICTokenTrait extends RSAEncryptTrait with AuthTokenTrait {
    def encrypt2Token(js : JsValue) : String = {
        null
    }

    def decrypt2JsValue(auth_token : String) : JsValue = {
        null
    }
}
