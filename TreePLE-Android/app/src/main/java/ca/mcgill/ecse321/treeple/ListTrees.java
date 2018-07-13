package ca.mcgill.ecse321.treeple;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

//list trees in a list format using ListView
//deprecated in favor of showing trees on Map
public class ListTrees extends AppCompatActivity {

    private List<JSONObject> treesList = new ArrayList<>();
    TreeAdapter adapterForTrees;
    private String errorMessage;
    enum alerts{error, success, remove};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trees);

        adapterForTrees = new TreeAdapter(this, R.layout.list_tree, treesList);
        final ListView listView = (ListView) findViewById(R.id.listOfTrees);
        listView.setAdapter(adapterForTrees);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = listView.getItemAtPosition(i);
                String r = o.toString();
                r = r.substring(r.indexOf(":"), r.indexOf(","));
                //removeTree(Integer.parseInt(r));
            }
        });
        refreshTrees();
    }

    private class TreeAdapter extends ArrayAdapter<JSONObject> {
        //constructor
        public TreeAdapter(@NonNull Context context, int resource, @NonNull List<JSONObject> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            JSONObject temp = getItem(position);
            String stringBuild;

            View listItemView = convertView;
            if(listItemView == null){
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_tree, parent, false);
            }

            try {
                TextView view = (TextView) listItemView.findViewById(R.id.tree_ID);
                view.setText("Tree_ID: "+temp.getString("id"));

                view = (TextView) listItemView.findViewById(R.id.tree_species);
                JSONObject temp2 = temp.getJSONObject("species");
                view.setText("Species: "+temp2.getString("nameEnglish"));

                view = (TextView) listItemView.findViewById(R.id.tree_dimensions);
                view.setText("Height: "+temp.getString("height")+", Width: "+temp.getString("diameter"));

                view = (TextView) listItemView.findViewById(R.id.tree_status);
                view.setText("Landtype: "+temp.getString("landType")+", Status: "+temp.getString("status"));

                view = (TextView) listItemView.findViewById(R.id.tree_location);
                temp2 = temp.getJSONObject("municipality");
                view.setText("Municipality: "+temp2.getString("name"));

                view = (TextView) listItemView.findViewById(R.id.tree_coordinates);
                view.setText("Latitude: "+temp.getString("x")+", Longitude: "+temp.getString("y"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return listItemView;
        }
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//        refreshTrees();
//    }

    private void refreshTrees() {
        HttpUtils.get("/trees", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray temp = response.getJSONArray("treeList");
                    for( int i = 0; i < temp.length(); i++){
                        treesList.add(temp.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    errorMessage = e.getMessage();
                    alertMessage(alerts.error);
                }
                adapterForTrees.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    errorMessage = errorResponse.get("message").toString();
                } catch (JSONException e) {
                    errorMessage = e.getMessage();
                }
                alertMessage(alerts.error);
            }
        });
    }


    private void alertMessage(alerts what){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(true);

        if(what == alerts.success){
            builder1.setMessage("Your tree was successfully added.");
            builder1.setNeutralButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }
        else if(what == alerts.error){
            builder1.setMessage("An error has occured:" + errorMessage);
            builder1.setNeutralButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }
        else if(what == alerts.remove){
            builder1.setMessage("Remove tree?");
            builder1.setPositiveButton(
                    "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //removeTree();
                            dialog.cancel();
                        }
                    });
            builder1.setNegativeButton(
                    "NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
