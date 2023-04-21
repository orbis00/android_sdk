package com.wigzo.android.models;

import com.wigzo.android.base.ValidationException;
import com.wigzo.android.helpers.StringUtils;
import com.wigzo.android.base.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class EventMetaData extends Model {

    private String title;
    private String description;
    private String productId;
    private Double price;
    private String url;

    public EventMetaData() {}
    public EventMetaData(String title, String description, String productId, Double price, String url) {
        this.title = title;
        this.description = description;
        this.productId = productId;
        this.price = price;
        this.url = url;
    }

    public EventMetaData setTitle(String title) { this.title = title; return this; }
    public EventMetaData setDescription(String description) { this.description = description; return this; }
    public EventMetaData setProductId(String productId) { this.productId = productId; return this; }
    public EventMetaData setPrice(Double price) { this.price = price; return this; }
    public EventMetaData setUrl(String url) { this.url = url; return this; }

    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getProductId() { return this.productId; }
    public Double getPrice() { return this.price; }
    public String getUrl() { return this.url; }

    @Override
    public JSONObject toJson() {
        JSONObject eventMetaData = new JSONObject();
        try {
            if(StringUtils.isNotEmpty(this.title)){
                eventMetaData.put("title", this.title);
            }
            if(StringUtils.isNotEmpty(this.description)){
                eventMetaData.put("description", this.description);
            }
            if(StringUtils.isNotEmpty(this.productId)){
                eventMetaData.put("productId", this.productId);
            }
            if(this.price!=null){
                eventMetaData.put("price", this.price);
            }
            if(StringUtils.isNotEmpty(this.url)){
                eventMetaData.put("url", this.url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventMetaData;
    }

    @Override
    public boolean isValid() throws ValidationException {
        if (StringUtils.allEmpty(this.title, this.description, this.productId, this.url) && this.price !=null) {
            throw new ValidationException("Metadata should contain at least: \n" +
                    "title (String), description (String), productId (String), price (String), url (String)");
        }
        return true;
    }
}
