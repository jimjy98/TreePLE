package ca.mcgill.ecse321.treeple;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static ca.mcgill.ecse321.treeple.MapsActivity.alerts.tree;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private List<JSONObject> treesList = new ArrayList<>();
    private String errorMessage;

    enum alerts{error, parseerror, success, cutSuccess, markSuccess, diseaseSuccess, tree};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getMarkers();
    }

    private void placeMarkers() {
        double sumX = 0, sumY = 0;
        System.out.println("2"+treesList);
        for(JSONObject tree : treesList){
            try {
                int ID = tree.getInt("id");
                Double lat = tree.getDouble("x"); sumX += lat;
                Double lng = tree.getDouble("y"); sumY += lng;
                LatLng temp = new LatLng(lat, lng);
                Double height = tree.getDouble("height");
                Double diameter = tree.getDouble("diameter");
                String species = tree.getJSONObject("species").getString("nameEnglish");
                System.out.println("SPECIES IS: "+species);
                String status = tree.getString("status");
                mMap.addMarker(new MarkerOptions().position(temp).title("Tree_ID: "+Integer.toString(ID)).snippet("" +
                        "Species: "+species+
                        "\nDiameter:"+diameter+
                        "\nHeight: "+height+
                        "\nStatus: "+status).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            } catch (JSONException e) {
                errorMessage = e.getMessage();
                alertMessage(alerts.parseerror, null);
            }
        }

        //set custom infoWindow so that more than one line of information can be displayed
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext();

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        //move camera to average location of markers
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(sumX/treesList.size(), sumY/treesList.size())));

        //check if user has clicked an info window
        //if so, show them an alert
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String markerTitle = marker.getTitle();
                markerTitle = markerTitle.substring(markerTitle.indexOf(" ") + 1);
                System.out.println(markerTitle);
                alertMessage(alerts.tree, markerTitle);
            }
        });
    }

    //get markers via HTTP GET
    private void getMarkers() {
        RequestParams rp = new RequestParams();

        //use values passed from MarkTree spinner's as specified by user
        //only use them if the corresponding checkboxes were checked
        String species = getIntent().getStringExtra("species");
        String municipality = getIntent().getStringExtra("municipality");
        if(getIntent().getBooleanExtra("munCheck", false)){
            rp.add("municipality", municipality);
        }
        if(getIntent().getBooleanExtra("speCheck", false)) {
            rp.add("species", species);
        }
        if(getIntent().getStringExtra("user").equals("local")){
            rp.add("markedOrDiseased", "true");
        }

        Log.v("Maps", Boolean.toString(getIntent().getBooleanExtra("speCheck", true)));
        Log.v("Maps", Boolean.toString(getIntent().getBooleanExtra("munCheck", true)));
        Log.v("Maps", "WHAT WAS SET: SPECIES = "+species+" MUNICIPALITY = "+municipality);

        HttpUtils.get("trees", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray temp = response.getJSONArray("treeList");
                    for( int i = 0; i < temp.length(); i++){
                        try {
                            treesList.add(temp.getJSONObject(i));
                        } catch (Exception e) {
                            errorMessage = e.getMessage();
                            alertMessage(alerts.error, null);
                        }
                    }

                    //call place markers to place markers on map
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                placeMarkers();
                            }catch(Exception e){
                                System.out.println(e);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    errorMessage = errorResponse.get("message").toString();
                } catch (JSONException e) {
                    errorMessage = e.getMessage();
                }
                alertMessage(alerts.error, null);
            }
        });
    }

    private void alertMessage(alerts what, final String ID){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        //if user is local/scientist, pass the appropriate action to removeTree method
        if(what == tree){
            //if user is local, give them the option to either remove a tree or not
            if(getIntent().getStringExtra("user").equals("local")){
                builder1.setMessage("Would you like to remove this tree?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //pass the ID of the tree to removeTree
                                removeTree(ID, "CuttedDown");
                                dialog.cancel();
                            }
                        });
                builder1.setNeutralButton(
                        "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            //if user is a scientist, give them the option to cut a tree, mark it for cut, or mark it for disease
            else if(getIntent().getStringExtra("user").equals("scientist")){
                builder1.setMessage("Would you like to remove or mark tree for disease?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "MARK FOR CUT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeTree(ID, "Marked");
                                dialog.cancel();
                            }
                        });
                builder1.setNeutralButton(
                        "CUT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeTree(ID, "CuttedDown");
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "MARK FOR DISEASE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeTree(ID, "Diseased");
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            return;
        }
        else if(what == alerts.success){
            builder1.setMessage("Trees were successfully loaded.");
        }
        else if(what == alerts.error){
            builder1.setMessage("There was an error retrieving trees:" + errorMessage);
        }
        else if(what == alerts.parseerror){
            builder1.setMessage("There was an error - try again.");
        }
        else if(what == alerts.cutSuccess){
            builder1.setMessage("Tree was removed.");
        }
        else if(what == alerts.markSuccess){
            builder1.setMessage("Tree was marked for cut down.");
        }
        else if(what == alerts.diseaseSuccess){
            builder1.setMessage("Tree was marked for disease.");
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

    //HTTP post a new report that corresponds to the intent of the local resident/scientist
    private void removeTree(String ID, final String intent){
        RequestParams rp = new RequestParams();
        rp.add("status", intent); //if user is local, only show removable trees.
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String comps[] = date.split("-");
        int year = Integer.parseInt(comps[2]);
        int month = Integer.parseInt(comps[1]);
        int day = Integer.parseInt(comps[0]);
        NumberFormat formatter = new DecimalFormat("00");
        rp.add("date", year + "-" + formatter.format(month) + "-" + formatter.format(day));
        rp.add("reporter", getIntent().getStringExtra("name"));

        //HTTP POST
        HttpUtils.post("tree/report/"+ID, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(intent.equals("CuttedDown"))
                    alertMessage(alerts.cutSuccess, null);
                else if(intent.equals("Marked"))
                    alertMessage(alerts.markSuccess, null);
                else if(intent.equals("Diseased"))
                    alertMessage(alerts.diseaseSuccess, null);

                //reset markers
                getMarkers();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    errorMessage = errorResponse.get("message").toString();
                    alertMessage(alerts.error, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //hide the "help" window at the top of MapsActivity if clicked
    public void removeInfo(View view){
        view.setVisibility(View.GONE);
    }
}
