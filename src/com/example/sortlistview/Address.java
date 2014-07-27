package com.example.sortlistview;

import java.util.Map;

public class Address {

	private String area;
	//private Server server;
	// ÓÃMapÊµÏÖ
	private Map<String, Integer> serverMap;
	
	public Address(String area,Map<String, Integer> serverMap){
		this.area=area;
		this.serverMap=serverMap;
	}
	public Address(){
		
	}
	public String getArea(){
		return area;
	}
	public void setArea(String area){
		this.area=area;
	}
	public Map<String, Integer> getServer(){
		return serverMap;
	}
	public void setseverMap(Map<String, Integer> serverMap){
	//	this.server.setID(id);
		this.serverMap=serverMap;
		
	}
}
