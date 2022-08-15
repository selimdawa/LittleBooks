package com.flatcode.littlebooks.Model;

public class ADs {

    String name;
    int adsLoadedCount, adsClickedCount;

    public ADs() {

    }

    public ADs(String name, int adsLoadedCount, int adsClickedCount) {
        this.name = name;
        this.adsLoadedCount = adsLoadedCount;
        this.adsClickedCount = adsClickedCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdsLoadedCount() {
        return adsLoadedCount;
    }

    public void setAdsLoadedCount(int adsLoadedCount) {
        this.adsLoadedCount = adsLoadedCount;
    }

    public int getAdsClickedCount() {
        return adsClickedCount;
    }

    public void setAdsClickedCount(int adsClickedCount) {
        this.adsClickedCount = adsClickedCount;
    }
}