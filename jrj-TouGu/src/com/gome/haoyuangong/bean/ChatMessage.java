package com.gome.haoyuangong.bean;

public class ChatMessage {
	
	public ChatMessage() {  
    }  
	
	public static final String TABLE_NAME = "chat_message";
	
    private int _id;  
    private int type;  
    private String content;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ChatMessage [Id=" + _id + ", type=" + type
				+ ", content="+content + "]";
	} 
    
    

}
