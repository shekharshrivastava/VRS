package com.app.ssoft.vrs.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ssoft.vrs.Model.VehicleData;
import com.app.ssoft.vrs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shekahar.Shrivastava on 19-Mar-18.
 */

public class CurrentRideAdapter extends BaseAdapter {
    private List<VehicleData> m_item;
    public ArrayList<Integer> m_selectedItem;
    Context m_context;
    Boolean m_isRoot;
    private Bitmap thumbnailDrawable;

    public CurrentRideAdapter(Context p_context, List<VehicleData> p_item) {
        m_context = p_context;
        m_item = p_item;
    }

    @Override
    public int getCount() {
        return m_item.size();
    }

    @Override
    public Object getItem(int position) {
        return m_item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int p_position, View p_convertView, ViewGroup p_parent) {
        View m_view = null;
        CurrentRideAdapter.ViewHolder m_viewHolder = null;
        if (p_convertView == null) {
            LayoutInflater m_inflater = LayoutInflater.from(m_context);
            m_view = m_inflater.inflate(R.layout.current_ride_layout, null);
            m_viewHolder = new CurrentRideAdapter.ViewHolder();
            m_viewHolder.tvVehName = (TextView) m_view.findViewById(R.id.tvVehName);
            m_viewHolder.driverAvlb = (TextView) m_view.findViewById(R.id.driverAvlb);
            m_viewHolder.tvSeater = (TextView) m_view.findViewById(R.id.tvSeater);
            m_viewHolder.imVehicleImage = (ImageView) m_view.findViewById(R.id.ivVehicalPhoto);
            m_viewHolder.avblStatus = (TextView) m_view.findViewById(R.id.avlblStatus);
            m_viewHolder.endTripButton = (Button) m_view.findViewById(R.id.endRideButton);
            m_viewHolder.detailsButton = (Button) m_view.findViewById(R.id.showDetailsButton);

            m_view.setTag(m_viewHolder);
        } else {
            m_view = p_convertView;
            m_viewHolder = ((CurrentRideAdapter.ViewHolder) m_view.getTag());
        }
        m_viewHolder.avblStatus.setVisibility(View.GONE);

        m_viewHolder.tvVehName.setText(m_item.get(p_position).getVehicleModel());
        if ((m_item.get(p_position).getDriverReq() != null) && (m_item.get(p_position).getDriverReq()).equals("Yes")) {
            m_viewHolder.driverAvlb.setText("With Driver");
        } else {
            m_viewHolder.driverAvlb.setText("Without Driver");
        }
        m_viewHolder.imVehicleImage.setTag(p_position);

        m_viewHolder.tvSeater.setHint(m_item.get(p_position).getNumberOfseat() + " Seaters");



        m_viewHolder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = m_item.get(p_position).getUserID();
                Intent intent = new Intent(m_context, VehicleDetailsActivity.class);
                intent.putExtra("userId", userId);
                m_context.startActivity(intent);
            }
        });

        m_viewHolder.endTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String advncePayment = m_item.get(p_position).getAdvanceAmnt();
                String rateValue = m_item.get(p_position).getRateValue();
                Intent intent = new Intent(m_context, BillGenerationActivity.class);
                intent.putExtra("userIdVal",m_item.get(p_position).getUserID());
                intent.putExtra("advPayment", advncePayment);
                intent.putExtra("rateValue", rateValue);
                m_context.startActivity(intent);
            }
        });

        if (m_item.get(p_position).getVehiclePhoto() != null) {
            m_viewHolder.imVehicleImage.setImageBitmap(StringToBitMap(m_item.get(Integer.parseInt(m_viewHolder.imVehicleImage.getTag().toString())).getVehiclePhoto()));
          /*  Glide.with(m_context)
                    .load(new File(m_item.get(p_position).getVehiclePhoto()))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.alto)
                    .error(R.drawable.alto)
                    .into(m_viewHolder.imVehicleImage);*/

        } else {
            m_viewHolder.imVehicleImage.setImageResource(R.drawable.placeholder_car);
        }


        return m_view;
    }

    class ViewHolder {
        TextView tvVehName;
        TextView driverAvlb;
        TextView tvSeater;
        ImageView imVehicleImage;
        TextView avblStatus;
        Button detailsButton;
        Button endTripButton;
    }

    /* private Bitmap setFileImageType(File m_file) {
         int m_lastIndex = m_file.getAbsolutePath().lastIndexOf(".");
         String m_filepath = m_file.getAbsolutePath();
         if (m_file.isDirectory())
             return null;
         else {
             if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png")) {
                 try {
                     thumbnailDrawable = Utils.getThumbnail(m_context.getContentResolver(), m_filepath);
                 } catch (Exception e) {
                     e.printStackTrace();
                     return null;
                 }
                 return thumbnailDrawable;
             } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg")) {

                 try {
                     thumbnailDrawable = Utils.getThumbnail(m_context.getContentResolver(), m_filepath);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 return thumbnailDrawable;
             } else {
                 return null;
             }
         }
     }

     String getLastDate(int p_pos) {
         File m_file = new File(m_path.get(p_pos));
         SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:a");
         return m_dateFormat.format(m_file.lastModified());
     }*/
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}