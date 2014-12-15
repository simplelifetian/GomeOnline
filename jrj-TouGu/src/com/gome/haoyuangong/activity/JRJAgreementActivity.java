/**
 * 
 */
package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;

import android.os.Bundle;
import android.webkit.WebView;



/**
 * 
 */
public class JRJAgreementActivity extends BaseActivity{
	
	private static final String TAG = JRJAgreementActivity.class.getName();


	private WebView helpContent;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		initView();
	}


	public void initView() {
		setTitle("");

		helpContent = (WebView) findViewById(R.id.content);
		helpContent.getSettings().setDefaultTextEncodingName("utf-8");
		
		String c = "<h1>1.为什么要用盈利宝股票交易？</h1><p>盈利宝与中信证券股份有限公司达成战略合作，使用盈利宝股票交易可享受超低佣金，让您的投资回报更高，让您的资金更安全！快捷方便的开户操作、买卖交易、查询历史，让您的投资更加简单，不错过任何一次投资机会。</p><h1>2.进行股票交易要使用什么账户？</h1><p>直接注册\\登录盈利宝账户，绑定中信证券账户。即可进行买卖撤查，无需另外注册新账户。</p><h1>3.盈利宝股票交易的功能？</h1><p>通过盈利宝账户可以实现股票委托买入、卖出、撤单操作。支持查询账户资金，并进行银证转账功能。</p><h1>4.盈利宝如何绑定券商？</h1><p>目前盈利宝股票交易支持绑定中信证券账户。拥有中信证券账户用户，可直在我的券商绑定中信证券账户。</p><h1>5.如何开通中信证券账户？</h1><p>打开盈利宝官方网站<a href=\\\"https://t.jrj.com\\\">https://t.jrj.com</a>，登录盈利宝账户，选择券商开户，按照页面操作流程操作即可。</p><h1>6.盈利宝账户安全吗？</h1><p>盈利宝严格执行中国证监会《客户交易结算资金管理办法》，账户资金由民生银行全程监管，确保账户资金安全。且资金仅可以在本人实名验证绑定的银行卡内遵循同卡进出原则，如用A卡存入的钱最终只能取回到A卡，无法转入到其他卡内。</p><h1>7.盈利宝支持哪些银行？可以绑定多少张银行卡？</h1><p>目前盈利宝支持工商银行、建设银行、农业银行、中国银行、平安银行、兴业银行、交通银行、招商银行、浦发银行，后续将提供更多银行。同一身份信息可以绑定多张银行卡，暂无上限。</p><h1>8.盈利宝存取款需要收手续费么？</h1><p>盈利宝存取款不收任何手续费。</p>";
//		HtmlContentResponse responseData = JSONUtils.parseObject(c, HtmlContentResponse.class);
//		String content = StringUtils.formatHtmlFragment(responseData.getData().getContent());
		helpContent.loadDataWithBaseURL(null, c, "text/html", "utf-8", null);
		
//		helpContent.loadUrl("http://test.mapi.jrj.com.cn/cmsdata/getCmsSpec?chnmbclsid=611002012");
	}

	


}
