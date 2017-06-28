package bmlogic.report

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by qianpeng on 2017/6/20.
  */
abstract class msg_ReportCommand extends CommonMessage

object ReportMessage {
	case class msg_InertParameterCommand(data: JsValue) extends msg_ReportCommand //插入报告查询条件
	case class msg_ReportParameterSummary(data: JsValue) extends msg_ReportCommand //报告条件查询
	
	case class msg_ReportGraph_One(data: JsValue) extends msg_ReportCommand //图1
	case class msg_ReportGraph_Tow(data: JsValue) extends msg_ReportCommand //图2
	case class msg_ReportGraph_Thr(data: JsValue) extends msg_ReportCommand //图3
	case class msg_ReportGraph_Four(data: JsValue) extends msg_ReportCommand //图4
	case class msg_ReportGraph_Five(data: JsValue) extends msg_ReportCommand //图5
	case class msg_ReportGraph_Six(data: JsValue) extends msg_ReportCommand //图6
	case class msg_ReportGraph_Seven(data: JsValue) extends msg_ReportCommand //图7
	case class msg_ReportGraph_Eight(data: JsValue) extends msg_ReportCommand //图8
	
	/**
	  * 把一个功能点粒化，而不是所有逻辑全部在一个fun里面去写
	  * 对于复杂的关系或逻辑的任务，应该尽量去粒化其功能
	  * 好处1 在业务改变时，你只需要更改其中的一个计算，整体不需要大变
	  * 好处2 每个fun的工作很清晰明了，做什么事情就做什么绝不做其他的计算
	  * 为以后的维护与接管这个项目的人，阅读代码的简易程度有关
	  * 剩下的自己思考，@杨艳媚
	  */
	case class msg_ReportTable_Sales(data: JsValue) extends msg_ReportCommand //表一排名前十企业的Sales
	
	case class msg_ReportTable_ProductUnitCount(data: JsValue) extends msg_ReportCommand //表一排名前十企业的最小产品单位数量
	
	case class msg_ReportTable_Calc(data: JsValue) extends msg_ReportCommand //表一排名前十的占比与增长率的计算
	
	case class msg_ReportTable_One(data: JsValue) extends msg_ReportCommand//表一最终输出数据结构
	
}
