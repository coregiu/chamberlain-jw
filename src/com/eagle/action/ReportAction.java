package com.eagle.action;

import org.jfree.data.time.TimeSeries;
import com.eagle.data.ViewTimeBean;

/**
 * 报表处理
 */
public class ReportAction extends ReportDataCollectThread {
	private TimeSeries combineData[] = { outData, inData };

	public ReportAction(ViewTimeBean vd, char type) {
		super(vd, type);
	}

	public TimeSeries[] getCombineData() {
		return combineData;
	}

	public void setCombineData(TimeSeries combineData[]) {
		this.combineData = combineData;
	}
}
