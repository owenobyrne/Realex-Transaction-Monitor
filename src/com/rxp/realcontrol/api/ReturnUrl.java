package com.rxp.realcontrol.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ReturnUrl {
	@Element(required=false)
	public String returnUrl;
}
