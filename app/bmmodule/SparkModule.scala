package bmmodule

import play.api.inject.Module
import play.api.Environment
import play.api.Configuration

class SparkModule extends play.api.inject.Module {
    def bindings(env : Environment, conf : Configuration) = Seq(
            bind[MongoDBSpark].toSelf
        )
}