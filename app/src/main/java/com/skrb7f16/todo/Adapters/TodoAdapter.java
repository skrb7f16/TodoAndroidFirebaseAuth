package com.skrb7f16.todo.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.skrb7f16.todo.Models.Todos;
import com.skrb7f16.todo.R;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter {


    ArrayList<Todos> list=new ArrayList<>();
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth;
    int DONE_BTN_TYPE_TODO=1;
    int CHECK_TODO_TYPE=2;

    public TodoAdapter(ArrayList<Todos> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database=FirebaseDatabase.getInstance("https://todo-32f52-default-rtdb.firebaseio.com/");
        auth=FirebaseAuth.getInstance();
        if(viewType==DONE_BTN_TYPE_TODO){
            View view=LayoutInflater.from(context).inflate(R.layout.sample_todo,parent,false);
            return new TodoDoneViewHolder(view);
        }
        else{
            View view=LayoutInflater.from(context).inflate(R.layout.sample_todo_check,parent,false);
            return new TodoCheckViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if(holder.getClass()==TodoDoneViewHolder.class){
            ((TodoDoneViewHolder)holder).todo.setText(list.get(position).getTodo());
            ((TodoDoneViewHolder)holder).doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                    Date dateobj = new Date();
                    database.getReference().child("Todos").child(auth.getUid()).child(list.get(position).getTodoId()).child("isDone").setValue(true);
                    database.getReference().child("Todos").child(auth.getUid()).child(list.get(position).getTodoId()).child("timeStamp").setValue(df.format(dateobj));
                }
            });
            ((TodoDoneViewHolder)holder).deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    database.getReference().child("Todos").child(auth.getUid()).child(list.get(position).getTodoId()).removeValue();

                }
            });
        }
        else{
            ((TodoCheckViewHolder)holder).todo.setText(list.get(position).getTodo());
            ((TodoCheckViewHolder)holder).deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    database.getReference().child("Todos").child(auth.getUid()).child(list.get(position).getTodoId()).removeValue();

                }
            });
            ((TodoCheckViewHolder)holder).check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                    Date dateobj = new Date();
                    database.getReference().child("Todos").child(auth.getUid()).child(list.get(position).getTodoId()).child("isDone").setValue(false);
                    database.getReference().child("Todos").child(auth.getUid()).child(list.get(position).getTodoId()).child("timeStamp").setValue(df.format(dateobj));
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TodoCheckViewHolder extends RecyclerView.ViewHolder{

        TextView todo;
        ImageView check,deleteBtn;
        public TodoCheckViewHolder(@NonNull View itemView) {
            super(itemView);
            todo=itemView.findViewById(R.id.todo);
            check=itemView.findViewById(R.id.check);
            deleteBtn=itemView.findViewById(R.id.deleteBtn);
        }
    }

    public class TodoDoneViewHolder extends RecyclerView.ViewHolder{

        TextView todo;
        ImageView deleteBtn;
        Button doneBtn;
        public TodoDoneViewHolder(@NonNull View itemView) {
            super(itemView);
            todo=itemView.findViewById(R.id.todo);
            doneBtn=itemView.findViewById(R.id.doneBtn);
            deleteBtn=itemView.findViewById(R.id.deleteBtn);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getIsDone()){
            return CHECK_TODO_TYPE;
        }
        else{
            return DONE_BTN_TYPE_TODO;
        }
    }
}
