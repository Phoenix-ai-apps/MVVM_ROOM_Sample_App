package com.symbo.insurance.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity(tableName = "InsuranceType")
public class InsuranceTypeEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "Id")
    private int id;

    @ColumnInfo(name = "Precedence")
    private int precedence;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "Description")
    private String description;

    @ColumnInfo(name = "Title")
    private String title;

    @ColumnInfo(name = "Label")
    private String label;

    @ColumnInfo(name = "BusinessType")
    private String businessType;

    @ColumnInfo(name = "ImageURL")
    private String imageURL;

    @ColumnInfo(name = "webViewURL")
    private String webviewURL;

    @ColumnInfo(name = "Category")
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getWebviewURL() {
        return webviewURL;
    }

    public void setWebviewURL(String webviewURL) {
        this.webviewURL = webviewURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /*Comparator for sorting the list by precedence*/
    public static Comparator<InsuranceTypeEntity> InsurancePrecedence = new Comparator<InsuranceTypeEntity>() {
        public int compare(InsuranceTypeEntity s1, InsuranceTypeEntity s2) {
            int rollno1 = s1.getPrecedence();
            int rollno2 = s2.getPrecedence();
            /*For ascending order*/
            return rollno1-rollno2;
            /*For descending order*/
            //rollno2-rollno1;
        }};
}
