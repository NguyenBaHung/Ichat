package com.example.nbhung318.ichat.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nbhung318.ichat.R;
import com.example.nbhung318.ichat.entity.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.ViewHolder> {
    private OnClickedListener onClickedListener;
    private LayoutInflater inflater;
    private Context context;
    private List<User> users;

    public void setOnClickedListener(OnClickedListener onClickedListener){
        this.onClickedListener = onClickedListener;
    }

    public AllUserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        User user = users.get(position);
        Picasso.get().load(user.getUserImage()).into(viewHolder.imgAvatarUser);
        viewHolder.txtNameUser.setText(user.getUserName());
        viewHolder.txtStatusUser.setText(user.getUserStatus());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedListener.onClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgAvatarUser;
        private TextView txtNameUser;
        private TextView txtStatusUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatarUser = itemView.findViewById(R.id.imgAvatarUser);
            txtNameUser = itemView.findViewById(R.id.txtNameUser);
            txtStatusUser = itemView.findViewById(R.id.txtStatusUser);
        }
    }

    public interface OnClickedListener{
        void onClicked(int position);
    }
}
