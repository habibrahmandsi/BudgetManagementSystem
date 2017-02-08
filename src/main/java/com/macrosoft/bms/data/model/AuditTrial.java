package com.macrosoft.bms.data.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "audit_trial")
public class AuditTrial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "user_session_id")
    private String userSessionId;

    @Column(name = "activity_date")
    private Date activityDate;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "device_details")
    private String deviceDetails;

    @Column(name = "os_details")
    private String osDetails;

    @Column(name = "browser_details")
    private String browserDetails;

    @Column(name = "activity_desc")
    private String activityDesc;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "is_mobile_device")
    private Boolean isMobileDevice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(String deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public String getOsDetails() {
        return osDetails;
    }

    public void setOsDetails(String osDetails) {
        this.osDetails = osDetails;
    }

    public String getBrowserDetails() {
        return browserDetails;
    }

    public void setBrowserDetails(String browserDetails) {
        this.browserDetails = browserDetails;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Boolean getIsMobileDevice() {
        return isMobileDevice;
    }

    public void setIsMobileDevice(Boolean isMobileDevice) {
        this.isMobileDevice = isMobileDevice;
    }

    @Override
    public String toString() {
        return "AuditTrialItem{" +
                "id=" + id +
                ", user=" + user +
                ", userSessionId='" + userSessionId + '\'' +
                ", activityDate=" + activityDate +
                ", ipAddress='" + ipAddress + '\'' +
                ", deviceDetails='" + deviceDetails + '\'' +
                ", osDetails='" + osDetails + '\'' +
                ", browserDetails='" + browserDetails + '\'' +
                ", activityDesc='" + activityDesc + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", isMobileDevice=" + isMobileDevice +
                '}';
    }
}
