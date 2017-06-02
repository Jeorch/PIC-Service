package bminjection.token.PICToken

import java.util.Base64

import bminjection.encrypt.RSA.RSAEncryptTrait
import bminjection.token.AuthTokenTrait
import play.api.libs.json.JsValue
import bminjection.encrypt.RSA.RSAUtils

/**
  * Created by alfredyang on 01/06/2017.
  */
trait PICTokenTrait extends RSAEncryptTrait with AuthTokenTrait {
//    def encrypt2Token(js : JsValue) : String = {
    def encrypt2Token(js : String) : String = {
//        bytesToString(encrypt(publicKey, js.toString.getBytes("UTF-8")))
//        val encodedData = RSAUtils.encryptByPublicKey(data, publicKey)

//        val data = js.toString().getBytes("UTF-8")
//        val encodedData = RSAUtils.encryptByPrivateKey(data, privateKey)
//        println("加密后文字：\r\n" + new String(encodedData))
//        new String(encodedData)

        val ret = des_encrypt(js.getBytes("UTF-8"), desKey)
        new String(it.sauronsoftware.base64.Base64.encode(ret))
    }

    def decrypt2JsValue(auth_token : String) : JsValue = {
//        val decodedData = RSAUtils.decryptByPrivateKey(auth_token.getBytes("UTF-8"), privateKey)
//        println(auth_token)
//        val result = this.bytesToString(this.decrypt(this.privateKey, auth_token.getBytes("UTF-8")))
//        println(result)

//        val decodedData = RSAUtils.decryptByPublicKey(auth_token.getBytes("UTF-8"), publicKey)
//        val target = new String(decodedData)

        val ret = it.sauronsoftware.base64.Base64.decode(auth_token)
        val target = new String(des_decrypt(ret.getBytes, desKey), "UTF-8")
        play.api.libs.json.Json.parse(target)
    }
}
