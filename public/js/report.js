/**
 * 全局变量变量
 */
var yearBarObj,yearScatterObj,yearXseBarObj,yearMapQltObj,yearAtcXseBarObj,yearMiniUnitXseZbObj,yearTop10XseZzlZbObj,yearTop10XseZbObj;

var a,b,c,d,e,f,g;

/*****************************我是分割线**********************************/
var top10linp;
$(function() {
	
});


function loadEchartsObj(){
	var myDate = new Date();  
	$(".reportTime").text(myDate.getFullYear()+"年"+(myDate.getMonth()+1)+"月");
	yearBarObj = echarts.init(document.getElementById('yearBar'), "vintage");
	yearScatterObj = echarts.init(document.getElementById('yearScatter'), "vintage");
	yearMapQltObj = echarts.init(document.getElementById('yearMapQlt'), "vintage");
	yearXseBarObj = echarts.init(document.getElementById('yearBarXse'), "vintage");
	yearAtcXseBarObj = echarts.init(document.getElementById('yearBarAtcXse'), "vintage");
	yearMiniUnitXseZbObj = echarts.init(document.getElementById('yearMiniUnitXseZb'), "vintage");
	yearTop10XseZzlZbObj =  echarts.init(document.getElementById('yearTop10XseZzlZb'), "vintage");
	yearTop10XseZbObj = echarts.init(document.getElementById('yearTop10XseZb'), "vintage");
	yearBarReport();
	yearScatterReport();
	yearXseBarReport();
	yearAtcXseBarReport();
	yearMiniUnitXseZbBarReport();
	yearTop10XseZzlZbReport();
	yearTop10XseZbReport();
}


/**
 * 搜索
 */
function serach(){
	if(pageTypeIndex != "dataGridPage"){
		$(".excelReportLoad").attr("class","excelReportLoad excelDataLoad");
	}
	var ATC1 = $("#ATC1").select2("data")[0];
	var ATC2 = $("#ATC2").select2("data")[0];
	var ATC3 = $("#ATC3").select2("data")[0];
	var genericnameinfo = $("#genericnameinfo").select2("data")[0];
	var product = $("#product").select2("data")[0];
	var province = $("#province").select2("data")[0];
	var manufacturetype = $("#manufacturetype").select2("data")[0];
	var manufacture = $("#manufacture").select2("data")[0];
	var dosage = $("#dosage").select2("data")[0];
	var specification = $("#specification").select2("data")[0];
	var packages = $("#package").select2("data")[0];
	var timeType = $("#timeType").val();//时间类型 年 月 还是季度
	var time;//时间
	var tempStr = new StringBuffer();
	tempStr.append("{");
	if(ATC1.id != null && ATC1.id != ""){
		//tempStr.append("\"interatc1id\":\""+ATC1.split(",")[0]+"\",");
		tempStr.append("\"atcType\":\"1\",");//属于ATC什么类型的  分级 最高还是通用名级别的
		tempStr.append("\"codeid\":\""+ATC1.id.split(",")[0]+"\",");//下拉选择的ID
		tempStr.append("\"atcName\":\""+ATC1.text+"\",");//下拉选择的文字
	}
	if(ATC2.id != null && ATC2.id != ""){
		//tempStr.append("\"interatc2id\":\""+ATC2.split(",")[0]+"\",");
		tempStr.append("\"parentcode\":\""+ATC2.id.split(",")[1]+"\",");
		tempStr.append("\"atcType\":\"2\",");
		tempStr.append("\"codeid\":\""+ATC2.id.split(",")[0]+"\",");
		tempStr.append("\"atcName\":\""+ATC2.text+"\",");//下拉选择的文字
	}
	if(ATC3.id != null && ATC3.id != ""){
		//tempStr.append("\"interatc3id\":\""+ATC3.split(",")[0]+"\",");
		tempStr.append("\"parentcode\":\""+ATC3.id.split(",")[1]+"\",");
		tempStr.append("\"atcType\":\"3\",");
		tempStr.append("\"codeid\":\""+ATC3.id.split(",")[0]+"\",");
		tempStr.append("\"atcName\":\""+ATC3.text+"\",");//下拉选择的文字
	}
	if(genericnameinfo.id != null && genericnameinfo.id != ""){
		//tempStr.append("\"genericnameid\":\""+genericnameinfo.split(",")[0]+"\",");
		tempStr.append("\"parentcode\":\""+genericnameinfo.id.split(",")[1]+"\",");
		tempStr.append("\"atcType\":\"4\",");
		tempStr.append("\"codeid\":\""+genericnameinfo.id.split(",")[0]+"\",");
		tempStr.append("\"atcName\":\""+genericnameinfo.text+"\",");//下拉选择的文字
	}
	if(product.id != null && product.id != ""){
		tempStr.append("\"atcType\":\"5\",");
		//tempStr.append("\"codeid\":\""+product.id+"\",");
		if(genericnameinfo.id.split(",")[1] != undefined){
			tempStr.append("\"parentcode\":\""+genericnameinfo.id.split(",")[1]+"\",");
		}else{
			tempStr.append("\"codeid\":\""+upperloader(product.id).split(",")[0]+"\",");
			tempStr.append("\"parentcode\":\""+upperloader(product.id).split(",")[1]+"\",");
		}
		tempStr.append("\"productnameid\":\""+product.id+"\",");
	}
	if(province.id != null && province.id != ""){
		//tempStr.append("\"atcType\":\"6\",");
		tempStr.append("\"provinceid\":\""+province.id+"\",");
	}
	if(manufacturetype.id != null && manufacturetype.id != ""){
		tempStr.append("\"atcType\":\"7\",");
		tempStr.append("\"manutypeid\":\""+manufacturetype.id+"\",");
	}
	if(manufacture.id != null && manufacture.id != ""){
		tempStr.append("\"atcType\":\"8\",");
		tempStr.append("\"manuid\":\""+manufacture.id+"\",");
	}
	if(dosage.id != null && dosage.id != ""){
		tempStr.append("\"atcType\":\"9\",");
		tempStr.append("\"dosageformid\":\""+dosage.id+"\",");
	}
	if(specification.id != null && specification.id != ""){
		tempStr.append("\"atcType\":\"10\",");
		tempStr.append("\"specificationsid\":\""+specification.id+"\",");
	}
	if(packages.id != null && packages.id != ""){
		tempStr.append("\"atcType\":\"11\",");
		tempStr.append("\"packagingid\":\""+packages.id+"\",");
	}
	if(timeType == 1){
		time = $("#yearInputb").val();//年
		if(time != null && time != ""){//由于数据不全所以暂时限制时间输入
			var a = time >= 2016 ? 2015 : time;
			tempStr.append("\"salesYear\":\""+a+"\",");
		}else{tempStr.append("\"salesYear\":\""+2015+"\",");}
	}/*else if(timeType == 2){
		time = $("#monthInputb").val();//月
		tempStr.append("{\"year\":\""+time+"\"},")
	}else{
		var timeByQuarterSelect = $("#quarterSelect").val();//季度的下拉框
		time = $("#quarterInput").val();//季度上的年份
		tempStr.append("{\"year\":\""+time+"\"},")
	}*/
	tempStr.append("}");
	var str = tempStr.toString();
	var tempMap;
	if(str.lastIndexOf(",") > -1){
		tempMap = eval("("+str.substring(0,str.lastIndexOf(","))+"}"+")");
	}else{
		tempMap = eval("("+str+")");
	}
	if(pageTypeIndex == "reportPage"){
		titlename(tempMap);
		topFour();
		if(isSerachValdations(tempMap)){
			yearBarReport(tempMap);
			if(province.text == "全国汇总"){
				$(".btprovince").hide();
				$("#four").empty().text("2");
				$("#five").empty().text("3");
				$("#six").empty().text("4");
				$("#seven").empty().text("5");
				$("#eight").empty().text("6");
				
			}else{
				$(".btprovince").show();
				$("#four").empty().text("4");
				$("#five").empty().text("5");
				$("#six").empty().text("6");
				$("#seven").empty().text("7");
				$("#eight").empty().text("8");
				yearScatterReport(tempMap);
			}
			yearXseBarReport(tempMap);
			if(tempMap.atcType == 3){
				$(".img7").show();
				if(province.text == "全国汇总"){
					$("#eight").empty().text("6");
				}else{
					$("#eight").empty().text("8");
				}
				yearAtcXseBarReport(tempMap);
			}else{
				$(".img7").hide();
				if(province.text == "全国汇总"){
					$("#eight").empty().text("5");
				}else{
					$("#eight").empty().text("7");
				}
			}
			yearMiniUnitXseZbBarReport(tempMap);
			yearTop10XseZzlZbReport(tempMap);
			reportDataList1(tempMap);
		}
		yearTop10XseZbReport(tempMap);
		if(tempMap != undefined){
			$.when(a,b,c,d,e,f,g).then(function(){
				$(".excelReportLoad").attr("class","excelReportLoad loadNone");
			})
		}
	}else if(pageTypeIndex == "dataGridPage"){
		topFour();
	}
}

