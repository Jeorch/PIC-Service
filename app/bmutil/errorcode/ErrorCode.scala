package bmutil.errorcode

import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.libs.json.JsValue

object ErrorCode {
  	case class ErrorNode(name : String, code : Int, message : String)

  	private def xls : List[ErrorNode] = List(
  		new ErrorNode("input error", -1, "输入的参数有错误"),

		new ErrorNode("get primary key error", -101, "获取主健健值失败"),
		new ErrorNode("primary key error", -102, "主健重复创建或者主键出错"),
		new ErrorNode("data not exist", -103, "数据不存在"),
		new ErrorNode("data duplicate", -104, "搜索结果不唯一，用query multiple搜索"),

		new ErrorNode("product without time", -201, "销售数据没有时间"),
		new ErrorNode("product without province", -202, "销售数据没有省份数据"),
		new ErrorNode("product without sales value", -203, "销售数据没有销售金额数据"),
		new ErrorNode("product without sales units", -204, "销售数据没有销售熟料数据"),
		new ErrorNode("product without oral name", -205, "销售数据没有商品通用名"),
		new ErrorNode("product without manufacture", -206, "销售数据没有生产厂家数据"),
        new ErrorNode("product without specifications", -207, "销售数据没有规格数据"),
        new ErrorNode("product without product unit", -208, "销售数据没有最小产品数据"),
        new ErrorNode("product without manufacture type", -209, "销售数据没有生产厂家类型数据"),
        new ErrorNode("product without product type", -210, "销售数据没有剂型数据"),
        new ErrorNode("product without package", -211, "销售数据没有剂型数据"),
        new ErrorNode("product without sales id", -212, "销售数据没有缺少ID"),
		new ErrorNode("product without category", -213, "销售数据没有分类数据"),

		new ErrorNode("search condition parse error", -301, "搜索条件解析错误"),
		new ErrorNode("calc market size func error", -302, "计算市场销售额出错"),
		new ErrorNode("calc percentage without oral name or product name", -303, "计算市场份额必须提供通用名或者产品名"),
		new ErrorNode("calc market trend func error", -304, "计算市场增长了出错"),

		new ErrorNode("no db connection", -901, "没找到数据库链接"),
		new ErrorNode("db prase error", -902, "数据库结构发现错误"),
		new ErrorNode("no encrypt impl", -903, "权限加密方式不清晰或者Token不存在"),
		new ErrorNode("token parse error", -904, "token数据解析出现错误"),
		new ErrorNode("token expired", -905, "token过期"),
		new ErrorNode("db aggregation error", -906, "数据Map Reduce操作发生错误"),

  		new ErrorNode("unknown error", -999, "unknown error")
  	)
  
  	def getErrorCodeByName(name : String) : Int = (xls.find(x => x.name == name)) match {
  			case Some(y) => y.code
  			case None => -9999
  		}
  	
   	def getErrorMessageByName(name : String) : String = (xls.find(x => x.name == name)) match {
  			case Some(y) => y.message
  			case None => "unknow error"
  		}
   	
   	def errorToJson(name : String) : JsValue =
  		Json.toJson(Map("status" -> toJson("error"), "error" -> 
  				toJson(Map("code" -> toJson(this.getErrorCodeByName(name)), "message" -> toJson(this.getErrorMessageByName(name))))))
}