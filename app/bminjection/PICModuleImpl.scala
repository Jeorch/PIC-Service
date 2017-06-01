package bminjection

import bminjection.db.MongoDB.MongoDBImpl
import bminjection.token.PICToken.PICTokenTrait

/**
  * Created by alfredyang on 01/06/2017.
  */
class PICModuleImpl extends PICTokenTrait with MongoDBImpl
