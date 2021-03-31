package com.skrb7f16.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skrb7f16.todo.Adapters.TodoAdapter;
import com.skrb7f16.todo.Models.Todos;
import com.skrb7f16.todo.databinding.ActivityMainBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    ActivityMainBinding binding;
    ArrayList<Todos> list=new ArrayList<>();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final TodoAdapter adapter=new TodoAdapter(list,MainActivity.this);
        binding.todoRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
        binding.todoRecycler.setLayoutManager(layoutManager);
        Toast.makeText(MainActivity.this,"Loading and refreshing your data",Toast.LENGTH_LONG).show();
        database=FirebaseDatabase.getInstance("https://todo-32f52-default-rtdb.firebaseio.com/");
        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Adding your todo");
        progressDialog.setMessage("Your Todo is being added");
        database.getReference().child("Todos").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               list.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Todos todo=dataSnapshot.getValue(Todos.class);
                    todo.setTodoId(dataSnapshot.getKey());
                    if(dataSnapshot.child("isDone").getValue().toString()=="false"){
                        todo.setIsDone(false);
                    }
                    else{
                        todo.setIsDone(true);
                    }

                    list.add(todo);
                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.addTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(binding.etTodo.getText().toString().length()==0){
                    Toast.makeText(MainActivity.this,"Empty Todo cannot be Passed",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Todos todo=new Todos();
                    todo.setTodo(binding.etTodo.getText().toString());
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                    Date dateobj = new Date();
                    todo.setTimestamp(df.format(dateobj));
                    todo.setIsDone(false);
                    database.getReference().child("Todos").child(auth.getUid()).push().setValue(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.hide();
                            binding.etTodo.setText("");
                        }
                    });
                }
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent=new Intent(MainActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });
    }
}