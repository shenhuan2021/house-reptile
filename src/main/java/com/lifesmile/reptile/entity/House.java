package com.lifesmile.reptile.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class House implements Serializable {
    private String id;
    private String title;
    private String url;
    private String city;
    private String region;
    private String street;
    private String community;
    private String floor;
    private Double totalPrice;
    private Double averagePrice;
    private String image;
    private Integer watch;
    private Integer view;
    private String releaseDate;
    //private String roomCount;
    //private String towards;
    private Double houseArea;
   // private String decoration;
   // private String elevator;
    //private String createDate;
    private String info;
}
