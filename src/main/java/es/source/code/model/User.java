package es.source.code.model;

import java.io.Serializable;

/**
 * Created by zhangyajie on 2017/10/5.
 */

public class User implements Serializable {
    public String userName;
    public String getterUserName() {//get方法
        return userName;
    }
    public void setterUserName(String name) {//set方法
        this.userName = name;
    }

    public String passWord;
    public String getterPassWord() {//get方法
        return passWord;
    }
    public void setterPassWord(String password) {//set方法
        this.passWord = password;
    }

    public Boolean oldUser;
    public boolean getterOldUser() {//get方法
        return oldUser;
    }
    public void setterOldUser(boolean olduser) {//set方法
        this.oldUser = olduser;
    }

    /*public User(String userName,String password,Boolean oldUser) {
        this.userName = userName;
        this.passWord = password;
        // 注：boolean 值的序列化方法
        this.oldUser = oldUser;
    }*/

    /*public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        *//**
         * 从Parcel中读取数据
         *//*

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source.readString(),source.readString(),(source.readByte() != 0));
        }

        *//**
         * 供外部类反序列化本类数组使用
         *//*
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // 第一个方法是内容接口描述，默认返回0就可以了
    public int describeContents() {
        return 0;
    }

    // 将我们的对象序列化一个Parcel对象，也就是将我们的对象存入Parcel中
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(passWord);
        dest.writeByte((byte) (oldUser ?  1 : 0));//boolean的写法
    }
*/
}

