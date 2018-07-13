package ca.mcgill.ecse321.treeple;

import android.Manifest;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import cz.msebera.android.httpclient.Header;

public class AddTree extends AppCompatActivity {

    private double latitude, longitude;
    private boolean dateSet = false;
    private LocationManager locationManager;
    private LocationListener locationListener;
    String errorMessage; //dynamic error message = JSONresponse if it's an error
    enum alerts{empty, success, error}; //allowable choices for alert

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tree);
        setLandTypeValues();
    }


    //set the landTypeSpinner's values and corresponding adapter
    private void setLandTypeValues(){
        Spinner landTypeSpinner = (Spinner) findViewById(R.id.landType);
        String[] landTypeValues = new String[] {
                "Residential", "Institutional", "Park", "Municipal"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, landTypeValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        landTypeSpinner.setAdapter(adapter);
    }

    //get user's location
    public void getLocation(View view) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                latitude = (double)Math.round(location.getLatitude()*100d)/100d;
                longitude = (double)Math.round(location.getLongitude()*100d)/100d;
                locationManager.removeUpdates(this); //remove old location of user

                //change corresponding textViews on AddTree layout
                TextView latitudeView = (TextView) findViewById(R.id.latitudeView);
                latitudeView.setText("" + latitude);

                TextView longitudeView = (TextView) findViewById(R.id.longtitudeView);
                longitudeView.setText("" + longitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},
                    10);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
    }

    public void addTree(View view) {
        //get reporter name
        String name = getIntent().getStringExtra("name");

        //get tree species
        TextView tv = (TextView) findViewById(R.id.addTreeType);
        String treeType = tv.getText().toString();

        //get land type
        Spinner landTypeSpinner = (Spinner) findViewById(R.id.landType);
        String landType = landTypeSpinner.getSelectedItem().toString();

        //get municipality
        tv = (TextView) findViewById(R.id.municipality);
        String municipality = tv.getText().toString();

        //get location
        tv = (TextView) findViewById(R.id.latitudeView);
        String lat = tv.getText().toString();

        tv = (TextView) findViewById(R.id.longtitudeView);
        String longit = tv.getText().toString();


        //alert user if empty fields
        if(name.equals("") || treeType.equals("") || landType.equals("") || landType == null ||
                municipality.equals("") || lat.equals("") || longit.equals("") || !dateSet){
            alertMessage(alerts.empty);
        }
        else{
            //get date
            tv = (TextView) findViewById(R.id.getDate);
            String text = tv.getText().toString();
            String comps[] = text.split("-");
            int year = Integer.parseInt(comps[2]);
            int month = Integer.parseInt(comps[1]);
            int day = Integer.parseInt(comps[0]);
            NumberFormat formatter = new DecimalFormat("00");

            RequestParams rp = new RequestParams();
            rp.add("municipality", municipality);
            rp.add("x", lat);
            rp.add("y", longit);
            rp.add("species", treeType);
            rp.add("landType", landType);
            rp.add("date", year + "-" + formatter.format(month) + "-" + formatter.format(day));
            rp.add("reporter", name);

            //HTTP POST
            HttpUtils.post("tree/report", rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    alertMessage(alerts.success);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    try {
                        errorMessage = errorResponse.get("message").toString();
                        alertMessage(alerts.error);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return;
        }
    }

    private void alertMessage(alerts what){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        if(what == alerts.empty){
            builder1.setMessage("Some fields are empty.");
        }
        else if(what == alerts.success){
            builder1.setMessage("Your tree was successfully added.");
        }
        else if(what == alerts.error){
            builder1.setMessage("An error has occured:" + errorMessage);
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

    //DATE METHODS
    private Bundle getDateFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split("-");
        int day = 1;
        int month = 1;
        int year = 1;

        if (comps.length == 3) {
            day = Integer.parseInt(comps[0]);
            month = Integer.parseInt(comps[1]);
            year = Integer.parseInt(comps[2]);
        }

        rtn.putInt("day", day);
        rtn.putInt("month", month-1);
        rtn.putInt("year", year);

        return rtn;
    }

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void setDate(int id, int d, int m, int y) {
        dateSet = true;
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d-%02d-%04d", d, m + 1, y));
    }
}
