package com.exlabwork2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NasdaqDatabase {
    private long id;
    private String name;
    private String databaseCode;
    private String description;
    private long datasetsCount;
    private long downloads;
    private boolean premium;
    private String image;
    private boolean favorite;
    private String urlName;
    private boolean exclusive;

    // Геттеры и сеттеры

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("database_code")
    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("datasets_count")
    public long getDatasetsCount() {
        return datasetsCount;
    }

    public void setDatasetsCount(long datasetsCount) {
        this.datasetsCount = datasetsCount;
    }

    @JsonProperty("downloads")
    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    @JsonProperty("premium")
    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("favorite")
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @JsonProperty("url_name")
    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    @JsonProperty("exclusive")
    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }
}