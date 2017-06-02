package bmlogic.auth.AuthScopes

/**
  * Created by alfredyang on 02/06/2017.
  */
trait AuthScope extends Enumeration {
    type AuthScope = Value
    val Edge_Scope  = Value(0, "区域权限")
    val Product_Scope = Value(1, "药品权限")
    val Manufacture_Scope = Value(2, "厂家权限")

    trait EdgeScope extends Enumeration {
        type EdgeScope = Value
        val beijing = Value(0, "北京") // 后面你们来添加
    }

    // TODO: 这样的权限不具备扩展性，在考虑
    trait ProductScope extends Enumeration {
        type ProductScope = Value
    }

    // TODO: 这样的权限不具备扩展性，在考虑
    trait ManufactureScope extends Enumeration {
        type ManufactureScope = Value
    }
}
