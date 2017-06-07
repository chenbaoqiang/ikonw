package com.bbq.iknow.model;

import android.util.SparseArray;

import com.bbq.iknow.bean.UserBean;

/**
 * Created by chenbaoqiang on 2017/6/5.
 */
public class UserModel implements IUserModel {

    private String mFristName;
    private String mLastName;
    private int mID;
    private SparseArray<UserBean> mUsererArray = new SparseArray<UserBean>();

    @Override
    public void setID(int id) {
        // TODO Auto-generated method stub
        mID = id;
    }

    @Override
    public void setFirstName(String firstName) {
        // TODO Auto-generated method stub
        mFristName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        // TODO Auto-generated method stub
        mLastName = lastName;
        UserBean UserBean = new UserBean(mFristName, mLastName);
        mUsererArray.append(mID, UserBean);
    }

    @Override
    public UserBean load(int id) {
        // TODO Auto-generated method stub
        mID = id;
        UserBean userBean = mUsererArray.get(mID, new UserBean("not found",
                "not found"));
        return userBean;

    }
}
