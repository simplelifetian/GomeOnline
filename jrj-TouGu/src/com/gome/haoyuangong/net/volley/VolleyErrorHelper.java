package com.gome.haoyuangong.net.volley;

import java.util.HashMap;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Volley的异常列表： AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
 * NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
 * NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
 * ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
 * SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
 * TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常
 * 默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy
 * 
 * @author
 * 
 */
public class VolleyErrorHelper {

	public static String getMessage(Object error) {
		if (error instanceof TimeoutError) {
			return "网络连接异常，请稍后再试";
		} else if (isServerProblem(error)) {
			return handleServerError(error);
		} else if (isNetworkProblem(error)) {
			return "网络连接异常，请稍后再试";
		}
//		return "请求错误";
		return "网络连接异常，请稍后再试";
	}

	private static boolean isNetworkProblem(Object error) {
		return (error instanceof NetworkError) || (error instanceof NoConnectionError);
	}

	private static boolean isServerProblem(Object error) {
		return (error instanceof ServerError) || (error instanceof AuthFailureError);
	}

	private static String handleServerError(Object err) {
		VolleyError error = (VolleyError) err;
		NetworkResponse response = error.networkResponse;
		if (response != null) {
			switch (response.statusCode) {
			case 404:
			case 422:
			case 401:
				try {
//					return response.statusCode+"错误";
					return "网络连接异常，请稍后再试";

				} catch (Exception e) {
					e.printStackTrace();
				}
				// invalid request
				return error.getMessage();

			default:
//				return "未知错误";
				return "网络连接异常，请稍后再试";
			}
		}
//		return "未知错误";
		return "网络连接异常，请稍后再试";
	}
	
	
}
