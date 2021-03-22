package com.example.awsuploadapplication.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awsuploadapplication.R;
import com.example.awsuploadapplication.databinding.RecyclerRowBinding;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.myViewHolder> {

    RecyclerRowBinding rowBinding;
    private Context context;
    private List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;

    }
    public UserListAdapter(Context context) {this.context = context;}

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.recycler_row, parent, false);

        return new myViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.myViewHolder holder, int position) {
        final User user = userList.get(position);

        holder.tvImage.setImageBitmap(DataConverter.convertByteArray(user.getImage()));

    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView tvImage;

        public myViewHolder(RecyclerRowBinding rowBinding) {
            super(rowBinding.getRoot());
            tvImage = rowBinding.tvImage;
        }
    }
}

