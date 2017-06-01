package bminjection.db.MongoDB

import bminjection.db.DBTrait
import play.api.{Configuration, Environment}

class MongoDBModule extends play.api.inject.Module {
    def bindings(env : Environment, conf : Configuration) = Seq(
        bind[DBTrait].toInstance(new MongoDBImpl)
    )
}