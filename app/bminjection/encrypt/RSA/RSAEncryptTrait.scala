package bminjection.encrypt.RSA

import java.security.{KeyPair, KeyPairGenerator, PrivateKey, PublicKey}

import bminjection.db.DBTrait
import bminjection.encrypt.EncryptTrait
import bmutil.dao.{_data_connection, from}
import com.mongodb.casbah.Imports._

/**
  * Created by alfredyang on 01/06/2017.
  */
trait RSAEncryptTrait extends javaEncryptTrait with EncryptTrait {
    override lazy val (publicKey, privateKey) : (PublicKey, PrivateKey) = {
        (from db() in "config" where ("project" -> "PIC") select (x => x)).toList match {
            case head :: Nil => (head.getAs[PublicKey]("public").get, head.getAs[PrivateKey]("private").get)
            case Nil => {
                val keyPairGen = KeyPairGenerator.getInstance("RSA");
                keyPairGen.initialize(1024);
                val keyPair = keyPairGen.generateKeyPair();

                val builder = MongoDBObject.newBuilder
                builder += "project" -> "PIC"
                builder += "public" -> keyPair.getPublic
                builder += "private" -> keyPair.getPrivate

                _data_connection.getCollection("config") += builder.result

                (keyPair.getPublic, keyPair.getPrivate)
            }
        }
    }
}
