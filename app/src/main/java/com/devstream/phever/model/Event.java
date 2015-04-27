package com.devstream.phever.model;

import org.w3c.dom.Text;

/**
 * Created by john on 26/04/15.
 */
public class Event {
    private String  edate;
    private String  etime;
    private String  name;
    private String  location;
    private String  headline;
    private String  headlineDesc;
    private String  price;
    private String  purchase;
    private Text supportActs;
    private Text terms;

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getHeadlineDesc() {
        return headlineDesc;
    }

    public void setHeadlineDesc(String headlineDesc) {
        this.headlineDesc = headlineDesc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public Text getSupportActs() {
        return supportActs;
    }

    public void setSupportActs(Text supportActs) {
        this.supportActs = supportActs;
    }

    public Text getTerms() {
        return terms;
    }

    public void setTerms(Text terms) {
        this.terms = terms;
    }


}
