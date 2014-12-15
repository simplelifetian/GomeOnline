package com.gome.haoyuangong.global;


import com.gome.haoyuangong.R;
import com.gome.haoyuangong.utils.SharedXml;
import com.gome.haoyuangong.utils.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * This class holds the configuration of server
 * @author 
 */
public class ServerManager {
	
	public static final String PREFS_NAME = "server_info";
	
	private static final String HTTP_PREFIX = "http://";
	
	private final static String SERVER_IP = "serverIp";
	private final static String SERVER_PORT = "serverPort";
	private final static String SERVER_CONTEXT = "serverContext";
	
	private Context mContext;
	
	private String server_ip;
	private String server_port;
	private String server_context;
	private String server_url;
	
	public ServerManager(Context context) {
		this.mContext = context;
		init(context);
	}
	
	private void init(Context context) {
		initServer();
		initServerConfig();
	}
	
	private void initServerConfig() {
		ServerConfig serverConfig = ServerConfig.getInstance();
		serverConfig.setServerIp(this.server_ip);
		serverConfig.setServerPort(this.server_port);
		serverConfig.setServerContext(this.server_context);
		
		serverConfig.setServerUrl(this.server_url);
	}
	
	private void initServer() {
		int indexInIpArrays = Integer.valueOf(mContext.getString(R.string.ip_default_index));
		/*if(!GlobalApplication.getInstance().isDebugMode()) {
			indexInIpArrays = 3;
		}*/
		
		String[] ipPortContext = Utils.getDefaultIpPortContext(mContext, 
				indexInIpArrays, R.array.ip_lists);
		SharedPreferences prefer = mContext.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String serverIp = SharedXml.getString(prefer, SERVER_IP, ipPortContext[0]);
		String serverPort = SharedXml.getString(prefer, SERVER_PORT, ipPortContext[1]);
		String serverContext = SharedXml.getString(prefer, SERVER_CONTEXT, ipPortContext[2]);
		
		initLocal(serverIp, serverPort, serverContext);
	}
	
	private void initLocal(String serverIp, String serverPort, String serverContext) {
		this.server_ip = serverIp;
		this.server_port = serverPort;
		this.server_context = serverContext;
		
		this.server_url = HTTP_PREFIX + server_ip + ":" + server_port + "/" + server_context;
	}
	
	/**
	 * change the configuration of server.
	 * <p>firstly, save the strings into file, sencondly, change the values in this object.</p>
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @param serverContext
	 */
	public void changeServerConfig(String serverIp, String serverPort, String serverContext) {
		SharedPreferences prefer = mContext.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor editor = prefer.edit();
		editor.putString(SERVER_IP, serverIp);
		editor.putString(SERVER_PORT, serverPort);
		editor.putString(SERVER_CONTEXT, serverContext);
		editor.commit();
		
		initServerConfig();
	}
}
