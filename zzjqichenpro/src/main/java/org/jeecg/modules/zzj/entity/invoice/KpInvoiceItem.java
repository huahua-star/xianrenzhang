package org.jeecg.modules.zzj.entity.invoice;

/**
 * 开票请求发票信息的项目明细
 */
public class KpInvoiceItem {
	/**
	 * 商品编码
	 */
	private String code;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 商品单价
	 */
	private String price;
	private String type;
	private String spec;

	/**
	 * 数量
	 */
	private String quantity;

	/**
	 * 单位
	 */
	private String uom;

	/**
	 * 税率
	 */
	private String taxRate;

	/**
	 * 金额
	 */
	private String amount;

	/**
	 * 备注
	 */
	private String itemRemark;

	/**
	 * 商品IMIE号
	 */
	private String imei;
	/**
	 * 商品IMIE号
	 */
	private String catalogCode;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getItemRemark() {
		return itemRemark;
	}

	public void setItemRemark(String itemRemark) {
		this.itemRemark = itemRemark;
	}

	public String getImei() {
		return imei;
	}

	public String getCatalogCode() {
		return catalogCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
}