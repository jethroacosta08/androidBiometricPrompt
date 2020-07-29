package com.jethro.biometricmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jethro.biometricmodule.biometric.biometricController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        biometricController biometric = new biometricController(this, MainActivity.this);
        if(biometric.isBiometricEnabled())
        {
            biometric.setBIOMETRIC_TITLE("Fingerprint Authentication");
            biometric.setBIOMETRIC_SUB_TITLE("jethroacosta08@gmail.com");
            biometric.setBIOMETRIC_DESCRIPTION("Touch fingerprint sensor");
            biometric.setBIOMETRIC_NEGATIVE_BUTTON_TEXT("Cancel");
            biometric.setBiometricConfirmationRequired(false);
            biometric.showBiometricPrompt();
        }
    }
}
