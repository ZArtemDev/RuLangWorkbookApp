package com.ZArtemDev.RuLangWorkbookApp;

public class User {
    private String name;
    private String password;
    private short type;

    public User(String name, String email, short Type) {
        this.name = name;
        this.password = email;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String password) {
        this.password = password;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