/***
 * 根据特定的字符查询上级的类别code
 * @param str1 参数
 * @returns
 */
function upperloader(str1){
	var temp = "";
	$.ajax({
		type:"POST",
		url:"../leftData/queryUpperloader",
		data:{"id":str1},
		async:false,
		success:function(data){
			temp = data[0].lid+","+data[0].parentCode;
		}
	});
	return temp;
}

/**
 * 更新报告页面上的文字联动
 * @returns
 */
function titlename(tempMap){
	var ATC1 = $("#ATC1").select2("data")[0];
	var ATC2 = $("#ATC2").select2("data")[0];
	var ATC3 = $("#ATC3").select2("data")[0];
	var genericnameinfo = $("#genericnameinfo").select2("data")[0];
	$(".reporttitlename").empty();
	if(ATC1.id != null && ATC1.id != ""){
		$(".reporttitlename").append(" • "+ATC1.text);
	}
	if(ATC2.id != null && ATC2.id != ""){
		$(".reporttitlename").append(" • "+ATC2.text);
	}
	if(ATC3.id != null && ATC3.id != ""){
		$(".reporttitlename").append(" • "+ATC3.text);
	}
	if(genericnameinfo.id != null && genericnameinfo.id != ""){
		$(".reporttitlename").append(" • "+genericnameinfo.text);
	}
	$("#xiaojieatcname").text(tempMap.atcName);
//	$(".biaoti").text(tempMap.atcName);
}

function sortNumber1(x,y){
	return  y[0] - x[0]
}

function sortNumber2(x,y){
	return y[1] -  x[1];
}

function sortNumber3(x,y){
	return x[0] -  y[0];
}

/**
 * 只针对于Left的治疗I至通用名的检验
 */
function isSerachValdations(tempMap){
	if(tempMap.atcType != undefined){
		return true;
	}else{
		alertDialog("至少在治疗I至通用名中选中一个");
		return false;
	}
}

/**
 * 顶部四个显示
 * @param tempMap
 */
function topFour(){
	var tempStr = new StringBuffer();
	var ATC1 = $("#ATC1").select2("data")[0];
	var ATC2 = $("#ATC2").select2("data")[0];
	var ATC3 = $("#ATC3").select2("data")[0];
	var genericnameinfo = $("#genericnameinfo").select2("data")[0];
	var product = $("#product").select2("data")[0];
	var timeType = $("#timeType").val();//时间类型 年 月 还是季度
	var time;//时间
	tempStr.append("{");
	if(ATC1.id != null && ATC1.id != ""){
		//tempStr.append("\"interatc1id\":\""+ATC1.split(",")[0]+"\",");
		tempStr.append("\"atcType\":\"1\",");//属于ATC什么类型的  分级 最高还是通用名级别的
		tempStr.append("\"codeid\":\""+ATC1.id.split(",")[0]+"\",");//下拉选择的ID
		tempStr.append("\"atcName\":\""+ATC1.text+"\",");//下拉选择的文字
	}
	if(ATC2.id != null && ATC2.id != ""){
		//tempStr.append("\"interatc2id\":\""+ATC2.split(",")[0]+"\",");
		tempStr.append("\"parentcode\":\""+ATC2.id.split(",")[1]+"\",");
		tempStr.append("\"atcType\":\"2\",");
		tempStr.append("\"codeid\":\""+ATC2.id.split(",")[0]+"\",");
		tempStr.append("\"atcName\":\""+ATC2.text+"\",");//下拉选择的文字
	}
	if(ATC3.id != null && ATC3.id != ""){
		//tempStr.append("\"interatc3id\":\""+ATC3.split(",")[0]+"\",");
		tempStr.append("\"parentcode\":\""+ATC3.id.split(",")[1]+"\",");
		tempStr.append("\"atcType\":\"3\",");
		tempStr.append("\"codeid\":\""+ATC3.id.split(",")[0]+"\",");
		tempStr.append("\"atcName\":\""+ATC3.text+"\",");//下拉选择的文字
	}
	if(genericnameinfo.id != null && genericnameinfo.id != ""){
		//tempStr.append("\"genericnameid\":\""+genericnameinfo.split(",")[0]+"\",");
		tempStr.append("\"parentcode\":\""+genericnameinfo.id.split(",")[1]+"\",");
		tempStr.append("\"atcType\":\"4\",");
		tempStr.append("\"codeid\":\""+genericnameinfo.id.split(",")[0]+"\",");
		tempStr.append("\"atcName\":\""+genericnameinfo.text+"\",");//下拉选择的文字
	}
	if(product.id != null && product.id != ""){
		tempStr.append("\"atcType\":\"5\",");
		//tempStr.append("\"codeid\":\""+product.id+"\",");
		if(genericnameinfo.id.split(",")[1] != undefined){
			tempStr.append("\"parentcode\":\""+genericnameinfo.id.split(",")[1]+"\",");
		}else{
			tempStr.append("\"codeid\":\""+upperloader(product.id).split(",")[0]+"\",");
			tempStr.append("\"parentcode\":\""+upperloader(product.id).split(",")[1]+"\",");
		}
		tempStr.append("\"productnameid\":\""+product.id+"\",");
	}
	if(timeType == 1){
		time = $("#yearInputb").val();//年
		if(time != null && time != ""){//由于数据不全所以暂时限制时间输入
			var a = time >= 2016 ? 2015 : time;
			tempStr.append("\"salesYear\":\""+a+"\",");
		}else{tempStr.append("\"salesYear\":\""+2015+"\",");}
	}/*else if(timeType == 2){
		time = $("#monthInputb").val();//月
		tempStr.append("{\"year\":\""+time+"\"},")
	}else{
		var timeByQuarterSelect = $("#quarterSelect").val();//季度的下拉框
		time = $("#quarterInput").val();//季度上的年份
		tempStr.append("{\"year\":\""+time+"\"},")
	}*/
	tempStr.append("}");
	var str = tempStr.toString();
	var tempMap;
	if(str.lastIndexOf(",") > -1){
		tempMap = eval("("+str.substring(0,str.lastIndexOf(","))+"}"+")");
	}else{
		tempMap = eval("("+str+")");
	}
	
	$(".topfourdiv").attr("class","topfourdiv topFourLoad");
	$("#guim").empty();
	$("#zengzl").empty();
	$("#fene").empty();
	$("#chanps").empty();
	$.post("../topFourData/query",tempMap,function(data){
		if(data != undefined && data != null && data != ""){
			$("#guim").attr("title",accounting.formatNumber(data[0].guim)+" 元");
			$("#guim").append(accounting.formatNumber(data[0].guim));
			$("#zengzl").attr("title",data[0].zengzl+" %");
			$("#zengzl").append(data[0].zengzl == 0 ? "" : data[0].zengzl+" %");
			$("#fene").attr("title",data[0].fene == 100 ? "" : data[0].fene+" %");
			$("#fene").append(data[0].fene == 100 ? "" : data[0].fene+" %");
			$("#chanps").attr("title",accounting.formatNumber(data[0].chanps)+" 个");
			$("#chanps").append(accounting.formatNumber(data[0].chanps));
		}else{
			$("#guim").append("");
			$("#zengzl").append("");
			$("#fene").append("");
			$("#chanps").append("");
		}
		$(".topfourdiv").attr("class","topfourdiv loadNone");
	});
}


/**
 * 增长率柱状图
 */
