package ca.mcgill.ecse321.treeple;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MarkTree extends AppCompatActivity {
    //to pass to intent
    private boolean municipalitySet = false;
    private boolean speciesSet = false;
    private String municipality;
    private String species;

    List<String> speciesList = new ArrayList<>();
    ArrayAdapter<String> speciesAdapater;

    List<String> municipalityList = new ArrayList<>();
    ArrayAdapter<String> municipalityAdapter;

    String errorMessage;
    enum alerts{error, success};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_tree);

        final Spinner speciesSpinner = (Spinner) findViewById(R.id.speciesSpinner);
        final Spinner municipalitySpinner = (Spinner) findViewById(R.id.municipalitySpinner);

        speciesAdapater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, speciesList);
        speciesAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        municipalityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, municipalityList);
        municipalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        municipalitySpinner.setAdapter(municipalityAdapter);
        speciesSpinner.setAdapter(speciesAdapater);

        refreshSpinners(this.getCurrentFocus());
    }

    private void refreshSpinners(View view){
        refresh(speciesAdapater, speciesList, "species");
        refresh(municipalityAdapter, municipalityList, "municipalities");
    }

    //HTTP GET a list of municipalities / species depending on call
    //copy list to corresponding adapter
    private void refresh(final ArrayAdapter<String> adapter, final List<String> list, final String which){
        HttpUtils.get(which, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response);
                list.clear();

                if(which.equals("species")){
                    for( int i = 0; i < response.length(); i++){
                        try {
                            speciesList.add(response.getJSONObject(i).getString("nameEnglish"));
                        } catch (Exception e) {
                            errorMessage = e.getMessage();
                        }
                    }
                }

                else if(which.equals("municipalities")){
                    for( int i = 0; i < response.length(); i++){
                        try {
                            municipalityList.add(response.getJSONObject(i).getString("name"));
                        } catch (Exception e) {
                            errorMessage = e.getMessage();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    errorMessage = errorResponse.get("message").toString();
                } catch (JSONException e) {
                    errorMessage = e.getMessage();
                }
            }
        });
    }

    private void alertMessage(alerts what){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        if(what == alerts.error){
            builder1.setMessage(errorMessage);
        }
        else if(what == alerts.success){
            builder1.setMessage("OK.");
        }
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

    //user clicked findTrees
    public void findTrees(View view){
        Intent i = new Intent(this, MapsActivity.class);
        setInfoToSend();
        //pass values to next activity
        i.putExtra("speCheck", speciesSet);
        i.putExtra("munCheck", municipalitySet);
        i.putExtra("species", species);
        i.putExtra("municipality", municipality);
        i.putExtra("name", getIntent().getStringExtra("name"));
        i.putExtra("user", getIntent().getStringExtra("user"));
        startActivity(i);
    }

    //check if species or municipalities are checked, and send the corresponding spinner Item if so
    public void setInfoToSend() {
        CheckBox temp = (CheckBox)findViewById(R.id.municipalityCheck);
        if (temp.isChecked()) {
            municipalitySet = true;
        }
        else{
            municipalitySet = false;
        }
        temp = (CheckBox)findViewById(R.id.speciesCheck);
        if (temp.isChecked()) {
            speciesSet = true;
        }
        else{
            speciesSet = false;
        }
        species = ((Spinner)findViewById(R.id.speciesSpinner)).getSelectedItem().toString();
        municipality = ((Spinner)findViewById(R.id.municipalitySpinner)).getSelectedItem().toString();
    }
}
