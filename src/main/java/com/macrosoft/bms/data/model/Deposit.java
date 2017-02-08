package com.macrosoft.bms.data.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="deposit")
public class Deposit {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(insertable=false,updatable=false)
    private Integer id;

    @Column(name="amount")
    private Double amount;

    @Column(name="date")
    private Date date;

    @Column(name="method")
    private String method;

    @Column(name="reference_no")
    private String referenceNo;

    @ManyToOne
    @JoinColumn(name="share_holder_id")
    private ShareHolder shareHolder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public ShareHolder getShareHolder() {
        return shareHolder;
    }

    public void setShareHolder(ShareHolder shareHolder) {
        this.shareHolder = shareHolder;
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", method='" + method + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", shareHolder=" + shareHolder +
                '}';
    }
}