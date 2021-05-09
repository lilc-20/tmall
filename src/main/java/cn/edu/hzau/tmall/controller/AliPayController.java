package cn.edu.hzau.tmall.controller;

import cn.edu.hzau.tmall.util.AlipayConfig;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AliPayController extends BaseController {

    @ResponseBody
    @RequestMapping(value = "pay")
    public String pay() throws AlipayApiException {
        logger.info("支付");
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type
        );
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String("202105091320250118");
        //付款金额，必填
        String total_amount = new String("4088");
        //订单名称，必填
        String subject = new String("Huawei/华为 P20 全面屏徕卡双摄正品4G手机");
        //商品描述，可空
        String body = new String("");

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        alipayRequest.setReturnUrl(AlipayConfig.return_url);

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "refund")
    public String refund() throws AlipayApiException {
        logger.info("退款");
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type
        );

        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        //商户订单号，商户网站订单系统中唯一订单号
//        String out_trade_no = new String();
        //支付宝交易号
        String trade_no = new String();
        //请二选一设置
        //需要退款的金额，该金额不能大于订单金额，必填
        String refund_amount = new String();
        //退款的原因说明
        String refund_reason = new String();
        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        String out_request_no = new String();

        alipayRequest.setBizContent("\"trade_no\":\""+ trade_no +"\","
                + "\"refund_amount\":\""+ refund_amount +"\","
                + "\"refund_reason\":\""+ refund_reason +"\","
                + "\"out_request_no\":\""+ out_request_no +"\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "close")
    public String close() throws AlipayApiException {
        logger.info("关闭交易");
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type
        );

        //设置请求参数
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        //商户订单号，商户网站订单系统中唯一订单号
//        String out_trade_no = new String("");
        //支付宝交易号
        String trade_no = new String();
        //请二选一设置

        alipayRequest.setBizContent("{\"trade_no\":\""+ trade_no +"\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "refundQuery")
    public String refund_query() throws AlipayApiException {
        logger.info("退款查询");
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type
        );

        //设置请求参数
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();

        //商户订单号，商户网站订单系统中唯一订单号
//        String out_trade_no = new String();
        //支付宝交易号
        String trade_no = new String();
        //请二选一设置
        //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号，必填
        String out_request_no = new String();

        alipayRequest.setBizContent("{\"trade_no\":\""+ trade_no +"\","
                +"\"out_request_no\":\""+ out_request_no +"\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "query")
    public String query() throws AlipayApiException {
        logger.info("交易查询");
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type
        );

        //设置请求参数
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();

        //商户订单号，商户网站订单系统中唯一订单号
//        String out_trade_no = new String();
        //支付宝交易号
        String trade_no = new String();
        //请二选一设置
        //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号，必填
        String out_request_no = new String();

        alipayRequest.setBizContent("{\"trade_no\":\""+ trade_no +"\","
                +"\"out_request_no\":\""+ out_request_no +"\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }

}
