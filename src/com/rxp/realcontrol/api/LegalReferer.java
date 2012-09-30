package com.rxp.realcontrol.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class LegalReferer {
	@Element(required=false)
	public String legalReferer;
	
}
