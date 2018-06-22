package com.example.just.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/22.
 */

public class Person implements Serializable {
    public Person(boolean isExit, String name, String email) {
        this.isExit = isExit;
        this.name = name;
        this.email = email;
    }

    boolean isExit;

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    String name;
    String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
