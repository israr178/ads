package com.adglobi.ads;

import java.io.Serializable;
import java.util.Hashtable;

public class AdModel implements Serializable {
    public String offerid;
    public String name;
    public String logo;
    public String status;
    public String category;
    public String currency;
    public String price;
    public String model;
    public String date_start;
    public String date_end;
    public String preview_url;
    public String offer_terms;
    public String offer_kpi;
    public String country_allow;
    public String country_block;
    public String city_allow;
    public String city_block;
    public String os_allow;
    public String os_block;
    public String device_allow;
    public String device_block;
    public String isp_allow;
    public String isp_block;
    public String capping_budget_period;
    public String capping_budget;
    public String capping_conversion_period;
    public String capping_conversion;
    public String click_url;
    public String impression_url;
    public String authorized;
    public Hashtable<String,String> creatives = new Hashtable<>();
    public String events;

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public String getOffer_terms() {
        return offer_terms;
    }

    public void setOffer_terms(String offer_terms) {
        this.offer_terms = offer_terms;
    }

    public String getOffer_kpi() {
        return offer_kpi;
    }

    public void setOffer_kpi(String offer_kpi) {
        this.offer_kpi = offer_kpi;
    }

    public String getCountry_allow() {
        return country_allow;
    }

    public void setCountry_allow(String country_allow) {
        this.country_allow = country_allow;
    }

    public String getCountry_block() {
        return country_block;
    }

    public void setCountry_block(String country_block) {
        this.country_block = country_block;
    }

    public String getCity_allow() {
        return city_allow;
    }

    public void setCity_allow(String city_allow) {
        this.city_allow = city_allow;
    }

    public String getCity_block() {
        return city_block;
    }

    public void setCity_block(String city_block) {
        this.city_block = city_block;
    }

    public String getOs_allow() {
        return os_allow;
    }

    public void setOs_allow(String os_allow) {
        this.os_allow = os_allow;
    }

    public String getOs_block() {
        return os_block;
    }

    public void setOs_block(String os_block) {
        this.os_block = os_block;
    }

    public String getDevice_allow() {
        return device_allow;
    }

    public void setDevice_allow(String device_allow) {
        this.device_allow = device_allow;
    }

    public String getDevice_block() {
        return device_block;
    }

    public void setDevice_block(String device_block) {
        this.device_block = device_block;
    }

    public String getIsp_allow() {
        return isp_allow;
    }

    public void setIsp_allow(String isp_allow) {
        this.isp_allow = isp_allow;
    }

    public String getIsp_block() {
        return isp_block;
    }

    public void setIsp_block(String isp_block) {
        this.isp_block = isp_block;
    }

    public String getCapping_budget_period() {
        return capping_budget_period;
    }

    public void setCapping_budget_period(String capping_budget_period) {
        this.capping_budget_period = capping_budget_period;
    }

    public String getCapping_budget() {
        return capping_budget;
    }

    public void setCapping_budget(String capping_budget) {
        this.capping_budget = capping_budget;
    }

    public String getCapping_conversion_period() {
        return capping_conversion_period;
    }

    public void setCapping_conversion_period(String capping_conversion_period) {
        this.capping_conversion_period = capping_conversion_period;
    }

    public String getCapping_conversion() {
        return capping_conversion;
    }

    public void setCapping_conversion(String capping_conversion) {
        this.capping_conversion = capping_conversion;
    }

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    public String getImpression_url() {
        return impression_url;
    }

    public void setImpression_url(String impression_url) {
        this.impression_url = impression_url;
    }

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }

    public Hashtable<String, String> getCreatives() {
        return creatives;
    }

    public void setCreatives(Hashtable<String, String> creatives) {
        this.creatives = creatives;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }
}
