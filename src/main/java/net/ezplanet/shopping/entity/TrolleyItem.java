package net.ezplanet.shopping.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class TrolleyItem {

    //@Id
    //private String id;

    @Id
    private String item;

    private String offer;

    private int quantity;

    private BigDecimal price;

    public TrolleyItem() {}

    public TrolleyItem(String item, int quantity) {
        //this.id       = id;
        this.item     = item;
        //this.offer    = offer;
        this.quantity = quantity;
        //this.price    = price;
    }
/*
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
*/
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    /*
    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }
*/
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
/*
    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }
*/
}
