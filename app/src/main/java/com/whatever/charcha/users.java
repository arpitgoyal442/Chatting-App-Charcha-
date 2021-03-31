package com.whatever.charcha;

public class users {

    private String name;
    private String img_url;
    private String uid;


    public String getName() {
        return name;
    }

    public String getImgurl() {
        return img_url;
    }

    public String getUid() {
        return uid;
    }

    public users()
    {

    }

    public  void users(String n,String i,String u)
    {
        name=n;
        img_url=i;
        uid=u;
    }
}
