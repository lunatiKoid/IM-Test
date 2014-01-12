package com.alibaba.rfq.sourcingfriends.dto;

import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

public class UserProfileDTO {

    private String userName;
    private Bitmap photo;
    private String bizType;
    private String companyName;
    private String address;
    private String miniSite;
    private long   added;
    private String mainProducts;
    private String recentlyQuotedBRequest;
    private String quoTotalLastMonth;
    private String quoAcLastMonth;

    public UserProfileDTO(String name, Bitmap photo, String company, String mainProducts) {
        this.userName = name;
        this.photo = photo;
        this.bizType = "";
        this.companyName = company;
        this.address = "";
        this.miniSite = "";
        this.added = 0;
        this.mainProducts = mainProducts;
        this.recentlyQuotedBRequest = "";
        this.quoTotalLastMonth = "";
        this.quoAcLastMonth = "";
    }

    public UserProfileDTO(String name, Bitmap photo, String bizType, String company, String address, String miniSite,
                          long added, String mainProducts, String recentlyQuotedBRequest, String quoTotal, String quoAc) {
        this.userName = name;
        this.photo = photo;
        this.bizType = bizType;
        this.companyName = company;
        this.address = address;
        this.miniSite = miniSite;
        this.added = added;
        this.mainProducts = mainProducts;
        this.recentlyQuotedBRequest = recentlyQuotedBRequest;
        this.quoTotalLastMonth = quoTotal;
        this.quoAcLastMonth = quoAc;
    }

    
    public String getQuoTotalLastMonth() {
        return quoTotalLastMonth;
    }

    
    public void setQuoTotalLastMonth(String quoTotalLastMonth) {
        this.quoTotalLastMonth = quoTotalLastMonth;
    }

    
    public String getQuoAcLastMonth() {
        return quoAcLastMonth;
    }

    
    public void setQuoAcLastMonth(String quoAcLastMonth) {
        this.quoAcLastMonth = quoAcLastMonth;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMiniSite() {
        return miniSite;
    }

    public void setMiniSite(String miniSite) {
        this.miniSite = miniSite;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public String getMainProducts() {
        return mainProducts;
    }

    public void setMainProducts(String mainProducts) {
        this.mainProducts = mainProducts;
    }

    public String getRecentlyQuotedBRequest() {
        return recentlyQuotedBRequest;
    }

    public void setRecentlyQuotedBRequest(String recentlyQuotedBRequest) {
        this.recentlyQuotedBRequest = recentlyQuotedBRequest;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
