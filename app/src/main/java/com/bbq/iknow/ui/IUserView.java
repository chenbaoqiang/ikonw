package com.bbq.iknow.ui;

/**
 * Created by chenbaoqiang on 2017/6/5.
 */
public interface  IUserView {
    int getID();

    String getFristName();

    String getLastName();

    void setFirstName(String firstName);

    void setLastName(String lastName);
}
