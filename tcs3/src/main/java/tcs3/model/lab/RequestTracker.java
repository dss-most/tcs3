package tcs3.model.lab;

import java.util.Date;
import java.util.List;

public class RequestTracker {

	private String LabNo;
	private String customerName;
	private Date customerSendDate;
	private String SampleDeliverName;
	private String orgName;
	private String mainOrgName;
	private Date estimatedReportDate;
	private List<RequestHistory> requestHisotries;
	
	private List<List<ReportHistory>> reportHistories;
	
	private ReportSentDetail sentDetail;

	public RequestTracker(Request r) {
		this.LabNo = r.getReqNo();
		this.customerName = r.getCustomerFullname();
		this.customerSendDate = r.getReqDate();
		this.SampleDeliverName = r.getCustomerName();
		this.orgName = r.getMainOrg().getName();
		this.mainOrgName = r.getGroupOrg().getName();
		//this.estimatedReportDate = r.get
	}
	
	
	
}
