package de.hdmstuttgart.securitas.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.hdmstuttgart.securitas.util.Generator;
import de.hdmstuttgart.securitas.R;
import de.hdmstuttgart.securitas.util.KeyCode;
import de.hdmstuttgart.securitas.viewmodels.PwSharedViewModel;

public class PwFormFragment extends Fragment {

    private boolean updatePassword = false;

    private TextInputEditText titleInput, userEmailInput, passwordInput, noteInput;
    private AutoCompleteTextView categoryInput;

    private Generator pwGenerator;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pw_form, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pwGenerator = new Generator();
        PwSharedViewModel pwSharedViewModel = new ViewModelProvider(requireActivity()).get(PwSharedViewModel.class);


        categoryInput = requireView().findViewById(R.id.category_input);
        titleInput = requireView().findViewById(R.id.title_input);
        userEmailInput = requireView().findViewById(R.id.email_input);
        passwordInput = requireView().findViewById(R.id.password_input);
        noteInput = requireView().findViewById(R.id.note_input);

        Button saveBtn = requireView().findViewById(R.id.save_Btn);
        Button abortBtn = requireView().findViewById(R.id.abort_Btn);


        //to show the categories as a list, location: /values/strings/categories
        String[] categoryItems = getResources().getStringArray(R.array.categories);
        displayCategories(categoryItems);


        //returns intent that starts this activity
        Intent intent = requireActivity().getIntent();


        //if intent has uid, it means a password exists, so the fragment should be filled with data
        if (intent.hasExtra(KeyCode.EXTRA_UID)) {
            //set correct title
            requireActivity().setTitle(getResources().getString(R.string.edit_title));

            titleInput.setText(intent.getStringExtra(KeyCode.EXTRA_TITLE));
            userEmailInput.setText(intent.getStringExtra(KeyCode.EXTRA_USER_EMAIL));
            passwordInput.setText(intent.getStringExtra(KeyCode.EXTRA_PASSWORD));
            noteInput.setText(intent.getStringExtra(KeyCode.EXTRA_NOTE));
            categoryInput.setText(intent.getStringExtra(KeyCode.EXTRA_CATEGORY));

            //call again to display the categories to be able to change it again
            displayCategories(categoryItems);

            //for later to pass the correct request-code
            updatePassword = true;
        }
        //no uid -> new password, fragment is empty
        else requireActivity().setTitle(getResources().getString(R.string.add_title));


        //pw is being observed, so if a pw is generated in the other fragment, it will be set to the textview in this fragment
        pwSharedViewModel.getPassword().observe(getViewLifecycleOwner(), pw -> passwordInput.setText(pw));

        saveBtn.setOnClickListener(v -> savePassword());

        abortBtn.setOnClickListener(v -> requireActivity().finish());
    }


    private void displayCategories(String[] categoryItems) {
        ArrayAdapter<String> adaptedItems = new ArrayAdapter<>(getContext(), R.layout.list_item_category, categoryItems);
        categoryInput.setAdapter(adaptedItems);
    }


    private void savePassword() {

        //extract values from views
        String category = categoryInput.getText().toString();
        String title = Objects.requireNonNull(titleInput.getText()).toString();
        String userEmail = Objects.requireNonNull(userEmailInput.getText()).toString();
        String password = Objects.requireNonNull(passwordInput.getText()).toString();
        String note = Objects.requireNonNull(noteInput.getText()).toString();


        //check if required fields are filled, otherwise pw will not be saved
        if (title.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(getContext(), "Bitte Titel und Passwort eingeben", Toast.LENGTH_SHORT).show();
            return;
        }

        //put extras to intent
        Intent intent = new Intent();
        intent.putExtra(KeyCode.EXTRA_CATEGORY, category);
        intent.putExtra(KeyCode.EXTRA_TITLE, title);
        intent.putExtra(KeyCode.EXTRA_USER_EMAIL, userEmail);
        intent.putExtra(KeyCode.EXTRA_PASSWORD, password);
        intent.putExtra(KeyCode.EXTRA_NOTE, note);

        int pwQualityCode = pwGenerator.checkPasswordQuality(password);
        intent.putExtra(KeyCode.EXTRA_PW_QUALITY, pwQualityCode);


        //if id does not exist, default will be -1 to make an error,to set a new id
        int id = requireActivity().getIntent().getIntExtra(KeyCode.EXTRA_UID, -1);
        if (id != -1) intent.putExtra(KeyCode.EXTRA_UID, id);


        //set correct result for intent
        if (updatePassword) requireActivity().setResult(KeyCode.UPDATE_PW_REQUEST, intent);
        else requireActivity().setResult(KeyCode.ADD_PW_REQUEST, intent);


        requireActivity().finish();
    }
}

