package com.jethro.biometricmodule.biometric;

import android.annotation.TargetApi;
import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class biometricController {
    Context appContext;
    FragmentActivity fragmentActivity;
    String biometricMessage;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private String BIOMETRIC_TITLE = "Biometric login for my app";
    private String BIOMETRIC_SUB_TITLE = "";
    private String BIOMETRIC_DESCRIPTION = "";
    private String BIOMETRIC_NEGATIVE_BUTTON_TEXT = "Use account password";

    private Boolean isBiometricConfirmationRequired = false;

    public biometricController(Context context, FragmentActivity fragmentActivity)
    {
        this.appContext = context;
        this.fragmentActivity = fragmentActivity;
    }

    public Boolean isBiometricEnabled()
    {
        Boolean biometricStatus = false;
        BiometricManager biometricManager = BiometricManager.from(this.appContext);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                this.biometricMessage = "App can authenticate using biometrics.";
                biometricStatus = true;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                this.biometricMessage = "No biometric features available on this device.";
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                this.biometricMessage = "Biometric features are currently unavailable.";
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                this.biometricMessage = "The user hasn't associated any biometric credentials with their account..";
                break;
        }
        Log.d("BIOMETRIC_STATUS", this.biometricMessage);
        return biometricStatus;
    }

    public String getBiometricMessage() {
        return biometricMessage;
    }

    public void setBiometricMessage(String biometricMessage) {
        this.biometricMessage = biometricMessage;
    }

    public String getBIOMETRIC_TITLE() {
        return BIOMETRIC_TITLE;
    }

    public void setBIOMETRIC_TITLE(String BIOMETRIC_TITLE) {
        this.BIOMETRIC_TITLE = BIOMETRIC_TITLE;
    }

    public String getBIOMETRIC_SUB_TITLE() {
        return BIOMETRIC_SUB_TITLE;
    }

    public void setBIOMETRIC_SUB_TITLE(String BIOMETRIC_SUB_TITLE) {
        this.BIOMETRIC_SUB_TITLE = BIOMETRIC_SUB_TITLE;
    }

    public String getBIOMETRIC_NEGATIVE_BUTTON_TEXT() {
        return BIOMETRIC_NEGATIVE_BUTTON_TEXT;
    }

    public void setBIOMETRIC_NEGATIVE_BUTTON_TEXT(String BIOMETRIC_NEGATIVE_BUTTON_TEXT) {
        this.BIOMETRIC_NEGATIVE_BUTTON_TEXT = BIOMETRIC_NEGATIVE_BUTTON_TEXT;
    }

    public Boolean getBiometricConfirmationRequired() {
        return isBiometricConfirmationRequired;
    }

    public void setBiometricConfirmationRequired(Boolean biometricConfirmationRequired) {
        isBiometricConfirmationRequired = biometricConfirmationRequired;
    }

    public String getBIOMETRIC_DESCRIPTION() {
        return BIOMETRIC_DESCRIPTION;
    }

    public void setBIOMETRIC_DESCRIPTION(String BIOMETRIC_DESCRIPTION) {
        this.BIOMETRIC_DESCRIPTION = BIOMETRIC_DESCRIPTION;
    }

    @TargetApi(23)
    public void showBiometricPrompt()
    {
        executor = ContextCompat.getMainExecutor(this.appContext);
        biometricPrompt = new BiometricPrompt(this.fragmentActivity,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(appContext,
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(appContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(appContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(BIOMETRIC_TITLE)
                .setSubtitle(BIOMETRIC_SUB_TITLE)
                .setDescription(BIOMETRIC_DESCRIPTION)
                .setNegativeButtonText(BIOMETRIC_NEGATIVE_BUTTON_TEXT)
                .setConfirmationRequired(isBiometricConfirmationRequired)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
