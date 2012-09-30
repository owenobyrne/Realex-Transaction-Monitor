package com.rxp.realcontrol.api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class MerchantDetail {
	@Attribute 
	public String fiid;
	
	@Attribute
	public String currency;
	
	@Attribute
	public String paymentMethod; 
}
