package com.bbq.iknow.presenter;

import com.bbq.iknow.bean.UserBean;
import com.bbq.iknow.model.IUserModel;
import com.bbq.iknow.model.UserModel;
import com.bbq.iknow.ui.IUserView;

/**
 * Created by chenbaoqiang on 2017/6/5.
 */
public class UserPresenter {
    private IUserView mUserView;
    private IUserModel mUserModel;

    public UserPresenter(IUserView view) {
        mUserView = view;
        mUserModel = new UserModel();
    }

    public void saveUser(int id, String firstName, String lastName) {
        mUserModel.setID(id);
        mUserModel.setFirstName(firstName);
        mUserModel.setLastName(lastName);
    }

    public void loadUser(int id) {
        UserBean user = mUserModel.load(id);
        mUserView.setFirstName(user.getFirstName());
        mUserView.setLastName(user.getLastName());
    }
}
