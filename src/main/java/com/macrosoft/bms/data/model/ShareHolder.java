package com.macrosoft.bms.data.model;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="share_holder")
public class ShareHolder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(insertable=false,updatable=false)
    private Integer id;

    private String name;

    @Column(name = "sex")
    private String sex;

    @Column(name = "religion")
    private String religion;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "photo_name")
    private String photoName;

    @Column(name="fathers_name")
    private String fathersName;

    @Column(name="mothers_name")
    private String mothersName;

    @Column(name="spouse_name")
    private String spouseName;

    @Column(name="current_address")
    private String currentAddress;

    @Column(name="permanent_address")
    private String permanentAddress;

    @Column(name="email")
    private String email;

    @Transient
    private String binaryFileData;

    @Transient
    private MultipartFile fileData;

    @Transient
    List<ShareHolder> shareHolderList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getMothersName() {
        return mothersName;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }

    public String getBinaryFileData() {
        return binaryFileData;
    }

    public void setBinaryFileData(String binaryFileData) {
        this.binaryFileData = binaryFileData;
    }

    public List<ShareHolder> getShareHolderList() {
        return shareHolderList;
    }

    public void setShareHolderList(List<ShareHolder> shareHolderList) {
        this.shareHolderList = shareHolderList;
    }

    @Override
    public String toString() {
        return "ShareHolder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", religion='" + religion + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", photoName='" + photoName + '\'' +
                ", fathersName='" + fathersName + '\'' +
                ", mothersName='" + mothersName + '\'' +
                ", spouseName='" + spouseName + '\'' +
                ", currentAddress='" + currentAddress + '\'' +
                ", permanentAddress='" + permanentAddress + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}