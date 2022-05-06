package de.hdmstuttgart.securitas.util;

public final class KeyCode {

    // tag for logging
    public static final String LOG_TAG = "sos";

    //request code to decide whether pw is added or edited
    public static final int ADD_PW_REQUEST = 1;
    public static final int UPDATE_PW_REQUEST = 2;

    //code to set the color that indicates the quality
    public static final int PW_QUALITY_BAD = 3;
    public static final int PW_QUALITY_OK = 4;
    public static final int PW_QUALITY_STRONG = 5;

    //code to handle long/short click on item
    public static final int SHORT_CLICK = 6;
    public static final int LONG_CLICK = 7;

    //package name to ensure String is unique
    //to put Extra Info into Intent
    public static final String EXTRA_UID = "de.hdmstuttgart.securitas.EXTRA_UID";
    public static final String EXTRA_CATEGORY = "de.hdmstuttgart.securitas.EXTRA_CATEGORY";
    public static final String EXTRA_TITLE = "de.hdmstuttgart.securitas.EXTRA_TITLE";
    public static final String EXTRA_USER_EMAIL = "de.hdmstuttgart.securitas.EXTRA_USER_EMAIL";
    public static final String EXTRA_PASSWORD = "de.hdmstuttgart.securitas.EXTRA_PASSWORD";
    public static final String EXTRA_NOTE = "de.hdmstuttgart.securitas.EXTRA_NOTE";
    public static final String EXTRA_PW_QUALITY = "de.hdmstuttgart.securitas.EXTRA_PW_QUALITY";
}
