package com.mihalichman.testtask;

public class Product {
    private Integer id;
    private String name;
    private String condition;
    private String state;
    private Float price;


    public Product(Integer id, String name, String condition, String state, float price){
        this.id = id;
        this.name = name;
        this.condition = condition;
        this.state = state;
        this.price = price;
    }

    public Product(){ }

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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
