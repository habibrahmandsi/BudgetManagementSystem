package com.macrosoft.bms.data.model.Entity;

import javax.persistence.*;

/**
 * Created by habib on 7/12/17.
 */
@Entity
@Table(name = "user2")
public class User{
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(insertable=false,updatable=false)
    private Long id;

    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
