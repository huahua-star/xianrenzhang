package org.jeecg.modules.zzj.entity.invoice;

import java.util.List;

/**
 * 开票请求发票信息
 */
public class KpInvoice {
	/**
	 * 付款方名称
	 */
	private String customerName;

	/**
	 * 付款方纳税人识别号
	 */
	private String customerCode;

	/**
	 * 收款方纳税人识别号
	 */
	private String taxpayerCode;

	/**
	 * 开票人
	 */
	private String drawer;

	private String customerAddress;
	private String customerTel;
	private String customerBanmName;
	private String payee;
	private String reviewer;

	/**
	 * 发票类型
	 */
	private String invoiceType;

	/**
	 * 总金额
	 */
	private String totalAmount;

	/**
	 * 项目明细
	 */
	private List<KpInvoiceItem> items;

	/**
	 * 备注
	 */
	private String remark;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getTaxpayerCode() {
		return taxpayerCode;
	}

	public void setTaxpayerCode(String taxpayerCode) {
		this.taxpayerCode = taxpayerCode;
	}

	public String getDrawer() {
		return drawer;
	}

	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}

	public List<KpInvoiceItem> getItems() {
		return items;
	}

	public void setItems(List<KpInvoiceItem> items) {
		this.items = items;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public String getCustomerBanmName() {
		return customerBanmName;
	}

	public void setCustomerBanmName(String customerBanmName) {
		this.customerBanmName = customerBanmName;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

}