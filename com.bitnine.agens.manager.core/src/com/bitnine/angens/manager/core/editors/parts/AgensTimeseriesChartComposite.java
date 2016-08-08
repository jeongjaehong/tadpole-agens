package com.bitnine.angens.manager.core.editors.parts;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.rap.addons.chart.basic.TimeDataGroup;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.bitnine.agens.manager.engine.core.dao.domain.Instance;
import com.bitnine.angens.manager.core.editors.template.AgensChartTemplate;
import com.bitnine.angens.manager.core.utils.AgensChartUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

public abstract class AgensTimeseriesChartComposite extends AgensChartComposite {
	private static final Logger logger = Logger.getLogger(AgensTimeseriesChartComposite.class);
	
	/** TIMESERIES CHART HTML */
	protected String _TEMPLATE_TIMESERIES_CHART_HTML = "";
	/** define line chart */
	protected Browser browserChart;

	public AgensTimeseriesChartComposite(Composite parent, UserDBDAO userDB, Instance instance) {
		super(parent, userDB, instance);
	}
	
	/**
	 * initialize ui data
	 * 
	 * @param xLabel
	 * @param xLabelFormat
	 * @param yLabel
	 * @param yLabelFormat
	 */
	protected void initializeUIData(String xLabel, String xLabelFormat, String yLabel, String yLabelFormat) {
		try {
			_TEMPLATE_TIMESERIES_CHART_HTML = AgensChartTemplate.getTimeseriesLineChart();
			
//			_TEMPLATE_TIMESERIES_CHART_HTML = StringUtils.replace(_TEMPLATE_TIMESERIES_CHART_HTML, "_XAXIS_LABEL_", 		"'Timestamp'");
//			_TEMPLATE_TIMESERIES_CHART_HTML = StringUtils.replace(_TEMPLATE_TIMESERIES_CHART_HTML, "_XAXIS_LABEL_FORMAT", 	"'%Y-%m-%d %H:%M'");
//			_TEMPLATE_TIMESERIES_CHART_HTML = StringUtils.replace(_TEMPLATE_TIMESERIES_CHART_HTML, "_YAXIS_LABEL_", 		"'Size(Byte)'");
//			_TEMPLATE_TIMESERIES_CHART_HTML = StringUtils.replace(_TEMPLATE_TIMESERIES_CHART_HTML, "_YAXIS_LABEL_FORMAT", 	"d3.format('.02f')");
			_TEMPLATE_TIMESERIES_CHART_HTML = StringUtils.replace(_TEMPLATE_TIMESERIES_CHART_HTML, "_XAXIS_LABEL_", 		xLabel);
			_TEMPLATE_TIMESERIES_CHART_HTML = StringUtils.replace(_TEMPLATE_TIMESERIES_CHART_HTML, "_FORMAT_XAXIS_LABEL", 	xLabelFormat);
			_TEMPLATE_TIMESERIES_CHART_HTML = StringUtils.replace(_TEMPLATE_TIMESERIES_CHART_HTML, "_YAXIS_LABEL_", 		yLabel);
			_TEMPLATE_TIMESERIES_CHART_HTML = StringUtils.replace(_TEMPLATE_TIMESERIES_CHART_HTML, "_FORMAT_YAXIS_LABEL", 	yLabelFormat);
			
			browserChart.setText(_TEMPLATE_TIMESERIES_CHART_HTML);
		} catch (IOException e) {
			logger.error("get template htmll", e);
		}
	}
	
	/**
	 * refresh UI
	 * @param listCPU
	 */
	public void refreshUI(final List<?> listData) {
		final Display display = browserChart.getDisplay();
	    display.asyncExec( new Runnable() {
	    	public void run() {
	    		try {
	    			TimeDataGroup[] arryDataGroup = listData.toArray(new TimeDataGroup[listData.size()]);
		    		String strHtml = StringUtils.replace(_TEMPLATE_TIMESERIES_CHART_HTML, AgensChartTemplate.TIMESERIESCHART_TEMPLATE, AgensChartUtils.jsonToFulliyTimeChart(arryDataGroup));
		    		
//		    		if(logger.isDebugEnabled()) logger.debug("[output hthml]" + strHtml);
		    		browserChart.setText(strHtml);
	    		} catch(Exception e) {
	    			logger.error("print timeseries chart", e);
	    		}
	    	}
	    });	// end display
	}

}
