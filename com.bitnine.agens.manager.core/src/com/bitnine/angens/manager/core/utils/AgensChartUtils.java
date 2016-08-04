package com.bitnine.angens.manager.core.utils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.rap.addons.chart.basic.TimeDataGroup;

public class AgensChartUtils {
	
	/**
	 * json to fully time chart
	 * 
	 * @param arryDataGroup
	 * @return
	 */
	public static String jsonToFulliyTimeChart(TimeDataGroup[] arryDataGroup) {
		StringBuffer sbJson = new StringBuffer("[");
		for (TimeDataGroup timeDataGroup : arryDataGroup) {
			sbJson.append(jsonStrToDate(timeDataGroup.toJson().toString())).append(",");
		}
		String sbFullJson = StringUtils.removeEnd(sbJson.toString(), ",");
		sbFullJson += "]";
		
		return sbFullJson;
	}
	
	/**
	 * json to string data
	 * 
	 * @param strJsonData
	 * @return
	 */
	public static String jsonStrToDate(String strJsonData) {
		String strString = StringUtils.replace(strJsonData, "\"new Date(\\\"", "new Date(\"");
		strString = StringUtils.replace(strString, "\\\")\"", "\")");
		
		return strString;
	}
}
