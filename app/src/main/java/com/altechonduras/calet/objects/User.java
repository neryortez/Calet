package com.altechonduras.calet.objects;

import android.support.annotation.Keep;

/**
 * Created by Nery Ortez on 28-Sep-17.
 */
@Keep
public class User {
    private String name;
    private String uid;
    private String otro;
    private String email;
    private boolean logged;

    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOtro() {
        return otro;
    }

    public void setOtro(String otro) {
        this.otro = otro;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
