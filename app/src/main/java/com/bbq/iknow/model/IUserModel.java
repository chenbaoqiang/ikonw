package com.bbq.iknow.model;

import com.bbq.iknow.bean.UserBean;

/**
 * Created by chenbaoqiang on 2017/6/5.
 */
public interface  IUserModel {
    void setID(int id);

    void setFirstName(String firstName);

    void setLastName(String lastName);

    UserBean load(int id);
}
