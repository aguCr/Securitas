package de.hdmstuttgart.securitas.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import java.util.concurrent.Executor;

import de.hdmstuttgart.securitas.util.KeyCode;
import de.hdmstuttgart.securitas.PwListActivity;
import de.hdmstuttgart.securitas.R;


public class AuthenticationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authentication, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        Button authBtn = requireView().findViewById(R.id.auth_btn);

        String authTitle = getResources().getString(R.string.auth_title);
        String authSubTitle = getResources().getString(R.string.auth_subtitle);
        String authDescription = getResources().getString(R.string.auth_description);

        //fingerprint prompt for authentication
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(authTitle)
                .setSubtitle(authSubTitle)  //optional
                .setDescription(authDescription)
                //.setNegativeButtonText("Cancel") // instead we provide a possibility to authenticate with PIN (some phones dont have a fingerprint sensor)
                .setAllowedAuthenticators(
                        BiometricManager.Authenticators.BIOMETRIC_STRONG     //fingerprint/face-recognition
                                | BiometricManager.Authenticators.DEVICE_CREDENTIAL)  //PIN/Pattern/Password
                .build();

        //show prompt immediately
        getPrompt().authenticate(promptInfo);

        //shows prompt when you press the btn(needed if you leave the prompt)
        authBtn.setOnClickListener(v -> getPrompt().authenticate(promptInfo));


        //to inform about errors of biometric authentication
        BiometricManager biometricManager = BiometricManager.from(requireActivity());
        switch (biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {

            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(KeyCode.LOG_TAG, "BIOMETRIC_SUCCESS");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e(KeyCode.LOG_TAG, "BIOMETRIC_ERROR_NO_HARDWARE");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(KeyCode.LOG_TAG, "BIOMETRIC_ERROR_HW_UNAVAILABLE");
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                Log.e(KeyCode.LOG_TAG, "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED");
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Log.e(KeyCode.LOG_TAG, "BIOMETRIC_ERROR_UNSUPPORTED");
                break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                Log.e(KeyCode.LOG_TAG, "onViewCreated: BIOMETRIC_STATUS_UNKNOWN");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                //opens OS settings to create a fingerprint
                //startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS)); //to be compatible lower Api if needed
                startActivity(new Intent(Settings.ACTION_BIOMETRIC_ENROLL)); //requires API R
                Toast.makeText(getContext(), "Bitte PIN + Fingerabdruck erstellen", Toast.LENGTH_LONG).show();
                Log.e("sos", "No Biometric features enrolled");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + biometricManager.canAuthenticate(
                        BiometricManager.Authenticators.BIOMETRIC_STRONG
                                | BiometricManager.Authenticators.DEVICE_CREDENTIAL));
        }
    }


    //handles authentication
    private BiometricPrompt getPrompt() {

        Executor executor = ContextCompat.getMainExecutor(requireActivity());

        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                notifyUser("Authentifizierungsfehler: " + errString.toString());
                Log.e(KeyCode.LOG_TAG, "Authentifizierungsfehler: " + errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                notifyUser("Authentifizierung erfolgreich!");
                Log.e(KeyCode.LOG_TAG, "Authentifizierung erfolgreich!");
                startActivity(new Intent(getContext(), PwListActivity.class));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                notifyUser("Authentifizierung Fehlgeschlagen");
                Log.e(KeyCode.LOG_TAG, "Authentifizierung Fehlgeschlagen");
            }
        };

        return new BiometricPrompt(requireActivity(), executor, callback);
    }

    //to simplify toasts
    private void notifyUser(String message) {
        Toast.makeText(requireActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
