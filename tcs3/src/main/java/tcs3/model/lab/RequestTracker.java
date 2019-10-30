package tcs3.model.lab;

import java.util.Date;
import java.util.List;

import tcs3.model.hrx.Officer;

public class RequestTracker {

	private Long requestId;
	private String LabNo;
	private String customerName;
	private Date customerSendDate;
	private String SampleDeliverName;
	private String orgName;
	private String mainOrgName;
	private String mainScientistName;
	private Date estimatedReportDate;
	private List<RequestHistory> requestHisotries;
	
	private List<List<ReportHistory>> reportHistories;
	
	private ReportSentDetail sentDetail;

	public RequestTracker(Request r) {
		this.requestId =r.getId();
		this.LabNo = r.getReqNo();
		this.customerName = r.getCustomerFullname();
		this.customerSendDate = r.getReqDate();
		this.SampleDeliverName = r.getCustomerName();
		this.orgName = r.getMainOrg().getName();
		this.mainOrgName = r.getGroupOrg().getName();
		this.mainScientistName = r.getMainScientist().getFirstName() + " "+ r.getMainScientist().getLastName();
		this.estimatedReportDate = r.getEstimatedReportDate();
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getLabNo() {
		return LabNo;
	}

	public void setLabNo(String labNo) {
		LabNo = labNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getCustomerSendDate() {
		return customerSendDate;
	}

	public void setCustomerSendDate(Date customerSendDate) {
		this.customerSendDate = customerSendDate;
	}

	public String getSampleDeliverName() {
		return SampleDeliverName;
	}

	public void setSampleDeliverName(String sampleDeliverName) {
		SampleDeliverName = sampleDeliverName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getMainOrgName() {
		return mainOrgName;
	}

	public void setMainOrgName(String mainOrgName) {
		this.mainOrgName = mainOrgName;
	}

	public String getMainScientistName() {
		return mainScientistName;
	}

	public void setMainScientistName(String mainScientistName) {
		this.mainScientistName = mainScientistName;
	}

	public Date getEstimatedReportDate() {
		return estimatedReportDate;
	}

	public void setEstimatedReportDate(Date estimatedReportDate) {
		this.estimatedReportDate = estimatedReportDate;
	}

	public List<RequestHistory> getRequestHisotries() {
		return requestHisotries;
	}

	public void setRequestHisotries(List<RequestHistory> requestHisotries) {
		this.requestHisotries = requestHisotries;
	}

	public List<List<ReportHistory>> getReportHistories() {
		return reportHistories;
	}

	public void setReportHistories(List<List<ReportHistory>> reportHistories) {
		this.reportHistories = reportHistories;
	}

	public ReportSentDetail getSentDetail() {
		return sentDetail;
	}

	public void setSentDetail(ReportSentDetail sentDetail) {
		this.sentDetail = sentDetail;
	}
	
	
	
	
	
}
