package com.gome.haoyuangong.global;

/**
 * <h1>服务器配置</h1>
 * @author 
 */
public class ServerConfig {
	String ServerIp;
	String ServerPort;
	String ServerContext;
	
	String ServerUrl;
	
	private static ServerConfig instance = new ServerConfig();
	
	public static ServerConfig getInstance() {
		return instance;
	}
	
	private ServerConfig() {
		
	}
	
	public String getServerIp() {
		return ServerIp;
	}

	public void setServerIp(String ServerIp) {
		this.ServerIp = ServerIp;
	}

	public String getServerPort() {
		return ServerPort;
	}

	public void setServerPort(String ServerPort) {
		this.ServerPort = ServerPort;
	}

	public String getServerContext() {
		return ServerContext;
	}

	public void setServerContext(String ServerContext) {
		this.ServerContext = ServerContext;
	}

	public String getServerUrl() {
		return ServerUrl;
	}

	public void setServerUrl(String ServerUrl) {
		this.ServerUrl = ServerUrl;
	}
}