function yearBarReport(tempMap) {
	yearBarObj = null;
	$("#yearBar").empty();
	yearBarObj = echarts.init(document.getElementById('yearBar'), "vintage");
	
	yearBarObj.showLoading();
	yearBarObj.setOption({
		title : {
            text: '销售额表现',
            //subtext: '纯属虚构',
            left: 'center',
            top:'7%'
        },
		tooltip: {
	        trigger: 'axis',
	        formatter : "{b}<br/>{a0}：{c0}亿元<br/>{a1}：{c1}%",
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
	    },
	    toolbox: {
	    	orient: 'vertical',
	        feature: {
	            magicType: {show: true, type: ['line', 'bar']},
	            restore: {show: true},
	            saveAsImage: {show: true}
	        }
	    },
	    legend: {
	        data:['销售金额','增长率']
	    },
	    xAxis: [
	        {
	            type: 'category',
	            data: []
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value',
	            name: '销售金额',
	            min: 0,
	            //interval: 100,
	            axisLabel: {
	                formatter: '{value}亿'
	            }
	        },
	        {
	            type: 'value',
	            name: '增长率',
	            min: 0,
	            interval: 2,
	            axisLabel: {
	                formatter: '{value} %'
	            }
	        }
	    ],
	    series: [{ name:'销售金额', type:'bar', data:[]},
	             { name:'增长率',type:'line',yAxisIndex: 1,data:[]}
	    ]
	});
	if(tempMap != undefined){
		a = $.post("../reportMedical/queryByReportLjxsZzlByYear",tempMap,function(data){
			var obj = eval("("+data+")");
			if(obj != null && obj.data != undefined && obj.data == "暂无数据"){
				yearBarObj.hideLoading();
				return ;
			}
			if(obj != null && obj != ""){
				var yearData = [];
				var salesValueData = [];
				var growthRateData = [];
				$.each(obj,function(i,v){
					yearData.push(v.year);
					salesValueData.push(parseFloat(v.salesValue).toFixed(2));
					growthRateData.push(isNaN(parseFloat(v.growthRate)) ? "" : parseFloat(v.growthRate).toFixed(2));
				});
				yearBarObj.setOption({
					xAxis: [{type: 'category', data: yearData}],//interval: 2,
					    yAxis: [{type: 'value', name: '销售金额',min: 0,axisLabel: {formatter: '{value}亿'}},
					    		{type: 'value',name: '增长率',min: 0,interval: 2,axisLabel: {formatter: '{value} %'}}
					    	],
					    series: [{name:'销售金额',type:'bar',data:salesValueData,itemStyle:{
			                normal:{
				                  label:{
				                    show:true,
				                    formatter:function(value){
				                      return  value.data + "亿元"
				                    }
				                  }
				                }
				            }},
					             {name:'增长率',type:'line',yAxisIndex: 1,data:growthRateData},]
				});
			}
			var timeObj = yearBarObj.getOption().xAxis[0].data;
			$(".yiyuanxiaojtime").eq(0).text(timeObj[0]);
			$(".yiyuanxiaojtime").eq(1).text(timeObj[timeObj.length-1]);
			$(".yiyuanxiaojtime").eq(2).text(timeObj[timeObj.length-1]);
			$("#xiaoJsumValue").text(yearBarObj.getOption().series[0].data[yearBarObj.getOption().series[0].data.length-1]);
			
			//把增长率的空值排除掉方便排序
			var temp = [];
			$.each(yearBarObj.getOption().series[1].data,function(i,v){
				temp.push(v);
			});
			var m = maopao(temp);
			$(".yiyuanxiaojzzl").eq(0).text(m[0] == "" ? "" : m[0] + "%");
			$(".yiyuanxiaojzzl").eq(1).text(m[m.length-1]+"%");
			/********我是分割线*********/
			$(".yiyuanzongtibxtime").eq(0).text(timeObj[0]+"-"+timeObj[timeObj.length-1]);
			$(".yiyuanzongtibxtime").eq(1).text(timeObj[timeObj.length-1]);
			$(".yiyuanzongtibxatcname").text(tempMap.atcName);
			$(".yiyuanzongtibxvalue").text(yearBarObj.getOption().series[0].data[yearBarObj.getOption().series[0].data.length-1]);
			$(".yiyuanzongtibxzzl").text(m[m.length-1]+"%");
			
			var fzzFlag = 0;//计数负增长率
			var normal = 0;//计数正常的增长率
			var pingwen = 0;//计数平稳上升的增长率
			var shangsheng = 0;//计数有上升趋势的增长率
			var xunsu = 0;//记录有迅速上升的趋势的增长率 
			var a = (yearBarObj.getOption().series[0].data[yearBarObj.getOption().series[0].data.length-1]-yearBarObj.getOption().series[0].data[0])/yearBarObj.getOption().series[0].data[0]*100;
			if(a >= 0 && a <= 10){
				normal++;
				pingwen++;
			}else if(a > 10 && a <= 100){
				normal++;
				shangsheng++;
			}else if(a > 100){
				normal++;
				xunsu++;
			}else{
				fzzFlag++;
			}
			
			if(fzzFlag > normal){
				$(".yiyuanzongtibxqs").text("负增长");
			}else{
				if(pingwen > shangsheng && pingwen > xunsu){
					$(".yiyuanzongtibxqs").text("基本稳定");
				}else if(shangsheng > pingwen && shangsheng > xunsu){
					$(".yiyuanzongtibxqs").text("上升");
				}else if(xunsu > pingwen && xunsu > pingwen){
					$(".yiyuanzongtibxqs").text("迅速上升");
				}else{
					$(".yiyuanzongtibxqs").text("基本稳定");
				}
			}
			$(".img1time").text(timeObj[0]+"-"+timeObj[timeObj.length-1]);	
			$(".img1atcname").text(tempMap.atcName);
			yearBarObj.hideLoading();
		});
	}
}

/**
 * 占比和销售额复合增长率 散点图
 */
