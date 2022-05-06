package de.hdmstuttgart.securitas.util;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import de.hdmstuttgart.securitas.R;

//to create an information dialog
public class QualityInformationDialog extends AppCompatDialogFragment {

    private final int qualityCode;

    public QualityInformationDialog(int qualityCode) {
        this.qualityCode = qualityCode;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String message = "";
        String title = "Information";

        String badQuality = getResources().getString(R.string.bad_quality_info);
        String okQuality = getResources().getString(R.string.ok_quality_info);
        String strongQuality = getResources().getString(R.string.strong_quality_info);


        switch (qualityCode) {
            case KeyCode.PW_QUALITY_BAD:
                message = badQuality;
                title = "Schwach!";
                break;

            case KeyCode.PW_QUALITY_OK:
                message = okQuality;
                title = "Ok!";
                break;

            case KeyCode.PW_QUALITY_STRONG:
                message = strongQuality;
                title = "Stark!";
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    //nothing
                });
        return builder.create();
    }

}
