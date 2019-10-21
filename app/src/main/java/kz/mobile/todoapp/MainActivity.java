package kz.mobile.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ToDoAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;
    protected static ArrayList<ToDo> toDos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        initAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CREATE_TO_DO_REQUEST) {
                ToDo toDo = (ToDo) data.getSerializableExtra(Constants.TODO);
                if (toDo != null) {
                    toDoAdapter.addItem(toDo);
                }
            }
            else if (requestCode == Constants.UPDATE_TO_DO_REQUEST) {
                ToDo toDo = (ToDo) data.getSerializableExtra(Constants.TODO);
                int position = data.getIntExtra(Constants.POSITION, -1);
                if (toDo != null) {
                    if (position >= 0) {
                        toDoAdapter.updateItem(position, toDo);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionCreate) {
            Intent intent = new Intent(this, CreateToDoActivity.class);
            startActivityForResult(intent, Constants.CREATE_TO_DO_REQUEST);
            return true;
        }
        return false;
    }


    @Override
    public void onItemClick(int position, ToDo item) {
        Intent intent = new Intent(this, CreateToDoActivity.class);
        intent.putExtra(Constants.TODO, item);
        intent.putExtra(Constants.POSITION, position);
        startActivityForResult(intent, Constants.UPDATE_TO_DO_REQUEST);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initAdapter() {
        toDoAdapter = new ToDoAdapter(this);
        recyclerView.setAdapter(toDoAdapter);
        toDos = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readFromFile();
    }

    private void readFromFile() {
        GetAllItemsAsync getAllItemsAsync = new GetAllItemsAsync(this);
        getAllItemsAsync.doInBackground();
        if (toDos.size() != 0) {
            toDoAdapter.addAll(toDos);
            Toast.makeText(this, "Objects read from local storage!", Toast.LENGTH_LONG).show();
        }
    }

    private static class GetAllItemsAsync extends AsyncTask<Void, Void, Void> {

        private Activity activity;
        public GetAllItemsAsync(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Todo get all saved items from file
            File dir = new File(activity.getFilesDir(), Constants.DIR);
            File dataFile = new File(dir, Constants.FILENAME);
            FileUtils.readFromFile(dataFile);
            return null;
        }
    }
}