function yearScatterReport(tempMap){
	yearMapQlyReport();
	yearScatterObj = null;
	$("#yearScatter").empty();
	yearScatterObj = echarts.init(document.getElementById('yearScatter'), "vintage");
	yearScatterObj.showLoading();
	yearMapQltObj.showLoading();
	var yearYaxis = "null";
	yearScatterObj.setOption({
		title : {
            text: '大小表示2013年各省份卫生总费用（亿元）',
            //subtext: '纯属虚构',
            left: 'center'//,
            //top:'7%'
        },
		tooltip: {
            trigger: 'axis',
            formatter: function(params) {
                return params.value[2] + '<br/>占比：'+params.value[0] + '%<br/>复合增长率：' +params.value[1] + '%<br/>' ;//+ params.value[3]
            },
            axisPointer: {
                type: 'cross',
                lineStyle: {
                    type: 'dashed',
                    width: 1
                }
            }
        },
        toolbox: {
        	orient: 'vertical',
	        feature: {
	            saveAsImage: {show: true},
	            restore : {show: true}
	        }
	    },
        xAxis: [{
            type: 'value',
            nameLocation: 'middle',
            nameGap: 25,
            name: '年各省销售占比(%)'
        }],
        yAxis: [{
            type: 'value',
            name: '年销售额复合增长率(%)',
            nameLocation:'middle',
            nameGap:45
            //splitNumber: 5 ,
        }],
        animation: true,
        series: [{
            type: 'scatter',
            data: []
        }]
	});
	if(tempMap != undefined){
		var tempMap2;
		if(tempMap.atcType > 4 && $("#genericnameinfo").select2("data")[0].id != null && $("#genericnameinfo").select2("data")[0].id != ""){
			var ATC1 = $("#ATC1").select2("data")[0];
			var ATC2 = $("#ATC2").select2("data")[0];
			var ATC3 = $("#ATC3").select2("data")[0];
			var genericnameinfo = $("#genericnameinfo").select2("data")[0];
//			var product = $("#product").select2("data")[0];
			var province = $("#province").select2("data")[0];
			var manufacturetype = $("#manufacturetype").select2("data")[0];
			var manufacture = $("#manufacture").select2("data")[0];
//			var dosage = $("#dosage").select2("data")[0];
//			var specification = $("#specification").select2("data")[0];
//			var packages = $("#package").select2("data")[0];
			var timeType = $("#timeType").val();//时间类型 年 月 还是季度
			var time;//时间
			
			var map = new StringBuffer();
			map.append("{");
			if(ATC1.id != null && ATC1.id != ""){
				map.append("\"atcType\":\"1\",");//属于ATC什么类型的  分级 最高还是通用名级别的
				map.append("\"codeid\":\""+ATC1.id.split(",")[0]+"\",");//下拉选择的ID
				map.append("\"atcName\":\""+ATC1.text+"\",");//下拉选择的文字
			}
			if(ATC2.id != null && ATC2.id != ""){
				map.append("\"parentcode\":\""+ATC2.id.split(",")[1]+"\",");
				map.append("\"atcType\":\"2\",");
				map.append("\"codeid\":\""+ATC2.id.split(",")[0]+"\",");
				map.append("\"atcName\":\""+ATC2.text+"\",");//下拉选择的文字
			}
			if(ATC3.id != null && ATC3.id != ""){
				map.append("\"parentcode\":\""+ATC3.id.split(",")[1]+"\",");
				map.append("\"atcType\":\"3\",");
				map.append("\"codeid\":\""+ATC3.id.split(",")[0]+"\",");
				map.append("\"atcName\":\""+ATC3.text+"\",");//下拉选择的文字
			}
			if(genericnameinfo.id != null && genericnameinfo.id != ""){
				map.append("\"parentcode\":\""+genericnameinfo.id.split(",")[1]+"\",");
				map.append("\"atcType\":\"4\",");
				map.append("\"codeid\":\""+genericnameinfo.id.split(",")[0]+"\",");
				map.append("\"atcName\":\""+genericnameinfo.text+"\",");//下拉选择的文字
			}
			
			if(province.id != null && province.id != ""){
				map.append("\"atcType\":\"6\",");
				map.append("\"provinceid\":\""+province.id+"\",");
			}
			if(manufacturetype.id != null && manufacturetype.id != ""){
				map.append("\"atcType\":\"7\",");
				map.append("\"manutypeid\":\""+manufacturetype.id+"\",");
			}
			if(manufacture.id != null && manufacture.id != ""){
				map.append("\"atcType\":\"8\",");
				map.append("\"manuid\":\""+manufacture.id+"\",");
			}
			if(timeType == 1){
				time = $("#yearInputb").val();//年
				if(time != null && time != ""){
					map.append("\"salesYear\":\""+time+"\",");
				}
			}/*else if(timeType == 2){
				time = $("#monthInputb").val();//月
				map.append("{\"year\":\""+time+"\"},")
			}else{
				var timeByQuarterSelect = $("#quarterSelect").val();//季度的下拉框
				time = $("#quarterInput").val();//季度上的年份
				map.append("{\"year\":\""+time+"\"},")
			}*/
			map.append("}");
			var str = map.toString();
			if(str.lastIndexOf(",") > -1){
				tempMap2 = eval("("+str.substring(0,str.lastIndexOf(","))+"}"+")");
			}else{
				tempMap2 = eval("("+str+")");
			}
			tempMap = tempMap2;
		}
		b = $.post("../reportMedical/queryByReportFhzzlByYear",tempMap,function(data){  // 求乘方 Math.pow(2,3)
			var mapData = [];
			var str;
			if(data == null || data == undefined || data == ""){
				yearScatterObj.hideLoading();
				yearMapQltObj.hideLoading();
				yearMapQltObj.setOption({
			        series : [
			            {
			                type: 'map',
			                map: 'china',
			                itemStyle: itemStyle,
			                showLegendSymbol: true,
			                label: {
			                    normal: {
			                        show: true
			                    },
			                    emphasis: {
			                        show: true
			                    }
			                },
			                data:[]
			            }
			        ]
				});
				return ;
			}
			
			var data1 = [];
			$.each(data,function(i,v){
				if(i==0){yearYaxis = v.year}
				var data2 = [];
				data2.push(v.zb.toFixed(2));
				data2.push(v.ffzzl.toFixed(2));
				data2.push(v.provinceName);
				data1.push(data2);
			});
			yearScatterObj.setOption({
				xAxis: [{
		            type: 'value',
		            nameLocation: 'middle',
		            nameGap: 25,
		            name: '年各省销售占比(%)'
		        }],
		        yAxis: [{
		            type: 'value',
		            name: yearYaxis+'年销售额复合增长率(%)',
		            nameLocation:'middle',
		            nameGap:45
		            //splitNumber: 5 ,
		        }],
				series: [{
		            type: 'scatter',
		            itemStyle:{
		                normal:{
		                  label:{
		                    show:true,
		                    formatter:function(value){
		                      return "——" + value.data[2]
		                    },
		                    position: 'right',
		                    textStyle: {
		                	  color:'#000'
		                  	}
		                  }
		                }
		            },
		            symbolSize: function(value) {
		               return value[1];
		            },
		            data: data1
		        }]
			});
			
			var zb = yearScatterObj.getOption().series[0].data.sort(sortNumber1);
			var fhzzl = yearScatterObj.getOption().series[0].data.sort(sortNumber2);
			
			var fhzzltempzd = [];
			var fhzzltempzx = [];
			$.each(zb,function(i,v){
				if(i > 4 && i < 15){
					fhzzltempzd.push(zb[i]);
				}
			});
			$.each(zb,function(i,v){
				if(i > zb.length-16 && i < zb.length-5){
					fhzzltempzx.push(zb[i]);
				}
			});
			fhzzltempzd = fhzzltempzd.sort(sortNumber2);
			fhzzltempzx = fhzzltempzx.sort(sortNumber2);
			
			var tempProvince = [];
			str = "{";
			//最大
			str += "zd:[["+zb[0][0]+","+zb[0][1]+",'"+zb[0][2]+"'],";
			str += "["+zb[1][0]+","+zb[1][1]+",'"+zb[1][2]+"'],";
			str += "["+zb[2][0]+","+zb[2][1]+",'"+zb[2][2]+"'],";
			str += "["+zb[3][0]+","+zb[3][1]+",'"+zb[3][2]+"'],";
			str += "["+zb[4][0]+","+zb[4][1]+",'"+zb[4][2]+"'],";
			str += "["+fhzzltempzd[0][0]+","+fhzzltempzd[0][1]+",'"+fhzzltempzd[0][2]+"'],";
			str += "["+fhzzltempzd[1][0]+","+fhzzltempzd[1][1]+",'"+fhzzltempzd[1][2]+"'],";
			str += "["+fhzzltempzd[2][0]+","+fhzzltempzd[2][1]+",'"+fhzzltempzd[2][2]+"'],";
			str += "["+fhzzltempzd[3][0]+","+fhzzltempzd[3][1]+",'"+fhzzltempzd[3][2]+"'],";
			str += "["+fhzzltempzd[1][0]+","+fhzzltempzd[4][1]+",'"+fhzzltempzd[4][2]+"']],";
			//最小
			str += "zx:[["+zb[(zb.length-1)-0][0]+","+zb[(zb.length-1)-0][1]+",'"+zb[(zb.length-1)-0][2]+"'],";
			str += "["+zb[(zb.length-1)-1][0]+","+zb[(zb.length-1)-1][1]+",'"+zb[(zb.length-1)-1][2]+"'],";
			str += "["+zb[(zb.length-1)-2][0]+","+zb[(zb.length-1)-2][1]+",'"+zb[(zb.length-1)-2][2]+"'],";
			str += "["+zb[(zb.length-1)-3][0]+","+zb[(zb.length-1)-3][1]+",'"+zb[(zb.length-1)-3][2]+"'],";
			str += "["+zb[(zb.length-1)-4][0]+","+zb[(zb.length-1)-4][1]+",'"+zb[(zb.length-1)-4][2]+"'],";
			str += "["+fhzzltempzx[5][0]+","+fhzzltempzx[5][1]+",'"+fhzzltempzx[5][2]+"'],";
			str += "["+fhzzltempzx[6][0]+","+fhzzltempzx[6][1]+",'"+fhzzltempzx[6][2]+"'],";
			str += "["+fhzzltempzx[7][0]+","+fhzzltempzx[7][1]+",'"+fhzzltempzx[7][2]+"'],";
			str += "["+fhzzltempzx[8][0]+","+fhzzltempzx[8][1]+",'"+fhzzltempzx[8][2]+"'],";
			str += "["+fhzzltempzx[9][0]+","+fhzzltempzx[9][1]+",'"+fhzzltempzx[9][2]+"']]";
			str += ",other:[";
			
			//最大的省份
			tempProvince.push(zb[0][2]);
			tempProvince.push(zb[1][2]);
			tempProvince.push(zb[2][2]);
			tempProvince.push(zb[3][2]);
			tempProvince.push(zb[4][2]);
			tempProvince.push(fhzzltempzd[0][2]);
			tempProvince.push(fhzzltempzd[1][2]);
			tempProvince.push(fhzzltempzd[2][2]);
			tempProvince.push(fhzzltempzd[3][2]);
			tempProvince.push(fhzzltempzd[4][2]);
			//最小的省份
			tempProvince.push(zb[(zb.length-1)-0][2]);
			tempProvince.push(zb[(zb.length-1)-1][2]);
			tempProvince.push(zb[(zb.length-1)-2][2]);
			tempProvince.push(zb[(zb.length-1)-3][2]);
			tempProvince.push(zb[(zb.length-1)-4][2]);
			tempProvince.push(fhzzltempzx[5][2]);
			tempProvince.push(fhzzltempzx[6][2]);
			tempProvince.push(fhzzltempzx[7][2]);
			tempProvince.push(fhzzltempzx[8][2]);
			tempProvince.push(fhzzltempzx[9][2]);
			$.each(data,function(i,v){
				var isExist = false;
				$.each(tempProvince,function(j,k){
					if(v.provinceName == k){
						isExist = true;
						return false;
					}
				});
				if(!isExist){
					str +="[";
					str += v.zb.toFixed(2);
					str +=",";
					str += v.ffzzl.toFixed(2);
					str +=",";
					str += "'"+v.provinceName+"'";
					str += "],";
				}
			});
			str = str.substring(0,str.lastIndexOf(","));
			str += "]";
			str += "}";
			var objtemp = eval("("+str+")")
			$.each(objtemp.zd,function(i,v){
				str = "{";
				str += "'name':"+"'"+v[2]+"'";
				str += ",'zb':"+"'"+v[0]+"'";
				str += ",'ffzzl':"+"'"+v[1]+"'";
				str += ",itemStyle:"+"itemStyleHight";
				str += "}";
				mapData.push(eval("("+str+")"));
			});
			$.each(objtemp.zx,function(i,v){
				str = "{";
				str += "'name':"+"'"+v[2]+"'";
				str += ",'zb':"+"'"+v[0]+"'";
				str += ",'ffzzl':"+"'"+v[1]+"'";
				str += ",itemStyle:"+"itemStyleEnd";
				str += "}";
				mapData.push(eval("("+str+")"));
			});
			$.each(objtemp.other,function(i,v){
				str = "{";
				str += "'name':"+"'"+v[2]+"'";
				str += ",'zb':"+"'"+v[0]+"'";
				str += ",'ffzzl':"+"'"+v[1]+"'";
				str += ",itemStyle:"+"itemStyleMiddle";
				str += "}";
				mapData.push(eval("("+str+")"));
			});
			yearMapQltObj.setOption({
		        series : [
		            {
		                type: 'map',
		                map: 'china',
		                itemStyle: itemStyle,
		                showLegendSymbol: true,
		                label: {
		                    normal: {
		                        show: true
		                    },
		                    emphasis: {
		                        show: true
		                    }
		                },
		                data:mapData
		            }
		        ]
			});
			
			$(".shengfxiaojtime").eq(0).text(yearYaxis);
			$(".shengfxiaojtime").eq(1).text(yearYaxis.split('-')[1]);
			
			$(".shengfxiaojname").eq(0).text(fhzzl[0][2]+"、"+fhzzl[1][2]+"、"+fhzzl[2][2]);
			$(".shengfxiaojvalue").eq(0).text(fhzzl[0][1]+"%、"+fhzzl[1][1]+"%、"+fhzzl[2][1]+"%");
			
			$(".shengfxiaojname").eq(1).text(zb[0][2]+"、"+zb[1][2]+"、"+zb[2][2]);
			$(".shengfxiaojvalue").eq(1).text(zb[0][0]+"%、"+zb[1][0]+"%、"+zb[2][0]+"%");
			/**********我是分割线**********/
			$(".shengfenshichangtime").eq(0).text(yearYaxis);
			$(".shengfenshichangtime").eq(1).text(yearYaxis.split('-')[1]);
			$(".shengfenshichangtop3").eq(0).text(fhzzl[0][2]+"、"+fhzzl[1][2]+"、"+fhzzl[2][2]);
			$(".shengfenshichangtop3").eq(1).text(zb[0][2]+"、"+zb[1][2]+"、"+zb[2][2]);
			$(".shengfenshichangtop3").eq(2).text(zb[0][2]+"、"+zb[1][2]+"、"+zb[2][2]);
			$(".shengfenshichangatcname").text(tempMap.atcName);
			$(".shengfenshichangfhzzl").text(fhzzl[0][1]+"%、"+fhzzl[1][1]+"%、"+fhzzl[2][1]+"%");
			$(".shengfenshichanggxl").text(zb[0][0]+"%、"+zb[1][0]+"%、"+zb[2][0]+"%");
			$(".img2atcname").text(tempMap.atcName);
			$(".img3atcname").text(tempMap.atcName);
			yearScatterObj.hideLoading();
			yearMapQltObj.hideLoading();
		});
	}
}


