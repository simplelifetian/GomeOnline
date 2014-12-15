package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.url.AskUrl;
import com.qq.taf.jce.dynamic.StringField;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends BaseActivity{
	public static final String BUNDLE_URL="url";
	public static final String BUNDLE_TITLE="title";
	private String url;
	private String title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_detail_web);
		url = getIntent().getStringExtra(BUNDLE_URL);
		title = getIntent().getStringExtra(BUNDLE_TITLE);
		setTitle(title);
		if(url==null){
			Logger.error("AskDetailWebActivity", "invalid askId");
			finish();
			return;
		}
		WebView view  = (WebView) findViewById(R.id.webview);
		 WebSettings webSettings = view.getSettings();
	    webSettings.setJavaScriptEnabled(true);
	    webSettings.setSupportZoom(true);
	    view.setWebViewClient(webViewClient);
	    // 设置setWebChromeClient对象
	    view.setWebChromeClient(webChromeClient);
		view.loadUrl(url);
	}
	 /**
   * 创建WebViewClient对象,在WebView的设计中，无论什么事都要WebView类处理，有些工作可以让其他类做，
   * 这样WebView就可以专心解析、渲染。WebViewClient就是帮助WebView处理各种通知、请求事件。
   */
  WebViewClient webViewClient = new WebViewClient() {
    /**
     * 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
     * 如果希望点击网页中某链接继续在当前WebView中响应，而不是新开Android的系统browser中响应该链接，必须覆盖
     * webview的WebViewClient对象
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      // Toast.makeText(WebViewAc.this,"shouldOverrideUrlLoading",Toast.LENGTH_LONG).show();
      //      System.out.println("---webViewClient--shouldOverrideUrlLoading" + url);
//      if (url.startsWith("goto:")) {
//        try {
//          gotoNextContent(url.substring(5));
//        } catch (Exception e) {
//        }
//      }else{
        view.loadUrl(url);
//      }
      return true;
    }

    // 开始加载网页时要做的工作
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      super.onPageStarted(view, url, favicon);
    }

    // 加载完成时要做的工作
    @Override
    public void onPageFinished(WebView view, String url) {
      super.onPageFinished(view, url);
    }

    // 加载错误时要做的工作
    @Override
    public void onReceivedError(WebView view, int errorCode,
        String description, String failingUrl) {
      super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
      super.onLoadResource(view, url);
    }

    /**
     * 获取用户在网页上的键盘事件
     */
    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
      return super.shouldOverrideKeyEvent(view, event);
    }
  };

  /**
   * 创建WebViewChromeClient对象，实际上它是处理javascript的引擎
   * WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
   * 也可使webview客户端能获知用户在html页面中所做的操作，从而做出不同的处理。
   */
  WebChromeClient webChromeClient = new WebChromeClient() {
    /**
     * Javascript弹出框有如下三种alert(提示对话框),confirm(选择对话框),prompt(带输入的对话框)
     * 处理Alert事件,对应html文件中的alert事件
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message,
        JsResult result) {
       return super.onJsAlert(view,url,message,result);
    }
    /**
     * 处理confirm事件,对应html文件中的confirm事件
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
        JsResult result) {
       return super.onJsConfirm(view,url,message,result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
        String defaultValue, JsPromptResult result) {
      // 处理提示事件
      // 看看默认的效果
      //      System.out.println("---webChromeClient--onJsPrompt");
      // Toast.makeText(WebViewAc.this,"onJsPrompt",Toast.LENGTH_LONG).show();
      // return true;
      // 系统弹出提示框
      return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    // 设置应用程序标题
    @Override
    public void onReceivedTitle(WebView view, String title) {
      super.onReceivedTitle(view, title);
    }
  };
	public static void gotoWebViewActivity(Context ctx, String title,String url){
		Intent i = new Intent(ctx, WebViewActivity.class);
		i.putExtra(BUNDLE_TITLE, title);
		i.putExtra(BUNDLE_URL, url);
		ctx.startActivity(i);
	}
}
