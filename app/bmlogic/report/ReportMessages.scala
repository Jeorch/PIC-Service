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
	
	case class msg_PicReportCommand(data: JsValue) extends msg_ReportCommand
}
