package com.hermanowicz.pantry.model;

public class ProductFeatures {

    private int isVege = -1;
    private int isBio = -1;
    private int hasSugar = -1;
    private int hasSalt = -1;

    public int getIsVege() {
        return isVege;
    }

    public void setIsVege(int productIsVege) {
        isVege = productIsVege;
    }

    public int getIsBio() {
        return isBio;
    }

    public void setIsBio(int productIsBio) {
        isBio = productIsBio;
    }

    public int getHasSugar() {
        return hasSugar;
    }

    public void setHasSugar(int productHasSugar) {
        hasSugar = productHasSugar;
    }

    public int getHasSalt() {
        return hasSalt;
    }

    public void setHasSalt(int productHasSalt) {
        hasSalt = productHasSalt;
    }
}