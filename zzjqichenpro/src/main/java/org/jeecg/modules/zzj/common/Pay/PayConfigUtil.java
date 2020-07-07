package org.jeecg.modules.zzj.common.Pay;

/**
 * 微信参数配置
 */
public class PayConfigUtil {
	// 初始化
	public final static String APP_ID = "wxf6d2926083a4183d"; // 公众账号appid（改为自己实际的）
	public final static String APP_SECRET = "";
	public final static String MCH_ID = "1527691441"; // 商户号（改为自己实际的）
	public final static String API_KEY = "jGpRn308fFiIbw6IrgFsBCAtVwg3kqdg"; // （改为自己实际的）key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置

	// 统一下单
	public final static String UFDODER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public final static String NOTIFY_URL = "http://xxxxxxx"; // 微信支付回调接口，就是微信那边收到（改为自己实际的）
	// 企业向个人账号付款的URL
	public final static String SEND_EED_PACK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

	public final static String CREATE_IP = "113.69.246.11";// 发起支付ip（改为自己实际的）

}
