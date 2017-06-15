package bmlogic.common

import play.api.libs.json.JsValue

/**
  * Created by qianpeng on 2017/6/15.
  * 采用策略设计模式，根据等级来来确定xiang'fu'ji
  */
object atcLinkage {
	def linkage(value: Map[String, String])
	           (implicit lst: Option[List[Map[String, JsValue]]]): Option[List[Map[String, JsValue]]] ={
		None
	}
}
