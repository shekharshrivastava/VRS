package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ssoft.vrs.Model.VehicleData;
import com.app.ssoft.vrs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import static android.app.Activity.RESULT_FIRST_USER;

/**
 * Created by Shekahar.Shrivastava on 06-Mar-18.
 */

public class VehicleListFragment extends android.support.v4.app.Fragment {

    private FirebaseAuth auth;
    private VehicleData vehicleData;
    private ListView rl_lvListRoot;
    private ArrayList<VehicleData> vehicleDetails;
    private ListAdapter m_listAdapter;
    private MKLoader loadingIndicator;
    private DatabaseReference ref;
    private FrameLayout content_frame;
    private CardView card_view;
    private String source = "";
    private String dest = "";
    private ArrayList<VehicleData> vehicleFilterData;
    private TextView searchTV;
    private String vehType = "";
    private ArrayList<VehicleData> vehicleSearchList;
    private String savedDestination;
    private String savedSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vehicle_list_layout, container, false);
        rl_lvListRoot = view.findViewById(R.id.list_item);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        searchTV = view.findViewById(R.id.searchTV);
        card_view = view.findViewById(R.id.card_view);
        vehicleDetails = new ArrayList<>();
        vehicleFilterData = new ArrayList<>();
        vehicleSearchList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddVehicleActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        new getAllFilesData().execute("vehicleDetails", source, dest);
        rl_lvListRoot.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                VehicleData vehicleData = vehicleDetails.get(position);
                String userId = vehicleData.getUserID();
                Intent intent = new Intent(getActivity(), VehicleDetailsActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("isFromMyVehicles", true);
                startActivity(intent);

            }


        });
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchVehicleActivity.class);
                source = "";
                dest = "";
                startActivityForResult(intent, 2);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("VRS");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_FIRST_USER) {
            boolean message = data.getBooleanExtra("dataAdded", false);
            Toast.makeText(getActivity(), "Data Added successfully", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2 && resultCode == 2) {
            vehicleSearchList.clear();
            source = data.getStringExtra("source");
            dest = data.getStringExtra("dest");
            vehType = data.getStringExtra("vehicleType");
            searchTV.setText("Result showing from source : " + source);
            if (!source.isEmpty() && !dest.isEmpty()) {

                ArrayList<VehicleData> vehicleDataList = new ArrayList();

                for (VehicleData vehData : vehicleFilterData) {
                    if ((!vehData.getSource().isEmpty() && vehData.getSource().equalsIgnoreCase(source))
                            && (!vehData.getDestination().isEmpty() && vehData.getDestination().equalsIgnoreCase(dest))
                            && (vehData.getVehicleType()!=null && vehData.getVehicleType().equalsIgnoreCase(vehType))) {
                        vehicleDataList.add(vehData);

                    }

                }
                if (vehicleDataList.size() > 0) {
                    vehicleDetails.removeAll(vehicleFilterData);
                    vehicleDetails.addAll(vehicleDataList);
                } else {
                    vehicleDataList.clear();
                    Toast.makeText(getActivity(), "No result found! Press home to reload", Toast.LENGTH_SHORT).show();
                }
                          /*  if (vehiclesData.getSource() != null && vehiclesData.getSource().contains(source)
                                    && vehiclesData.getVehicleType() != null
                                    && vehiclesData.getVehicleType().contains(vehType)) {
                                vehicleDetails.removeAll(vehicleFilterData);
                                vehicleSearchList.add(vehicleData);
                                vehicleDetails.add(vehicleData);

                            }*/
            }
//            new getAllFilesData().execute("vehicleDetails", source, dest);
        }
    }

    public class getAllFilesData extends AsyncTask<String, Void, ArrayList<VehicleData>> {


        @Override
        protected ArrayList<VehicleData> doInBackground(final String... String) {
            return getAllVehicleData(String[0], String[1], String[2]);

        }

        @Override
        protected void onPreExecute() {
            rl_lvListRoot.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);
//            rl_lvListRoot.setOnItemClickListener(null);
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(ArrayList<VehicleData> vehicleData) {

            rl_lvListRoot.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.GONE);
            m_listAdapter = new ListAdapter(getActivity(), vehicleData);
            rl_lvListRoot.setAdapter(m_listAdapter);

            super.onPostExecute(vehicleData);
        }
    }

    public ArrayList<VehicleData> getAllVehicleData(String childColumn, final String source, final String destination) {
        ref = FirebaseDatabase.getInstance().getReference().child(childColumn);
        ref.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        VehicleData vehiclesData = dataSnapshot.getValue(VehicleData.class);
                        VehicleData vehicleData = new VehicleData();
                        String vehicleModel = vehiclesData.getVehicleModel();
                        String driver = vehiclesData.getDriverReq();
                        String seater = vehiclesData.getNumberOfseat();
                        String vehiclePhoto = vehiclesData.getVehiclePhoto();
                        String sourceValue = vehiclesData.getSource();
                        String destinationVal = vehiclesData.getDestination();
                        String vehType = vehiclesData.getVehicleType();
                        vehicleData.setVehicleModel(vehicleModel);
                        vehicleData.setDriverReq(driver);
                        vehicleData.setNumberOfseat(seater);
                        vehicleData.setVehiclePhoto(vehiclePhoto);
                        vehicleData.setUserID(dataSnapshot.getKey());
                        vehicleData.setSource(sourceValue);
                        vehicleData.setDestination(destinationVal);
                        vehicleData.setVehicleType(vehType);

//                        vehicleFilterData.add(vehicleData);
                        if (source.isEmpty() && destination.isEmpty() && (vehiclesData.getCurrentUserID()!=null &&!(vehiclesData.getCurrentUserID().equalsIgnoreCase(auth.getCurrentUser().getUid())))) {
                            vehicleFilterData.add(vehicleData);
                            vehicleDetails.add(vehicleData);
                        }
                        if (m_listAdapter != null && vehicleDetails.size() > 0) {
                            m_listAdapter.notifyDataSetChanged();
                            loadingIndicator.setVisibility(View.GONE);
                        } else {
                            loadingIndicator.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        m_listAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        m_listAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        return vehicleDetails;
    }
}
