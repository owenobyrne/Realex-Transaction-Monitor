//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.25 at 11:25:34 PM IST 
//


package com.rxp.realcontrol.api;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;



/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{}baseEntity">
 *       &lt;sequence>
 *         &lt;element name="nextRange" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}int" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="currentRange" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}int" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="transaction" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="currency" type="{}amountCurrency" minOccurs="0"/>
 *                   &lt;element name="name" type="{}cardHolderNameInList"/>
 *                   &lt;element name="orderid" type="{}orderId"/>
 *                   &lt;element name="timestamp" type="{}dateTimeStamp"/>
 *                   &lt;element name="accountName" type="{}accountName"/>
 *                   &lt;element name="result" type="{}resultCode" minOccurs="0"/>
 *                   &lt;element name="varref" type="{}variableReferenceRC" minOccurs="0"/>
 *                   &lt;element name="prodid" type="{}productIdRC" minOccurs="0"/>
 *                   &lt;element name="custnum" type="{}customerNumberRC" minOccurs="0"/>
 *                   &lt;element name="guid" type="{}guid"/>
 *                   &lt;element name="cardtype" type="{}cardType"/>
 *                   &lt;element name="comment1" type="{}commentSimple" minOccurs="0"/>
 *                   &lt;element name="comment2" type="{}commentSimple" minOccurs="0"/>
 *                   &lt;element name="status" type="{}transactionStatus"/>
 *                   &lt;element name="eci" type="{}mpiEci" minOccurs="0"/>
 *                   &lt;element name="fraudScore" type="{}realScoreTotal" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="totalNumTransactions" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Root
public class Transactions {

	@Element(required = false)
    public Transactions.NextRange nextRange;
	@Element(required = false)
    public Transactions.CurrentRange currentRange;
    @ElementList(inline=true)
    public List<Transactions.Transaction> transaction;
    @Element(required = false)
    public Integer totalNumTransactions;

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}int" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */

    @Root
    public static class CurrentRange {
    	@Element(required = false)
        public int offset;
    	@Element(required = false)
        public int max;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}int" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */

    @Root
    public static class NextRange {
    	@Element(required = false)
    	public int offset;
    	@Element(required = false)
    	public int max;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="currency" type="{}amountCurrency" minOccurs="0"/>
     *         &lt;element name="name" type="{}cardHolderNameInList"/>
     *         &lt;element name="orderid" type="{}orderId"/>
     *         &lt;element name="timestamp" type="{}dateTimeStamp"/>
     *         &lt;element name="accountName" type="{}accountName"/>
     *         &lt;element name="result" type="{}resultCode" minOccurs="0"/>
     *         &lt;element name="varref" type="{}variableReferenceRC" minOccurs="0"/>
     *         &lt;element name="prodid" type="{}productIdRC" minOccurs="0"/>
     *         &lt;element name="custnum" type="{}customerNumberRC" minOccurs="0"/>
     *         &lt;element name="guid" type="{}guid"/>
     *         &lt;element name="cardtype" type="{}cardType"/>
     *         &lt;element name="comment1" type="{}commentSimple" minOccurs="0"/>
     *         &lt;element name="comment2" type="{}commentSimple" minOccurs="0"/>
     *         &lt;element name="status" type="{}transactionStatus"/>
     *         &lt;element name="eci" type="{}mpiEci" minOccurs="0"/>
     *         &lt;element name="fraudScore" type="{}realScoreTotal" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @Root
    public static class Transaction {
    	@Element(required = false)
    	public int amount;
    	@Element(required = false)
        public String currency;
    	@Element(required = false)
        public String name;
    	@Element(required = false)
        public String orderid;
    	@Element(required = false)
    	public String timestamp;
    	@Element(required = false)
    	public String accountName;
    	@Element(required = false)
    	public String result;
    	@Element(required = false)
        public String varref;
    	@Element(required = false)
        public String prodid;
    	@Element(required = false)
        public String custnum;
    	@Element(required = false)
        public String guid;
    	@Element(required = false)
        public String cardtype;
    	@Element(required = false)
        public String comment1;
    	@Element(required = false)
        public String comment2;
    	@Element(required = false)
        public String status;
    	@Element(required = false)
    	public String eci;
    	@Element(required = false)
    	public String fraudScore;

    }
}