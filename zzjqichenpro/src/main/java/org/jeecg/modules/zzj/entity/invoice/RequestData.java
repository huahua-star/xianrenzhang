package org.jeecg.modules.zzj.entity.invoice;

/**
 * 请求对象 Created by Amhong on 2015/2/7.
 */
public class RequestData {
	/*
	 * RequestData 操作流水号
	 */
	private String serialNo;

	/*
	 * 请求发送时间
	 */
	private String postTime;
	private String orderNo;
	private String taxpayerCode;

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	private String invoiceType;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTaxpayerCode() {
		return taxpayerCode;
	}

	public void setTaxpayerCode(String taxpayerCode) {
		this.taxpayerCode = taxpayerCode;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
}
