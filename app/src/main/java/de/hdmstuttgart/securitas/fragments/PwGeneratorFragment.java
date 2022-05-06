package de.hdmstuttgart.securitas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.securitas.util.QualityInformationDialog;
import de.hdmstuttgart.securitas.R;
import de.hdmstuttgart.securitas.viewmodels.GeneratorViewModel;
import de.hdmstuttgart.securitas.viewmodels.PwSharedViewModel;


public class PwGeneratorFragment extends Fragment {


    private TextView pwLengthTv, pwQualityTv, resultPwTv;
    private CheckBox lowerCb, upperCb, numberCb, symbolCb;
    private Button generateBtn;
    private RadioGroup radioGroup;
    private ImageView infoBtnIv;
    private GeneratorViewModel generatorViewModel;
    private PwSharedViewModel pwSharedViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pw_generator, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        generatorViewModel = new ViewModelProvider(this).get(GeneratorViewModel.class);
        pwSharedViewModel = new ViewModelProvider(requireActivity()).get(PwSharedViewModel.class);


        SeekBar seekBarPwLength = requireView().findViewById(R.id.seek_bar_pw_length);
        pwLengthTv = requireView().findViewById(R.id.pw_length_tv);
        pwQualityTv = requireView().findViewById(R.id.pw_quality_tv);
        resultPwTv = requireView().findViewById(R.id.result_pw_tv);
        generateBtn = requireView().findViewById(R.id.generate_btn);
        lowerCb = requireView().findViewById(R.id.lower_cb1);
        upperCb = requireView().findViewById(R.id.upper_cb2);
        numberCb = requireView().findViewById(R.id.number_cb3);
        symbolCb = requireView().findViewById(R.id.symbol_cb4);
        radioGroup = requireView().findViewById(R.id.radio_group);
        infoBtnIv = requireView().findViewById(R.id.information_Ib);


         //keeps the btn disabled on config change( dark mode/rotation)
        if (!generatorViewModel.getIsEnabled()) {
            generateBtn.setEnabled(false);
        }

        //seekbar (Slider)
        seekBarPwLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                generatorViewModel.setPwLength(i);
                String pwLenStr = "Passwortlänge " + i;
                pwLengthTv.setText(pwLenStr);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //nothing
            }
        });


        //checkboxes, to set properties in the viewModel of the generator to true/false
        // they also handle the radioGroup and button enabling
        lowerCb.setOnClickListener(v -> {
            generatorViewModel.setLower(lowerCb.isChecked());
            handleBtnEnabling();
            handleRadioBtnEnabling();
        });

        upperCb.setOnClickListener(v -> {
            generatorViewModel.setUpper(upperCb.isChecked());
            handleBtnEnabling();
            handleRadioBtnEnabling();
        });

        numberCb.setOnClickListener(v -> {
            generatorViewModel.setNumber(numberCb.isChecked());
            handleBtnEnabling();
            handleRadioBtnEnabling();
        });

        symbolCb.setOnClickListener(v -> {
            generatorViewModel.setSymbol(symbolCb.isChecked());
            handleBtnEnabling();
            handleRadioBtnEnabling();
        });


        //fill pw + quality textview with viewModel data if pw already exists, important after config change
        if (!generatorViewModel.getPassword().isEmpty()) {
            resultPwTv.setText(generatorViewModel.getPassword());
            pwQualityTv.setText(generatorViewModel.getQualityString());
            infoBtnIv.setVisibility(View.VISIBLE);
        }


        //generates pw + fills the password-form from the other fragment with the password
        generateBtn.setOnClickListener(v -> {
            handleRadioGroup();

            generatorViewModel.makePassword();
            resultPwTv.setText(generatorViewModel.getPassword());
            pwQualityTv.setText(generatorViewModel.getQualityString());

            //to pass password directly to PwFormFragment
            pwSharedViewModel.setPassword(generatorViewModel.getPassword());

            infoBtnIv.setVisibility(View.VISIBLE);
        });


        //information imageview that explains the password quality
        infoBtnIv.setOnClickListener(view1 -> openQualityDialog(generatorViewModel.getQualityCode()));
    }


    //disables generate btn if no checkbox is ticked
    private void handleBtnEnabling() {

        generatorViewModel.setIsEnabled(true);
        generateBtn.setEnabled(true);

        if (!lowerCb.isChecked()
                && !upperCb.isChecked()
                && !numberCb.isChecked()
                && !symbolCb.isChecked()) {

            generatorViewModel.setIsEnabled(false);
            generateBtn.setEnabled(false);
            Toast.makeText(getContext(), "Bitte mindestens eine Eigenschaft auswählen!", Toast.LENGTH_SHORT).show();
        }
    }


    //disables radioBtn if corresponding checkbox is not ticked
    private void handleRadioBtnEnabling() {

        RadioButton lowerRb = requireView().findViewById(R.id.lower_rb);
        RadioButton upperRb = requireView().findViewById(R.id.upper_rb);
        RadioButton numberRb = requireView().findViewById(R.id.number_rb);
        RadioButton symbolsRb = requireView().findViewById(R.id.symbols_rb);


        if (lowerCb.isChecked()) {
            lowerRb.setEnabled(true);
        } else {
            lowerRb.setEnabled(false);
            lowerRb.setChecked(false);
            generatorViewModel.setBeginLower(false);
        }

        if (upperCb.isChecked()) {
            upperRb.setEnabled(true);
        } else {
            upperRb.setEnabled(false);
            upperRb.setChecked(false);
            generatorViewModel.setBeginUpper(false);
        }

        if (numberCb.isChecked()) {
            numberRb.setEnabled(true);
        } else {
            numberRb.setEnabled(false);
            numberRb.setChecked(false);
            generatorViewModel.setBeginNumber(false);
        }

        if (symbolCb.isChecked()) {
            symbolsRb.setEnabled(true);
        } else {
            symbolsRb.setEnabled(false);
            symbolsRb.setChecked(false);
            generatorViewModel.setBeginSymbol(false);
        }
    }


    //to set the first character, handle the radio group
    private void handleRadioGroup() {
        int radioBtnId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = requireView().findViewById(radioBtnId);

        if (lowerCb.isChecked()) {
            generatorViewModel.setBeginLower(radioButton.getText().equals("Kleinbuchstaben"));
        }
        if (upperCb.isChecked()) {
            generatorViewModel.setBeginUpper(radioButton.getText().equals("Großbuchstaben"));
        }
        if (numberCb.isChecked()) {
            generatorViewModel.setBeginNumber(radioButton.getText().equals("Zahlen"));
        }
        if (symbolCb.isChecked()) {
            generatorViewModel.setBeginSymbol(radioButton.getText().equals("Sonderzeichen"));
        }
    }


    //opens dialog of quality according to the quality code
    private void openQualityDialog(int qualityCode) {
        QualityInformationDialog qualityDialog = new QualityInformationDialog(qualityCode);
        qualityDialog.show(getParentFragmentManager(), "Quality information dialog");
    }
}