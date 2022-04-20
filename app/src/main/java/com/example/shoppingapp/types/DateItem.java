package com.example.shoppingapp.types;

public class DateItem extends ListItem{
    private String date;

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    int getType() {
        return ListItem.TYPE_DATE;
    }
}
