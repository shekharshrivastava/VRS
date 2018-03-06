package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vehicle_list_layout, container, false);
        rl_lvListRoot = view.findViewById(R.id.list_item);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        vehicleDetails = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddVehicleActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        new getAllFilesData().execute();
        rl_lvListRoot.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                VehicleData vehicleData = vehicleDetails.get(position);
                String userId = vehicleData.getUserID();
                Intent intent = new Intent(getActivity(), VehicleDetailsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);

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
        }
    }

    public class getAllFilesData extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            ref = FirebaseDatabase.getInstance().getReference().child("vehicleDetails");
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
                            vehicleData.setVehicleModel(vehicleModel);
                            vehicleData.setDriverReq(driver);
                            vehicleData.setNumberOfseat(seater);
                            vehicleData.setVehiclePhoto(vehiclePhoto);
                            vehicleData.setUserID(dataSnapshot.getKey());
                            vehicleDetails.add(vehicleData);
                            if(m_listAdapter!=null) {
                                m_listAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }
                    });
            return null;
        }

        @Override
        protected void onPreExecute() {
            rl_lvListRoot.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);
//            rl_lvListRoot.setOnItemClickListener(null);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            rl_lvListRoot.setVisibility(View.VISIBLE);
            loadingIndicator.setVisibility(View.GONE);
            m_listAdapter = new ListAdapter(getActivity(), vehicleDetails);
            rl_lvListRoot.setAdapter(m_listAdapter);
            super.onPostExecute(aVoid);
        }
    }

}
