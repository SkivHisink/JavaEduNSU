package com.exlabwork2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NasdaqData {
    private long id;
    private String datasetCode;
    private String databaseCode;
    private String name;
    private String description;
    private String refreshedAt;
    private String newestAvailableDate;
    private String oldestAvailableDate;
    private List<String> columnNames;
    private String frequency;
    private String type;
    private boolean premium;
    private Long limit;
    private String transform;
    private int columnIndex;
    private String startDate;
    private String endDate;
    private List<List<Object>> data;
    private long databaseId;

    private long collapse;
    private long order;
    // Геттеры
    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("dataset_code")
    public String getDatasetCode() {
        return datasetCode;
    }

    @JsonProperty("database_code")
    public String getDatabaseCode() {
        return databaseCode;
    }

    @JsonProperty("database_id")
    public long getDatabaseId() {
        return databaseId;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("refreshed_at")
    public String getRefreshedAt() {
        return refreshedAt;
    }

    @JsonProperty("newest_available_date")
    public String getNewestAvailableDate() {
        return newestAvailableDate;
    }

    @JsonProperty("oldest_available_date")
    public String getOldestAvailableDate() {
        return oldestAvailableDate;
    }

    @JsonProperty("column_names")
    public List<String> getColumnNames() {
        return columnNames;
    }

    @JsonProperty("frequency")
    public String getFrequency() {
        return frequency;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("premium")
    public boolean isPremium() {
        return premium;
    }

    @JsonProperty("limit")
    public Long getLimit() {
        return limit;
    }

    @JsonProperty("transform")
    public String getTransform() {
        return transform;
    }

    @JsonProperty("column_index")
    public int getColumnIndex() {
        return columnIndex;
    }

    @JsonProperty("start_date")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("end_date")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("data")
    public List<List<Object>> getData() {
        return data;
    }

    @JsonProperty("collapse")
    public long getCollapse() {
        return collapse;
    }

    @JsonProperty("order")
    public long getOrder() {
        return order;
    }

    // Сеттеры

    public void setOrder(long order) {
        this.order = order;
    }
    public void setDatabaseId(long databaseId) {
        this.databaseId = databaseId;
    }

    public void setCollapse(long collapse) {
        this.collapse = collapse;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDatasetCode(String datasetCode) {
        this.datasetCode = datasetCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRefreshedAt(String refreshedAt) {
        this.refreshedAt = refreshedAt;
    }

    public void setNewestAvailableDate(String newestAvailableDate) {
        this.newestAvailableDate = newestAvailableDate;
    }

    public void setOldestAvailableDate(String oldestAvailableDate) {
        this.oldestAvailableDate = oldestAvailableDate;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }
}