/**
 * 医院市场潜力图
 */
function yearMapQlyReport(){
	yearMapQltObj = null;
	$("#yearMapQlt").empty();
	yearMapQltObj = echarts.init(document.getElementById('yearMapQlt'), "vintage");
	yearMapQltObj.setOption({
        title : {
            text: '潜力图',
            //subtext: '纯属虚构',
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: function(v){
            	//console.info(v);
            	return v.data.name+"<br/>占比："+v.data.zb+"%<br/>复合增长率："+v.data.ffzzl+"%";
            }
        },
        /*legend: {
            orient: 'vertical',
            left: 'left'
        },*/
        /*dataRange: {
            x: 'left',
            y: 'bottom',
            splitList: [
                {start: 30, end: 100},
                {start: 10, end: 30},
                {start: 0, end: 2}
            ],
            color: ['#280CC6', '#3B82F6', '#3BC4F6']
        },*/
        /*visualMap: {
            min: 0,
            max: 100,
            left: 'left',
            top: 'bottom',
            text:['高','低'],           // 文本，默认为数值文本
            color: ['#280CC6','#3B82F6','#3BC4F6'],
            calculable : true
        },*/
        //selectedMode: false,//'single',
        series : [
            {
                type: 'map',
                map: 'china',
                itemStyle: itemStyle,
                showLegendSymbol: true,
                label: {
                    normal: {
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                data:[]
            }
        ]
    });
}


/**
 * 根据企业类型查找销售额复合图
 */
function yearXseBarReport(tempMap){
	yearXseBarObj = null;
	$("#yearBarXse").empty();
	yearXseBarObj = echarts.init(document.getElementById('yearBarXse'), "vintage");
	
	yearXseBarObj.showLoading();
	yearXseBarObj.setOption({
		title : {
            text: '外资/合资和内资企业销售表现',
            //subtext: '纯属虚构',
            left: 'center',
            top:'7%'
        },
		tooltip : {
            trigger: 'axis',
            formatter : "{b}<br/>{a0}：{c0}亿元<br/>{a1}：{c1}亿元<br/>{a2}：{c2}%",
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'      // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:['合资','内资','占比']
        },
        toolbox: {
            show : true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature : {
                mark : {show: true},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : ['2014','2015']
            }
        ],
        yAxis : [
            {
                type : 'value',
                min: 0,
                name: '销售额',
                axisLabel: {
	                formatter: '{value}亿'
	            }
            },
	        {
	            type: 'value',
	            name: '占比',
	            min: 0,
	            interval: 20,
	            axisLabel: {
	                formatter: '{value} %'
	            }
	        }
        ],
        series : [
            {
                name:'合资',
                type:'bar',
                data:[]
            },
            {
                name:'内资',
                type:'bar',
                data:[]
             
            },
            {
            	name:'占比',
                type:'line',
                yAxisIndex: 1,
                data:[]
            }
        ]	
	});
	if(tempMap != undefined){
		c = $.post("../reportMedical/queryByWaiZiHeZiXsbxYear",tempMap,function(data){
			if(data == null || data == undefined || data == ""){
				yearXseBarObj.hideLoading();
				return;
			}
			var data1 = [];//存放X轴的年份
			var data2 = [];//存放合资数
			var data3 = [];//存放内资数
			var data4 = [];//存放占比
			$.each(data,function(i,v){
				data1.push(v.time);
				if(v.manuTypeName == "合资"){
//					console.info(v.time+"=合资="+(v.xse/100000000));
					data2.push((v.xse/100000000).toFixed(2));
					//data2.push(v.xse/100000000);
				}else if(v.manuTypeName == "内资"){
//					console.info(v.time+"=内资="+(v.xse/100000000));
					data3.push((v.xse/100000000).toFixed(2));
					//data3.push(v.xse/100000000);
				}
			});
			$.each(data2,function(i,v){
				var zb = parseFloat(v) /(parseFloat(v)+parseFloat(data3[i]));
				data4.push((zb*100).toFixed(2));
			});
			
			yearXseBarObj.setOption({
				xAxis : [{type : 'category',data : uniqueArray(data1)}],
				series : [
		            {
		                name:'合资',
		                type:'bar',
		                label: {normal: {show: true,formatter: '{c}亿元'}},
		                data:data2
		            },
		            {
		                name:'内资',
		                type:'bar',
		                label: {normal: {show: true,formatter: '{c}亿元'}},
		                data:data3
		             
		            },
		            {
		            	name:'占比',
		                type:'line',
		                yAxisIndex: 1,
		                data:data4
		            }
		        ]
			});
			$(".waizbentxiaojtime").eq(0).text(yearXseBarObj.getOption().xAxis[0].data[0]);
			$(".waizbentxiaojtime").eq(1).text(yearXseBarObj.getOption().xAxis[0].data[yearXseBarObj.getOption().xAxis[0].data.length-1]);
			$(".waizbentxiaojtime").eq(2).text(yearXseBarObj.getOption().xAxis[0].data[yearXseBarObj.getOption().xAxis[0].data.length-1]);
			var maop = maopao(yearXseBarObj.getOption().series[2].data);
			$(".waizizb").eq(0).text(maop[0]+"%");
			$(".waizizbxse").eq(0).text(yearXseBarObj.getOption().series[0].data[yearXseBarObj.getOption().series[0].data.length-1]);
			$(".waizizbxszb").eq(0).text(yearXseBarObj.getOption().series[2].data[yearXseBarObj.getOption().series[2].data.length-1]+"%");
			/***我是分割线***/
			$(".waizibentutime").eq(0).text(yearXseBarObj.getOption().xAxis[0].data[0]+"-"+yearXseBarObj.getOption().xAxis[0].data[yearXseBarObj.getOption().xAxis[0].data.length-1]);
			$(".waizibentuatcname").text(tempMap.atcName);
			$(".waizibentutzb").text(maop[0]+"%");
			$(".waizibentutime").eq(1).text(yearXseBarObj.getOption().xAxis[0].data[yearXseBarObj.getOption().xAxis[0].data.length-1]);
			$(".waizibentuxse").text(yearXseBarObj.getOption().series[0].data[yearXseBarObj.getOption().series[0].data.length-1]);
			$(".waizibentuxsezb").text(yearXseBarObj.getOption().series[2].data[yearXseBarObj.getOption().series[2].data.length-1]+"%");
			$(".bentuxse").text(yearXseBarObj.getOption().series[1].data[yearXseBarObj.getOption().series[1].data.length-1]);
			$(".img4time").text(yearXseBarObj.getOption().xAxis[0].data[0]+"-"+yearXseBarObj.getOption().xAxis[0].data[yearXseBarObj.getOption().xAxis[0].data.length-1]);
			$(".img4atcname").text(tempMap.atcName);
			var zengzhang = 0;
			var jianshao = 0;
			$.each(yearXseBarObj.getOption().series[2].data,function(i,v){
				if(i!=0){
					if(v-yearXseBarObj.getOption().series[2].data[i-1] > 0){
						zengzhang++;
					}else if(v-yearXseBarObj.getOption().series[2].data[i-1] < 0){
						jianshao++;
					}
				}
			});
			if(zengzhang > jianshao){
				$(".waizibentuqs").text("增长");
			}else if(jianshao > zengzhang){
				$(".waizibentuqs").text("降低");
			}else{
				$(".waizibentuqs").text("平稳");
			}
			yearXseBarObj.hideLoading();
		});
	}
}

/**
 * ATC与通用名的销售额占比
 * @param tempMap
 */
function yearAtcXseBarReport(tempMap){
	yearAtcXseBarObj = null;
	$("#yearBarAtcXse").empty();
	yearAtcXseBarObj = echarts.init(document.getElementById('yearBarAtcXse'), "vintage");
	yearAtcXseBarObj.showLoading();
	yearAtcXseBarObj.setOption({
		title : {
            //text: '各分子类别在医院他汀类调脂药市场销售额占比',
            //subtext: '纯属虚构',
            left: 'center',
            top:'7%'
        },
		tooltip : {
            trigger: 'axis',
            //formatter : "{b}<br/>{a0}：{c0}亿元<br/>{a1}：{c1}亿元<br/>{a2}：{c2}%",
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'      // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:[]//'辛伐他汀胶囊','阿托伐他汀钙','氟伐他汀','普伐他汀钠','辛伐他汀','瑞舒伐他汀钙'
        },
        toolbox: {
            show : true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature : {
                mark : {show: true},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : []//'2012','2013','2014','2015'
            }
        ],
        yAxis : [
            
	        {
	            type: 'value',
	            name: '占有率',
	            min: 0,
	            //interval: 20,
	            axisLabel: {
	                formatter: '{value} %'
	            }
	        },
	        {
                type : 'value',
                min: 0,
                name: '销售额',
               // interval: 20000,
                axisLabel: {
	                formatter: '{value}亿'
	            }
            }
        ],
        series : []
	});
	if(tempMap != undefined){
		d = $.post("../reportMedical/queryByAtcXsZbYear",tempMap,function(data){
			yearAtcXseBarObj.setOption({
				legend: {data:[]},
				xAxis : [{type : 'category',data : []}],
				series : []
			});
			if(data == null || data == undefined || data == ""){
				yearAtcXseBarObj.hideLoading();
				return;
			}
			var data1 = [];//存放X轴的年份
			var data2 = [];//存放分子分类名称
			var strTemp = new StringBuffer();
			var strTemp2 = new StringBuffer();
			$.each(data,function(i,v){
				data1.push(v.time);
				data2.push(v.name);
			});
			strTemp.append("[");
			$.each(uniqueArray(data2),function(i,v){
				strTemp.append("{");
				strTemp2.append("{");
				strTemp.append("name:'"+v+"',");
				strTemp2.append("name:'"+v+"',");
				strTemp.append("type:'bar',label: {normal: {show: true,formatter: '{c}%'}},");
				strTemp2.append("type:'line',");
				strTemp.append("stack:'xsezb',");
				strTemp.append("yAxisIndex: 0,");
				strTemp2.append("yAxisIndex: 1,");
				strTemp.append("data:[");
				strTemp2.append("data:[");
				var temp = "";
				var temp2 = "";
				$.each(data,function(j,k){
					if(v == k.name){
						temp += parseFloat(k.xsezb).toFixed(2)+",";
						temp2 += (parseFloat(k.xse)/100000000).toFixed(2)+",";
					}
				});
				strTemp.append(temp.substring(0,temp.lastIndexOf(",")));
				strTemp2.append(temp2.substring(0,temp2.lastIndexOf(",")));
				strTemp.append("]");
				strTemp2.append("]");
				strTemp.append("},");
				strTemp2.append("},");
				/////////////////////////////////////////
			});
			strTemp.append(strTemp2);
			var seriesObj = strTemp.toString().substring(0,strTemp.toString().lastIndexOf(","))+"]";
			yearAtcXseBarObj.setOption({
				legend: {data:uniqueArray(data2)},
				xAxis : [{type : 'category',data : uniqueArray(data1)}],
				series : eval("("+seriesObj+")")
			});
			
			$(".atcclasstime").eq(0).text(yearAtcXseBarObj.getOption().xAxis[0].data[0]);
			$(".atcclasstime").eq(1).text(yearAtcXseBarObj.getOption().xAxis[0].data[yearAtcXseBarObj.getOption().xAxis[0].data.length-1]);
			var currueArray = [];
			$.each(yearAtcXseBarObj.getOption().series,function(i,v){
				if(v.type == 'bar'){
					//console.info(v.name+","+v.data[yearAtcXseBarObj.getOption().xAxis[0].data.length-1]);
					var tempArray = [];
					tempArray.push(v.data[yearAtcXseBarObj.getOption().xAxis[0].data.length-1]);
					tempArray.push(v.name);
					currueArray.push(tempArray);
				}
			});
			var atcclas="";
			var atcclasszb="";
			$.each(currueArray.sort(sortNumber1),function(i,v){
				if(i <= 2){
					atcclas+=v[1]+"、";
					atcclasszb+=v[0]+"%、";
				}
			});
			$(".atcclas").eq(0).text(tempMap.atcName);
			$(".atcclas").eq(1).text(atcclas.substring(0,atcclas.lastIndexOf('、')));
			$(".atcclasszb").eq(0).text(atcclasszb.substring(0,atcclasszb.lastIndexOf('、')));
			
			$(".getongyongmscfxatcname").text(tempMap.atcName);
			$(".getongyongmscfxtime").text(yearAtcXseBarObj.getOption().xAxis[0].data[0]+"-"+yearAtcXseBarObj.getOption().xAxis[0].data[yearAtcXseBarObj.getOption().xAxis[0].data.length-1]);
			$("#getongyongmscfxtime").text(yearAtcXseBarObj.getOption().xAxis[0].data[yearAtcXseBarObj.getOption().xAxis[0].data.length-1]);
			$(".getongyongmscfxtop3").text(atcclas.substring(0,atcclas.lastIndexOf('、')));
			$(".getongyongmscfxtop3zb").text(atcclasszb.substring(0,atcclasszb.lastIndexOf('、')));
			$(".selectatctongymtime").text(yearAtcXseBarObj.getOption().xAxis[0].data[yearAtcXseBarObj.getOption().xAxis[0].data.length-1]);
			$(".img7time").text(yearAtcXseBarObj.getOption().xAxis[0].data[0]+"-"+yearAtcXseBarObj.getOption().xAxis[0].data[yearAtcXseBarObj.getOption().xAxis[0].data.length-1]);
			yearAtcXseBarObj.hideLoading();
		});
	}
}

/**
 * 市场最小产品销售额占比 
 */
function yearMiniUnitXseZbBarReport(tempMap){
	yearMiniUnitXseZbObj = null;
	$("#yearMiniUnitXseZb").empty();
	yearMiniUnitXseZbObj = echarts.init(document.getElementById('yearMiniUnitXseZb'), "vintage");
	yearMiniUnitXseZbObj.showLoading();
	yearMiniUnitXseZbObj.setOption({
		/*title : {
            text: '各分子类别在医院他汀类调脂药市场销售额占比',
            //subtext: '纯属虚构',
            left: 'center',
            top:'7%'
        },*/
		tooltip: {
	        trigger: 'axis',//axis item
	        //formatter : "{b}<br/>{a0}：{c0}万元<br/>{a1}：{c1}%",
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
	    },
	    toolbox: {
	    	orient: 'vertical',
	        feature: {
	            magicType: {show: true, type: ['line', 'bar']},
	            restore: {show: true},
	            saveAsImage: {show: true}
	        }
	    },
	    dataZoom: [{
            show: true,
            start: 0,
            end: 100,
            top: '95%',
            height: 12,
            handleSize: 8
        }, {
            type: 'inside',
            start: 0,
            end: 100
        }, {
            show: true,
            start: 50,
            end: 100,
            yAxisIndex: 0,
            filterMode: 'empty',
            width: 12,
            height: '75%',
            handleSize: 8,
            showDataShadow: false,
            left: '92%'
        }],
	    legend: {
	        data:[]
	    },
	    grid:{
	    	show :true,
	    	left:200
	    },
	    yAxis: [
	        {
	            type: 'category',
	            data: [],
	            axisLabel:{
	            	margin:2,
	            	rotate:30
	            }
	        }
	    ],
	    xAxis:{
	    	type: 'value',
            name: '销售额',
            axisLabel: {
                formatter: '{value}百万'
            }
	    },
	    series: []
	});
	if(tempMap != undefined){
		e = $.post("../reportMedical/queryByMinniUnitXseZb",tempMap,function(data){
			if(tempMap.atcType != undefined && tempMap.atcType == 4){
				switch (tempMap.atcName){
					case "阿托伐他汀":
						$("#atftt").show();
						$("#rsftt").hide();
						$("#xftt").hide();
						$("#pftt").hide();
						$("#fftt").hide();
						break;
					case "瑞舒伐他汀":
						$("#atftt").hide();
						$("#rsftt").show();
						$("#xftt").hide();
						$("#pftt").hide();
						$("#fftt").hide();
						break;
					case "辛伐他汀":
						$("#atftt").hide();
						$("#rsftt").hide();
						$("#xftt").show();
						$("#pftt").hide();
						$("#fftt").hide();
						break;
					case "普伐他汀":
						$("#atftt").hide();
						$("#rsftt").hide();
						$("#xftt").hide();
						$("#pftt").show();
						$("#fftt").hide();
						break;
					case "氟伐他汀":
						$("#atftt").hide();
						$("#rsftt").hide();
						$("#xftt").hide();
						$("#pftt").hide();
						$("#fftt").show();
						break;
				}
			}else{
				$("#atftt").show();
				$("#rsftt").show();
				$("#xftt").show();
				$("#pftt").show();
				$("#fftt").show();
			}
			
			if(data == null || data == undefined || data == ""){
				yearMiniUnitXseZbObj.hideLoading();
				return;
			}
			var data1 = [];//存放X轴的年份
			var data2 = [];//存放分子分类名称
			var strTemp = new StringBuffer();
			$.each(data,function(i,v){
				data1.push(v.time);
				data2.push(v.name);
			});
			strTemp.append("[");
			$.each(uniqueArray(data1),function(i,v){
				strTemp.append("{");
				strTemp.append("name:'"+v+"',");
				strTemp.append("type:'bar',");
				strTemp.append("yAxisIndex: 0,");
				strTemp.append("data:[");
				var temp = "";
				$.each(data,function(j,k){
					if(v == k.time){
						temp += (parseFloat(k.xse)/1000000).toFixed(2)+",";
					}
				});
				strTemp.append(temp.substring(0,temp.lastIndexOf(",")));
				strTemp.append("]");
				strTemp.append("},");
			});
			var seriesObj = strTemp.toString().substring(0,strTemp.toString().lastIndexOf(","))+"]";
			yearMiniUnitXseZbObj.setOption({
				legend: {data:uniqueArray(data1)},//data:uniqueArray(data2)
				yAxis: [{type: 'category',data: uniqueArray(data2)}],
				series : eval("("+seriesObj+")")
			});
			
			$(".chanpintime").text(yearMiniUnitXseZbObj.getOption().legend[0].data[yearMiniUnitXseZbObj.getOption().legend[0].data.length-1]);
			$(".chanpinatcclass").eq(0).text(tempMap.atcName);
			$(".shichangbiaoxianatcname").text(tempMap.atcName);
			$(".shichangbiaoxiantime").text(yearMiniUnitXseZbObj.getOption().legend[0].data[yearMiniUnitXseZbObj.getOption().legend[0].data.length-1]);
			yearMiniUnitXseZbObj.hideLoading();
		});
	}
}

/**
 * 根据市场（ATC）查询TOP10品牌销售总额占比与占比增长率
 * @param tempMap
 */
function yearTop10XseZzlZbReport(tempMap){
	yearTop10XseZzlZbObj = null;
	$("#yearTop10XseZzlZb").empty();
	yearTop10XseZzlZbObj =  echarts.init(document.getElementById('yearTop10XseZzlZb'), "vintage");
	yearTop10XseZzlZbObj.showLoading();
	yearTop10XseZzlZbObj.setOption({
		tooltip : {
            trigger: 'axis',
            //formatter : "{b}<br/>{a0}：{c0}亿元<br/>{a1}：{c1}亿元<br/>{a2}：{c2}%",
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'      // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            data:["销售占比","占比增长率"]//'辛伐他汀胶囊','阿托伐他汀钙','氟伐他汀','普伐他汀钠','辛伐他汀','瑞舒伐他汀钙'
        },
        toolbox: {
            show : true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature : {
                mark : {show: true},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : ['2012','2013','2014','2015']//'2012','2013','2014','2015'
            }
        ],
        yAxis : [
	        {
	            type: 'value',
	            name: '销售额占比',
	            min: 0,
	            axisLabel: {
	                formatter: '{value} %'
	            }
	        },
	        {
                type : 'value',
                min: 0,
                name: '占比增长率',
                axisLabel: {
	                formatter: '{value}%'
	            }
            }
        ],
        series:[{name:'销售占比',type:'bar',data:[0,0,0,0]},
				{name:'占比增长率',type:'line',yAxisIndex: 1,data:[0,0,0,0]}
          ]
	});
	if(tempMap != undefined){
		f = $.post("../reportMedical/queryByTop10XseZbZzl",tempMap,function(data){
			var data1 = [];//存年
			var data2 = [];//销售占比
			var data3 = [];//占比增长率
			$.each(eval("("+data+")"),function(i,v){
				data1.push(v.time);
				data2.push(parseFloat(v.xsezb).toFixed(2));
				data3.push(parseFloat(v.zbzzl).toFixed(2));
			});
			yearTop10XseZzlZbObj.setOption({
				xAxis : [{type : 'category',data : data1}],
				series: [{name:'销售占比',type:'bar',data:data2,label: {normal: {show: true,formatter: '{c}%'}}},
						{name:'占比增长率',type:'line',yAxisIndex: 1,data:data3}
		          ]
			});
			$(".top10chanpintime").eq(0).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[0]+"-"+yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".top10chanpinatcclass").eq(0).text(tempMap.atcName);
			$(".top10chanpinatcclass").eq(1).text(tempMap.atcName);
			var temp = [];
			$.each(yearTop10XseZzlZbObj.getOption().series[0].data,function(i,v){
				if(v != ""){
					temp.push(v);
				}
			});
			$(".top10chanpinzb").text(maopao(temp)[0]+"%");
			$(".top10chanpintime").eq(1).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".top10changjiatime").eq(0).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			
			$(".img5time").text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[0]+"-"+yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".img6time").text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[0]+"-"+yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".img8time").text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[0]+"-"+yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".atcimg5name").text(tempMap.atcName);
			$(".atcimg6name").text(tempMap.atcName);
			$(".atcimg7name").text(tempMap.atcName);
			
			/*top10产品*/
			$(".top10chanpinmiaostime").eq(0).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[0]+"-"+yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".top10chanpinmiaostime").eq(1).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".top10chanpinmiaostime").eq(2).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".top10chanpinmiaosatcname").text(tempMap.atcName);
			$(".top10chanpinmiaoszb").text(maopao(temp)[0]+"%");
			
			
			/*top10厂家*/
			$(".top10changjiamiaoshutime").eq(0).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[0]+"-"+yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".top10changjiamiaoshutime").eq(1).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".top10changjiamiaoshutime").eq(2).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".top10changjiamiaoshutime").eq(3).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
			$(".top10changjiamiaoshutime").eq(4).text(yearTop10XseZzlZbObj.getOption().xAxis[0].data[0]+"-"+yearTop10XseZzlZbObj.getOption().xAxis[0].data[yearTop10XseZzlZbObj.getOption().xAxis[0].data.length-1]);
		});
	}
	yearTop10XseZzlZbObj.hideLoading();
}



