package net.ezplanet.shopping.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Offer {

    @Id
    private String code;

    private int threshold;

    private int free;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public Offer () {}

    public Offer(String code, int threshold, int free) {
        this.code      = code;
        this.threshold = threshold;
        this.free      = free;
    }
}
