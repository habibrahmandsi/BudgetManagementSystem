package com.macrosoft.bms.data.model;

/**
 * Created by habib on 7/25/15.
 */
public class DataModelForTypeAhead {

    private Boolean status;
    private String error;
    private ShareHolder data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ShareHolder getData() {
        return data;
    }

    public void setData(ShareHolder data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataModelForTypeAhead{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}
