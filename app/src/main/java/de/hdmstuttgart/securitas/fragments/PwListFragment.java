package de.hdmstuttgart.securitas.fragments;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import de.hdmstuttgart.securitas.PwCreateActivity;
import de.hdmstuttgart.securitas.R;
import de.hdmstuttgart.securitas.adapters.PasswordDataAdapter;
import de.hdmstuttgart.securitas.data.PasswordData;
import de.hdmstuttgart.securitas.util.Generator;
import de.hdmstuttgart.securitas.util.KeyCode;
import de.hdmstuttgart.securitas.viewmodels.PasswordDataViewModel;

public class PwListFragment extends Fragment {

    private PasswordDataViewModel pwDataViewModel;

    //Launcher create activity according to the request code (edit/add)
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> requestHandler(result.getResultCode(), result.getData()));

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pw_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        final PasswordDataAdapter adapter = new PasswordDataAdapter();
        RecyclerView recyclerView = requireView().findViewById(R.id.main_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        //works on swipe to the left
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT /*| ItemTouchHelper.RIGHT*/) {
            @Override
            //drag an drop
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //nothing implemented
                return false;
            }

            //to delete ListItem by swiping to the left + undo possibility
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final PasswordData deletedPw = adapter.getPwDataAt(viewHolder.getAdapterPosition());
                pwDataViewModel.delete(adapter.getPwDataAt(viewHolder.getAdapterPosition()));

                String title = deletedPw.getTitle();
                Snackbar.make(recyclerView, title + " gelöscht", Snackbar.LENGTH_LONG)
                        .setAction("Rückgängig", view -> pwDataViewModel.insert(deletedPw))
                        .show();
            }
        })
        .attachToRecyclerView(recyclerView);


        //when list changes, the recyclerview updates its view
        pwDataViewModel = new ViewModelProvider(this).get(PasswordDataViewModel.class);
        pwDataViewModel.getAllPasswordData().observe(getViewLifecycleOwner(), adapter::submitList);


        FloatingActionButton fabAddNote = requireView().findViewById(R.id.add_password_FAB);
        fabAddNote.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), PwCreateActivity.class);
            resultLauncher.launch(intent);
        });


        adapter.setOnItemClickListener(
            new PasswordDataAdapter.OnItemClickListener() {
                //simple click
                @Override
                public void onItemClick(PasswordData passwordData) {

                    //create intent with saved data
                    Intent intent = new Intent(getActivity(), PwCreateActivity.class);

                    intent.putExtra(KeyCode.EXTRA_CATEGORY, passwordData.getCategory());
                    intent.putExtra(KeyCode.EXTRA_TITLE, passwordData.getTitle());
                    intent.putExtra(KeyCode.EXTRA_USER_EMAIL, passwordData.getUserEMail());
                    intent.putExtra(KeyCode.EXTRA_PASSWORD, passwordData.getPassword());
                    intent.putExtra(KeyCode.EXTRA_NOTE, passwordData.getNote());
                    intent.putExtra(KeyCode.EXTRA_UID, passwordData.getUid());
                    intent.putExtra(KeyCode.EXTRA_PW_QUALITY, passwordData.getQuality());

                    resultLauncher.launch(intent);
                }

                //long click
                @Override
                public void onItemLongClick(int position) {
                    final PasswordData longPressedPw = adapter.getPwDataAt(position);
                    String password = longPressedPw.getPassword();
                    copyPwToClipboard(password);
                }
            }
        );
    }


    //makes PasswordData with help of the KeyCode for the intent
    private PasswordData makePasswordData(Intent intent) {

        Generator generator = new Generator();

        String category = intent.getStringExtra(KeyCode.EXTRA_CATEGORY);
        String title = intent.getStringExtra(KeyCode.EXTRA_TITLE);
        String userEmail = intent.getStringExtra(KeyCode.EXTRA_USER_EMAIL);
        String password = intent.getStringExtra(KeyCode.EXTRA_PASSWORD);
        String note = intent.getStringExtra(KeyCode.EXTRA_NOTE);

        //check made after the password is passed
        int pwStrength = generator.checkPasswordQuality(password);

        return new PasswordData(category, title, userEmail, password, note, pwStrength);
    }


    //handles request codes
    private void requestHandler(int resultCode, @Nullable Intent intent) {

        PasswordData passwordData;

        switch (resultCode) {
            case KeyCode.ADD_PW_REQUEST:
                assert intent != null;
                passwordData = makePasswordData(intent);
                pwDataViewModel.insert(passwordData);
                notifyUser("Passwort gespeichert");
                Log.e(KeyCode.LOG_TAG, "Passwort gespeichert");
                break;

            case KeyCode.UPDATE_PW_REQUEST:
                assert intent != null;
                int id = intent.getIntExtra(KeyCode.EXTRA_UID, -1);
                //error handling(if uid does not exist)
                if (id == -1) {
                    notifyUser("Passwort kann nicht gespeichert werden");
                    Log.e(KeyCode.LOG_TAG, "Passwort kann nicht gespeichert werden");
                    return;
                }

                passwordData = makePasswordData(intent);
                passwordData.setUid(id);
                pwDataViewModel.update(passwordData);
                notifyUser("Passwort aktualisiert");
                Log.e(KeyCode.LOG_TAG, "Passwort aktualisiert");
                break;

            default:
                notifyUser("Vorgang abgebrochen");
                Log.e(KeyCode.LOG_TAG, "Vorgang abgebrochen");
                break;
        }
    }

    //to simplify Toasts
    private void notifyUser(String message) {
        Toast.makeText(requireActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void copyPwToClipboard(String password) {
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Passwort", password);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), password + " wurde in die Zwischenablage kopiert", Toast.LENGTH_SHORT).show();
    }
}