package com.example.user.navigationdrawersample.Auth;

public class ApiServices {
    private static String HOST = "http://192.168.18.23:8000";
    private static String API = HOST + "/api/";
    private static String API_LOGIN = HOST+"/api/login";

    public static String getApiLogin() {
        return API_LOGIN;
    }
}
