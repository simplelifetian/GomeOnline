
package com.gome.haoyuangong.update;

/**
 * Specified exception for network error.<br>
 * 
 * @author <a href="mailto:zhengzhaomail@gmail.com">Zheng Zhao</a>
 */
public class NetworkException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6825704733952663884L;

    public NetworkException() {
        super();
    }

    public NetworkException(String msg) {
        super(msg);
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }

    public NetworkException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
