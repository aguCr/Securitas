package de.hdmstuttgart.securitas.util;


import android.util.Log;

public class Generator {

    // properties
    private int pwLength = 8;
    private boolean hasLower = true;
    private boolean hasUpper = true;
    private boolean hasNumber = true;
    private boolean hasSymbol = true;


    private boolean beginLower = false;
    private boolean beginUpper = false;
    private boolean beginNumber = false;
    private boolean beginSymbol = false;

    //Alphabet (property sets)
    private final String lowerCases = "abcdefghijklmnopqrstuvwxyz";
    private final String upperCases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String numbers = "1234567890";
    private final String symbols = "!§$%&?@.-_~+*€";


    private StringBuilder password;


    //setter
    public void setHasLower(boolean hasLower) {
        this.hasLower = hasLower;
    }

    public void setHasUpper(boolean hasUpper) {
        this.hasUpper = hasUpper;
    }

    public void setHasNumber(boolean hasNumber) {
        this.hasNumber = hasNumber;
    }

    public void setHasSymbol(boolean hasSymbol) {
        this.hasSymbol = hasSymbol;
    }

    public void setBeginLower(boolean beginLower) {
        this.beginLower = beginLower;
    }

    public void setBeginUpper(boolean beginUpper) {
        this.beginUpper = beginUpper;
    }

    public void setBeginNumber(boolean beginNumber) {
        this.beginNumber = beginNumber;
    }

    public void setBeginSymbol(boolean beginSymbol) {
        this.beginSymbol = beginSymbol;
    }

    public void setPwLength(int pwLength) {
        this.pwLength = pwLength;
    }


    public String generatePassword() {

        password = new StringBuilder();

        // no alphabet
        if (!hasLower && !hasNumber && !hasSymbol && !hasUpper) {
            Log.e(KeyCode.LOG_TAG, "no chars selected");
            return null;
        }

        //generates pw until it contains all selected properties (when pw is to short the random generation does not get all properties)
        while (generateAgain()) {

            for (int i = 0; i < pwLength; i++) {

                //random nr between 1-4 (for properties)
                int randomNr = (int) (4 * Math.random() + 1);

                switch (randomNr) {
                    case 1: {
                        //uses appendRandomChar-method down below
                        if (hasLower) appendRandomChar(lowerCases);
                            // restore iteration to keep the length
                        else i--;
                        break;
                    }
                    case 2: {
                        if (hasUpper) appendRandomChar(upperCases);
                        else i--;
                        break;
                    }
                    case 3: {
                        if (hasNumber) appendRandomChar(numbers);
                        else i--;
                        break;
                    }
                    case 4: {
                        if (hasSymbol) appendRandomChar(symbols);
                        else i--;
                        break;
                    }
                }
            }

            setFirstCharPw();
            generateAgain();
        }
        return password.toString();
    }


    //tells to generate pw again until it contains all selected properties
    // (when pw is to short the random generation does not get all properties)
    //uses hasChar-method down below
    private boolean generateAgain() {
        if (!hasChar(password.toString(), lowerCases) && hasLower
                || !hasChar(password.toString(), upperCases) && hasUpper
                || !hasChar(password.toString(), numbers) && hasNumber
                || !hasChar(password.toString(), symbols) && hasSymbol
        ) {
            password.setLength(0);
            return true;
        }
        return false;
    }


    //true if (pass)word contains a char of a property set
    private boolean hasChar(String word, String propertySet) {
        for (int i = 0; i < word.length(); i++) {
            if (propertySet.contains(String.valueOf(word.charAt(i)))) {
                return true;
            }
        }
        return false;
    }


    //uses insertFirstChar-method down below
    private void setFirstCharPw() {
        if (beginLower) insertFirstChar(lowerCases);
        if (beginUpper) insertFirstChar(upperCases);
        if (beginNumber) insertFirstChar(numbers);
        if (beginSymbol) insertFirstChar(symbols);
    }


    private void insertFirstChar(String propertySet) {
        int randomIndex = (int) (propertySet.length() * Math.random());
        password.insert(0, propertySet.charAt(randomIndex));
        //deletes last digit to keep the length
        password.deleteCharAt(password.length() - 1);
    }


    private void appendRandomChar(String propertySet) {
        int randomIndex = (int) (propertySet.length() * Math.random());
        password.append(propertySet.charAt(randomIndex));
    }


    //returns Quality
    public int checkPasswordQuality(String password) {
        int counter = 0;
        int quality = 0;


        if (password.length() >= 8) counter++;
        if (hasChar(password, lowerCases)) counter++;
        if (hasChar(password, upperCases)) counter++;
        if (hasChar(password, numbers)) counter++;
        if (hasChar(password, symbols)) counter++;


        switch (counter) {
            case 0:
                Log.e("sos", "ups, something went wrong, no chars");
                break;
            case 1:
            case 2:
                quality = KeyCode.PW_QUALITY_BAD;
                break;
            case 3:
            case 4:
                quality = KeyCode.PW_QUALITY_OK;
                break;
            case 5:
                quality = KeyCode.PW_QUALITY_STRONG;
                break;
        }
        return quality;
    }
}