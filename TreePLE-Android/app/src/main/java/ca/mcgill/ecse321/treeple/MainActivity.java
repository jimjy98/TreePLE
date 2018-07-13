package ca.mcgill.ecse321.treeple;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openLocal(View view) {
        Intent i = new Intent(this, LocalResident.class);
        String name = ((TextView)findViewById(R.id.enterName)).getText().toString();
        if(name.equals("") || name == null){
            alertMessage();
            return;
        }
        //pass values to next activity
        i.putExtra("name", name);
        i.putExtra("user", "local");
        startActivity(i);
    }

    public void openScientist(View view) {
        Intent i = new Intent(this, Scientist.class);
        String name = ((TextView)findViewById(R.id.enterName)).getText().toString();
        if(name.equals("") || name == null){
            alertMessage();
            return;
        }
        //pass values to next activity
        i.putExtra("name", name);
        i.putExtra("user", "scientist");
        startActivity(i);
    }

    private void alertMessage(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Name is required.");
        builder1.setCancelable(true);

        builder1.setNeutralButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
