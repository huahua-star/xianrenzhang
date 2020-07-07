package org.jeecg.modules.zzj.entity.invoice;

import java.util.List;

/**
 * 开票请求
 */
public class KpParams extends RequestData {

	/**
	 * 订购信息
	 */
	private Order order;

	/**
	 * 发票信息
	 */
	private KpInvoice invoice;

	/**
	 * 通知方式
	 */
	private List<Notice> notices;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public KpInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(KpInvoice invoice) {
		this.invoice = invoice;
	}

	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}
}