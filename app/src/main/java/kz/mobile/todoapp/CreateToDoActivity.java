package kz.mobile.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreateToDoActivity extends AppCompatActivity implements OnTaskCompleted {

    private EditText inputName;
    private EditText inputDescription;
    private Button buttonCreate;
    private Button buttonShare;
    private CreateToDoAsync createToDoAsync;

    private ToDo toDo;
    private int toDoPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do);
        initViews();
        setToDoData();
    }

    private void initViews() {
        inputName = findViewById(R.id.inputName);
        inputDescription = findViewById(R.id.inputDescription);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonShare = findViewById(R.id.buttonShare);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String description = inputDescription.getText().toString();
                createToDo(name, description);
            }
        });
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra("Title: " + Intent.EXTRA_SUBJECT, inputName.getText().toString());
                intent.putExtra("Description: " + Intent.EXTRA_TEXT, inputDescription.getText().toString());
                startActivity(Intent.createChooser(intent, "Share todo"));
            }
        });
    }

    private void setToDoData() {
        if (getIntent().hasExtra(Constants.TODO)) {
            toDo = (ToDo)getIntent().getSerializableExtra(Constants.TODO);
            toDoPosition = (getIntent().getIntExtra(Constants.POSITION, -2));
            if (toDo != null) {
                inputName.setText(toDo.getName());
                inputDescription.setText(toDo.getDescription());
            }
        }
    }

    private void createToDo(String name, String description) {
        if ((name != null && description != null) && (!name.equals("") && !description.equals(""))) {
            if (toDo == null) {
                toDo = new ToDo(name, description);
            } else {
                toDo.setName(name);
                toDo.setDescription(description);
            }
            createToDoAsync = new CreateToDoAsync(this);
            createToDoAsync.execute(toDo);
            Toast.makeText(this, "Object saved on local storage!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please fill name and description!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void itemCreated(ToDo toDo) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TODO, toDo);
        if (toDoPosition != -1) {
            intent.putExtra(Constants.POSITION, toDoPosition);
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private class CreateToDoAsync extends AsyncTask<ToDo, Void, ToDo> {

        private OnTaskCompleted listener;

        CreateToDoAsync(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ToDo doInBackground(ToDo... voids) {
            //Todo save object to file
            if (voids[0] != null ){
                File dir = new File(CreateToDoActivity.this.getFilesDir(), Constants.DIR);
                File dataFile = new File (dir, Constants.FILENAME);
                if (toDoPosition != -1) {
                    MainActivity.toDos.set(toDoPosition, voids[0]);
                    FileUtils.saveToFile(dataFile);
                } else {
                    MainActivity.toDos.add(voids[0]);
                    if ( !dir.exists()) {
                        dir.mkdirs();
                    }
                    FileUtils.saveToFile(dataFile);
                }

            }
            return voids[0];
        }

        @Override
        protected void onPostExecute(ToDo item) {
            super.onPostExecute(item);
            if (listener != null) {
                listener.itemCreated(item);
            }
        }
    }



}
