package com.cs.ssm.utils;

import org.springframework.beans.propertyeditors.PropertiesEditor;

import java.util.Date;

public class DateStringEditor extends PropertiesEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            Date date = DateUtils.string2Date(text, "yyyy-MM-dd HH:MM");
            super.setValue(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