/**
 * 根据市场
 * @param tempMap
 */
function yearTop10XseZbReport(tempMap){
	yearTop10XseZbObj = null;
	$("#yearTop10XseZb").empty();
	yearTop10XseZbObj = echarts.init(document.getElementById('yearTop10XseZb'), "vintage");
	
	yearTop10XseZbObj.showLoading();
	yearTop10XseZbObj.setOption({
		title : {
            text: '医院市场TOP 10品牌销售额占比',
            //subtext: '纯属虚构',
            left: 'center'
        },
		 tooltip: {
	        trigger: 'axis',
	        formatter : "{b}<br/>占比：{c}%",
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    xAxis: {
	        type: 'value',
	        boundaryGap: [0, 0.01],
	        axisLabel: {
                formatter: '{value} %'
            }
	    },
	    yAxis: {
	        type: 'category',
	        data: []
	    },
	    series: []
	});
	if(tempMap != undefined){
		$.post("../reportMedical/queryByTop10XseZb",tempMap,function(data){
			yearTop10XseZbObj.setOption({
				yAxis: {type: 'category',data: []},
				series:[]
			});
			var data1 = [];//存放类别（公司名称）
			var data2 = [];//存放市场
			var data3 = [];//存放整体数据
			var strTemp = new StringBuffer();
			$.each(data,function(i,v){
				data1.push(v.manuTypeName);
				data2.push(v.name);
			});
			strTemp.append("[");
			strTemp.append("{");
			strTemp.append("name:");
			strTemp.append("'',");
			strTemp.append("type:'bar',label: {normal: {show: true,formatter: '{c}%',position: 'right',textStyle: {color:'#000'}}},");
			strTemp.append("data:[");
			var temp = "";
			var xse = [];
			$.each(data,function(j,k){
				var xsezb = parseFloat(k.xsezb);
				var xsev = parseFloat(k.xse);
				temp+=""+xsezb.toFixed(2)+",";
				xse.push(xsev.toFixed(2));
			});
			strTemp.append(temp.substring(0,temp.lastIndexOf(",")));
			strTemp.append("]");
			strTemp.append("}");
			strTemp.append("]");
			yearTop10XseZbObj.setOption({
				yAxis: {type: 'category',data: data2},
				series:eval("("+strTemp.toString()+")")
			});
			top10linp = yearTop10XseZbObj.getOption().yAxis[0].data[0];
			$("#top10linp").text(top10linp);
			
			var heziCount = 0;
			var neiziCount = 0;
			var guochanname="";
			var guochanzbsum=0;
			$.each(data,function(i,v){
				if(v.manuTypeName == '合资'){
					heziCount++;
				}else{
					neiziCount++;
					guochanname+=v.name+"、"
					guochanzbsum+=parseFloat(v.xsezb);
				}
			});
			$(".top10chanpinwz").text(heziCount);
			$(".top10chanpinnz").text(neiziCount);
			$(".topchanpintuchu").text(yearTop10XseZbObj.getOption().yAxis[0].data[0]);
			
			/*市场产品表现*/
			
			$(".shichangbiaoxiantop3").text(yearTop10XseZbObj.getOption().yAxis[0].data[0]+"、"+yearTop10XseZbObj.getOption().yAxis[0].data[1]+"、"+yearTop10XseZbObj.getOption().yAxis[0].data[2]);
			$(".shichangbiaoxianxsfe").text(accounting.formatNumber(xse[0])+"、"+accounting.formatNumber(xse[1])+"、"+accounting.formatNumber(xse[2]));
			
			$(".chanpinatcclasstop3").text(yearTop10XseZbObj.getOption().yAxis[0].data[0]+"、"+yearTop10XseZbObj.getOption().yAxis[0].data[1]+"、"+yearTop10XseZbObj.getOption().yAxis[0].data[2]);
			$(".chanpinatcclassfe").text(accounting.formatNumber(xse[0])+"、"+accounting.formatNumber(xse[1])+"、"+accounting.formatNumber(xse[2]));
			
			/*top10产品*/
//			$(".top10chanpinmiaoswaiz").text(heziCount);
			$(".top10chanpinmiaosgsname").text(data[0].manuName);
			$(".top10chanpinmiaoschanp").text(data[0].name);
			$(".top10chanpinmiaosxsezb").text(parseFloat(data[0].xsezb).toFixed(2)+"%");
			$(".top10chanpinmiaosguochannum").text(neiziCount);
			$(".top10chanpinmiaosguochannname").text(guochanname.substring(0,guochanname.lastIndexOf("、")));
			$(".top10chanpinmiaosguochannzb").text(parseFloat(guochanzbsum).toFixed(2)+"%");
			yearTop10XseZbObj.hideLoading();
		});
	}
}

/**
 * 表1
 * @param tempMap
 */
function reportDataList1(tempMap){
	if(tempMap != undefined){
		g = $.post("../reportMedical/queryByTop10ListDataOne",tempMap,function(data){
			$("#list1").empty().append(eval("("+data+")").html);
			$(".top10changjiazhud").text(eval("("+data+")").manuTypeTop);
			var sumzb = [];
			sumzb.push(eval("("+data+")").fristScfezbSum);
			sumzb.push(eval("("+data+")").middleScfezbSum);
			sumzb.push(eval("("+data+")").lastScfezbSum);
			$(".top10changjiasumzb").text(parseFloat(maopao(sumzb)[sumzb.length-1]).toFixed(2)+"%");
			$(".top10changjiachuse").text(eval("("+data+")").chuse);
			
			$(".top10changjiazb").text(parseFloat(maopao(sumzb)[0]).toFixed(2)+"%");
			$(".top10changjiawaizicount").text(eval("("+data+")").hezi);
			$(".top10chanpinnum").text(eval("("+data+")").chanpinnum);
			$(".top10changjiagongs").text(eval("("+data+")").chuse);
			$(".top10scxsezb").text(parseFloat(eval("("+data+")").maxscfezb).toFixed(2)+"%");
			$(".top10scxsezzl").text(parseFloat(eval("("+data+")").maxscfezzl).toFixed(2)+"%");
			$(".xunsechengz").text(eval("("+data+")").maxxsezzlmanuname);
			$(".xunsechengzzzl").text(parseFloat(eval("("+data+")").maxxsezzlzb).toFixed(2)+"%");
		});
	}
}
//$(".excelReportLoad").attr("class","excelReportLoad loadNone");
/***
 * 设置自动调整Size
 */
window.addEventListener("resize", function() {
	yearBarObj.resize();
	yearScatterObj.resize();
	yearXseBarObj.resize();
	yearMapQltObj.resize();
	yearAtcXseBarObj.resize();
	yearMiniUnitXseZbObj.resize();
	yearTop10XseZzlZbObj.resize();
	yearTop10XseZbObj.resize();
});