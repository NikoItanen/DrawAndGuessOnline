package com.nijoat.frontend.util;

import com.nijoat.frontend.model.User;

public class UserSession {
    private static String username;
    private static boolean isDrawer;

    public static String getUsername() {
        return username;
    }

    public static void setUsername (String username) {
        UserSession.username = username;
    }

    public static void setIsDrawer(boolean value) {
        UserSession.isDrawer = value;
    }

    public static boolean getIsDrawer() {
        return isDrawer;
    }
}
