package com.tyz.search;

import com.tyz.cn.CN;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.reactivestreams.Publisher;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by com on 2017/9/11.
 */
@Entity
public class Contact implements CN {
    @Id
    public long id;

    public  String name;

    public  int imgUrl;

    public Contact(String name, int imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    @Generated(hash = 2019261515)
    public Contact(long id, String name, int imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }

    @Override
    public String chinese() {
        return name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }
}
