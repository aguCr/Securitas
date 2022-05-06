package de.hdmstuttgart.securitas.viewmodels;


import androidx.lifecycle.ViewModel;

import de.hdmstuttgart.securitas.util.Generator;
import de.hdmstuttgart.securitas.util.KeyCode;

public class GeneratorViewModel extends ViewModel {


    private final Generator generator = new Generator();
    private String password = "";
    private int qualityCode;
    private Boolean isEnabled = true;


    public void makePassword() {
        password = generator.generatePassword();
    }

    public String getPassword() {
        return password;
    }


    public int getQualityCode() {
        checkPasswordQuality(password);
        return qualityCode;
    }

    public String getQualityString() {
        String quality = "Qualität: ";

        switch (getQualityCode()) {
            case KeyCode.PW_QUALITY_BAD:
                quality += "schlecht";
                break;
            case KeyCode.PW_QUALITY_OK:
                quality += "ok";
                break;
            case KeyCode.PW_QUALITY_STRONG:
                quality += "stark";
                break;
            default://nothing
        }
        return quality;
    }

    public void checkPasswordQuality(String password) {
        qualityCode = generator.checkPasswordQuality(password);
    }


    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }


    public void setPwLength(int length) {
        generator.setPwLength(length);
    }

    public void setLower(Boolean lower) {
        generator.setHasLower(lower);
    }

    public void setUpper(Boolean upper) {
        generator.setHasUpper(upper);
    }

    public void setNumber(Boolean number) {
        generator.setHasNumber(number);
    }

    public void setSymbol(Boolean symbol) {
        generator.setHasSymbol(symbol);
    }


    public void setBeginLower(Boolean beginLower) {
        generator.setBeginLower(beginLower);
    }

    public void setBeginUpper(Boolean beginUpper) {
        generator.setBeginUpper(beginUpper);
    }

    public void setBeginNumber(Boolean beginNumber) {
        generator.setBeginNumber(beginNumber);
    }

    public void setBeginSymbol(Boolean beginSymbol) {
        generator.setBeginSymbol(beginSymbol);
    }
}