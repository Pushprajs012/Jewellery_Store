package com.ps.jewellerystore.Data;

public class JewelleryData {
    private String name;
    private int price;
    private String weight;
    private String image;
    private String product_id;

    public JewelleryData(String name, int price, String weight, String image, String product_id) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.image = image;
        this.product_id = product_id;
    }





    public JewelleryData() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

}
