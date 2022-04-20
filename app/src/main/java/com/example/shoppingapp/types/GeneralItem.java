package com.example.shoppingapp.types;

import com.example.shoppingapp.models.CheckoutModel;

class GeneralItem extends  ListItem{

    private CheckoutModel pojoOfJsonArray;

    CheckoutModel getPojoOfJsonArray() {
        return pojoOfJsonArray;
    }

    void setPojoOfJsonArray(CheckoutModel pojoOfJsonArray) {
        this.pojoOfJsonArray = pojoOfJsonArray;
    }

    @Override
    int getType() {
        return ListItem.TYPE_GENERAL;
    }
}
