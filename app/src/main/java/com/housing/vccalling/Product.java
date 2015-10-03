package com.housing.vccalling;

import java.net.URL;

public class Product {

    private String name;
    private Integer quantity;
    private Float price;
    private String image;
    private String username;


    public Product(String name, Integer quantity, Float price, String image, String username)
    {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
        this.username = username;
    }
    public String getName()
    {
        return name;
    }
    public Integer getQuantity()
    {
        return quantity;
    }

    public Float getPrice()
    {
        return price;
    }
    public String getImage()
    {
        return image;
    }

    public String getUsername()
    {
        return username;
    }


}
