package com.devstream.phever.model;

import org.w3c.dom.Text;

/**
 * Created by john on 26/04/15.
 */
public class Event {
    private String  edate;
    private String edateSort;
    private String  etime;
    private String  name;
    private String  location;
    private String  headline;
    private String  headlineDesc;
    private String imageUrl;
    private String  price;
    private String  purchase;
    private String supportActs;
    private String terms;

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getEdateSort() {
        return edateSort;
    }

    public void setEdateSort(String edateSort) {
        this.edateSort = edateSort;
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

    public String getSupportActs() {
        return supportActs;
    }

    public void setSupportActs(String supportActs) {
        this.supportActs = supportActs;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
