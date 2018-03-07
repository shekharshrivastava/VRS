package com.app.ssoft.vrs.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ssoft.vrs.Model.VehicleData;
import com.app.ssoft.vrs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shekahar.Shrivastava on 06-Mar-18.
 */

class MyVehicleAdapter extends BaseAdapter {
    private List<VehicleData> m_item;
    public ArrayList<Integer> m_selectedItem;
    Context m_context;
    Boolean m_isRoot;
    private Bitmap thumbnailDrawable;

    public MyVehicleAdapter(Context p_context, List<VehicleData> p_item) {
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
        MyVehicleAdapter.ViewHolder m_viewHolder = null;
        if (p_convertView == null) {
            LayoutInflater m_inflater = LayoutInflater.from(m_context);
            m_view = m_inflater.inflate(R.layout.row_layout, null);
            m_viewHolder = new MyVehicleAdapter.ViewHolder();
            m_viewHolder.tvVehName = (TextView) m_view.findViewById(R.id.tvVehName);
            m_viewHolder.driverAvlb = (TextView) m_view.findViewById(R.id.driverAvlb);
            m_viewHolder.tvSeater = (TextView) m_view.findViewById(R.id.tvSeater);
            m_viewHolder.imVehicleImage = (ImageView) m_view.findViewById(R.id.ivVehicalPhoto);
            m_view.setTag(m_viewHolder);
        } else {
            m_view = p_convertView;
            m_viewHolder = ((MyVehicleAdapter.ViewHolder) m_view.getTag());
        }

        m_viewHolder.tvVehName.setText( m_item.get(p_position).getVehicleModel());
        if((m_item.get(p_position).getDriverReq() !=null) && (m_item.get(p_position).getDriverReq()).equals("Yes")) {
            m_viewHolder.driverAvlb.setText("With Driver");
        }else{
            m_viewHolder.driverAvlb.setText("Without Driver");
        }
        m_viewHolder.imVehicleImage.setTag(p_position);

        m_viewHolder.tvSeater.setHint( m_item.get(p_position).getNumberOfseat() + " Seaters");

        if (m_item.get(p_position).getVehiclePhoto() != null) {
            m_viewHolder.imVehicleImage.setImageBitmap(StringToBitMap(m_item.get(Integer.parseInt(m_viewHolder.imVehicleImage.getTag().toString())).getVehiclePhoto()));
          /*  Glide.with(m_context)
                    .load(new File(m_item.get(p_position).getVehiclePhoto()))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.alto)
                    .error(R.drawable.alto)
                    .into(m_viewHolder.imVehicleImage);*/

        }else{
            m_viewHolder.imVehicleImage.setImageResource(R.drawable.placeholder_car);
        }


        return m_view;
    }

    class ViewHolder {
        TextView tvVehName;
        TextView driverAvlb;
        TextView tvSeater;
        ImageView imVehicleImage;
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
   public Bitmap StringToBitMap(String encodedString){
       try {
           byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
           Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
           return bitmap;
       } catch(Exception e) {
           e.getMessage();
           return null;
       }
   }
}