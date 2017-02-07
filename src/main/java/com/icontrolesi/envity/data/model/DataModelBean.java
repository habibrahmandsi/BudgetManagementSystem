package com.icontrolesi.envity.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by habib on 7/25/15.
 */
public class DataModelBean {

    private int sEcho;
    private int iTotalRecords;
    private int iTotalDisplayRecords;
    private int iDisplayLength;
    private int iDisplayStart;
//    private int iDeferLoading;

    private List aaData = new ArrayList();

    public int getsEcho() {
        return sEcho;
    }

    public void setsEcho(int sEcho) {
        this.sEcho = sEcho;
    }
    public int getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public List getAaData() {
        return aaData;
    }

    public void setAaData(List aaData) {
        this.aaData = aaData;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    @Override
    public String toString() {
        return "DataModelBean{" +
                "sEcho=" + sEcho +
                ", iTotalRecords=" + iTotalRecords +
                ", iTotalDisplayRecords=" + iTotalDisplayRecords +
                ", iDisplayLength=" + iDisplayLength +
                ", iDisplayStart=" + iDisplayStart +
                ", aaData=" + aaData +
                '}';
    }
}
