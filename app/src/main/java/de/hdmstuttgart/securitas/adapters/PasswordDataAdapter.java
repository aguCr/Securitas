package de.hdmstuttgart.securitas.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.hdmstuttgart.securitas.R;
import de.hdmstuttgart.securitas.util.KeyCode;
import de.hdmstuttgart.securitas.data.PasswordData;



public class PasswordDataAdapter extends ListAdapter<PasswordData, PasswordDataAdapter.PasswordDataViewHolder> {

    private OnItemClickListener listener;


    public PasswordDataAdapter() {
        super(DIFF_CALLBACK);
    }


    //to handle same/different input into the list
    private static final DiffUtil.ItemCallback<PasswordData> DIFF_CALLBACK = new DiffUtil.ItemCallback<PasswordData>() {
        @Override
        public boolean areItemsTheSame(@NonNull PasswordData oldItem, @NonNull PasswordData newItem) {
            return oldItem.getUid() == newItem.getUid();
        }

        @Override
        //true when nothing changes (when you edit the PasswordData without a change)
        public boolean areContentsTheSame(@NonNull PasswordData oldItem, @NonNull PasswordData newItem) {

            return oldItem.getCategory().equals(newItem.getCategory())
                    && oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getUserEMail().equals(newItem.getUserEMail())
                    && oldItem.getPassword().equals(newItem.getPassword())
                    && oldItem.getNote().equals(newItem.getNote());
        }
    };


    @NonNull
    @Override
    public PasswordDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pw, parent, false);
        return new PasswordDataViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull PasswordDataViewHolder holder, int position) {

        PasswordData currentData = getItem(position);

        holder.titleTv.setText(currentData.getTitle());
        holder.passwordTv.setText(currentData.getPassword());
        holder.setQualityColor(currentData, holder.pwQualityIv.getContext());
    }


    //to access PasswordData on a position from outside
    public PasswordData getPwDataAt(int position) {
        return getItem(position);
    }


    class PasswordDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTv, passwordTv;
        private final ImageView pwQualityIv;


        public PasswordDataViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.item_title_tv);
            passwordTv = itemView.findViewById(R.id.item_password_tv);
            pwQualityIv = itemView.findViewById(R.id.pw_quality_circle_Iv);

            //to avoid that item with invalid position is clicked(e.g. when item is deleted but still in the animation)
            itemView.setOnClickListener(view -> handlePositionClick(KeyCode.SHORT_CLICK));

            itemView.setOnLongClickListener(view -> {
                handlePositionClick(KeyCode.LONG_CLICK);
                return true;
            });
        }


        private void handlePositionClick(int clickCode) {
            if (listener != null) {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    switch (clickCode) {
                        case KeyCode.LONG_CLICK:
                            listener.onItemLongClick(pos);
                            break;
                        case KeyCode.SHORT_CLICK:
                            listener.onItemClick(getItem(pos));
                            break;
                    }
                }
            }
        }


        //to set the color from resources according to the quality of the password
        private void setQualityColor(PasswordData currentData, Context context) {
            switch (currentData.getQuality()) {
                case KeyCode.PW_QUALITY_BAD:
                    pwQualityIv.setColorFilter(ContextCompat.getColor(context, R.color.bad_red));
                    break;
                case KeyCode.PW_QUALITY_OK:
                    pwQualityIv.setColorFilter(ContextCompat.getColor(context, R.color.ok_yellow));
                    break;
                case KeyCode.PW_QUALITY_STRONG:
                    pwQualityIv.setColorFilter(ContextCompat.getColor(context, R.color.strong_green));
                    break;
                default:
                    break;
            }
        }
    }


    //interface to handle clicks
    public interface OnItemClickListener {
        void onItemClick(PasswordData passwordData);

        void onItemLongClick(int position);
    }

    //to access method from outside
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
