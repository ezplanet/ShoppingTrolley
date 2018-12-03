package net.ezplanet.shopping.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    private String name;
    private String offer;
    private BigDecimal price;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public Product() {}

    public Product(String name, BigDecimal price) {
        this.name  = name;
        this.price = price;
    }

    public Product(String name, String offer, BigDecimal price) {
        this.name  = name;
        this.offer = offer;
        this.price = price;
    }

    public Product(Builder builder) {
        this.name  = builder.name;
        this.price = builder.price;

    }

    public static Builder builder() { return new Builder();}

    public static class Builder {
        private String name;
        private BigDecimal price;

        public Builder withName(String val) {
            this.name = val;
            return this;
        }
        public Builder withPrice(BigDecimal val) {
            this.price = val;
            return this;
        }

        public Product build() {return new Product(this);}
    }

}
