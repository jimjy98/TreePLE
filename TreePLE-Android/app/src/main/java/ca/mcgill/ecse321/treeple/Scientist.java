package ca.mcgill.ecse321.treeple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Scientist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scientist);
    }

    public void openAddTree(View view) {
        Intent i = new Intent(this, AddTree.class);
        //pass values to next activity
        i.putExtra("name", getIntent().getStringExtra("name"));
        i.putExtra("user", getIntent().getStringExtra("user"));
        startActivity(i);
    }

    public void openMarkTree(View view) {
        Intent i = new Intent(this, MarkTree.class);
        //pass values to next activity
        i.putExtra("name", getIntent().getStringExtra("name"));
        i.putExtra("user", getIntent().getStringExtra("user"));
        startActivity(i);
    }
}
