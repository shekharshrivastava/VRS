package com.app.ssoft.vrs.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ssoft.vrs.Model.VehicleData;
import com.app.ssoft.vrs.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shekahar.Shrivastava on 18-Jan-18.
 */

public class ListAdapter extends BaseAdapter {
    private List<VehicleData> m_item;
    public ArrayList<Integer> m_selectedItem;
    Context m_context;
    Boolean m_isRoot;
    private Bitmap thumbnailDrawable;

    public ListAdapter(Context p_context, List<VehicleData> p_item) {
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
        ViewHolder m_viewHolder = null;
        if (p_convertView == null) {
            LayoutInflater m_inflater = LayoutInflater.from(m_context);
            m_view = m_inflater.inflate(R.layout.row_layout, null);
            m_viewHolder = new ViewHolder();
            m_viewHolder.tvVehName = (TextView) m_view.findViewById(R.id.tvVehName);
            m_viewHolder.driverAvlb = (TextView) m_view.findViewById(R.id.driverAvlb);
            m_viewHolder.tvSeater = (TextView) m_view.findViewById(R.id.tvSeater);
            m_viewHolder.imVehicleImage = (ImageView) m_view.findViewById(R.id.ivVehicalPhoto);
            m_view.setTag(m_viewHolder);
        } else {
            m_view = p_convertView;
            m_viewHolder = ((ViewHolder) m_view.getTag());
        }

        m_viewHolder.tvVehName.setText(m_item.get(p_position).getVehicleModel());
        if ((m_item.get(p_position).getDriverReq() != null) && (m_item.get(p_position).getDriverReq()).equals("Yes")) {
            m_viewHolder.driverAvlb.setText("With Driver");
        } else {
            m_viewHolder.driverAvlb.setText("Without Driver");
        }
        m_viewHolder.tvSeater.setHint(m_item.get(p_position).getNumberOfseat() + " Seaters");
        if (m_item.get(p_position).getVehiclePhoto() != null) {
           /* Glide.with(m_context)
                    .load(new File(m_item.get(p_position).getVehiclePhoto()))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.alto)
                    .error(R.drawable.alto)
                    .into(m_viewHolder.imVehicleImage);*/
            File imgFile = new File(m_item.get(p_position).getVehiclePhoto());

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                m_viewHolder.imVehicleImage.setImageBitmap(myBitmap);

            }
        }
      /*  m_viewHolder.m_tvFileName.setText(m_item.get(p_position));
//        String m_filepath = new File(m_path.get(p_position)).getAbsolutePath();

     *//*   int m_lastIndex = new File(m_path.get(p_position)).getAbsolutePath().lastIndexOf(".");
        String m_filepath = new File(m_path.get(p_position)).getAbsolutePath();
        Bitmap imageThumbnail = setFileImageType(new File(m_path.get(p_position)));
        if (imageThumbnail != null && !(new File(m_path.get(p_position)).isDirectory())) {
            m_viewHolder.m_ivIcon.setImageBitmap(imageThumbnail);
        } else if ((new File(m_path.get(p_position)).isDirectory())) {
            m_viewHolder.m_ivIcon.setImageResource(R.drawable.closed_folders);
        } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png") ||
                m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg")) {
            m_viewHolder.m_ivIcon.setImageResource(R.drawable.picture_folder);
        } else {
            m_viewHolder.m_ivIcon.setImageResource(R.drawable.doc_folder);
        }*//*
        if (!(new File(m_path.get(p_position)).isDirectory())) {
            if (m_path.get(p_position).endsWith(".pdf")) {
                m_viewHolder.m_ivIcon.setImageResource(R.drawable.pdf_icon);
            } else if (m_path.get(p_position).endsWith(".txt")) {
                m_viewHolder.m_ivIcon.setImageResource(R.drawable.txt_icon);
            } else if (m_path.get(p_position).endsWith(".doc")) {
                m_viewHolder.m_ivIcon.setImageResource(R.drawable.doc_icon);
            } else if (m_path.get(p_position).endsWith(".apk")) {
                PackageManager pm = m_context.getPackageManager();
                PackageInfo pi = pm.getPackageArchiveInfo(m_path.get(p_position), PackageManager.GET_META_DATA);

                // the secret are these two lines....
                if (pi != null) {
                    pi.applicationInfo.sourceDir = m_path.get(p_position);
                    pi.applicationInfo.publicSourceDir = m_path.get(p_position);
                    //
                    Drawable APKicon = pi.applicationInfo.loadIcon(pm);
                    String AppName = (String) pi.applicationInfo.loadLabel(pm);

                    m_viewHolder.m_ivIcon.setImageDrawable(APKicon);
                }

            } else {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            setFileImageType(new File(m_path.get(p_position))).compress(Bitmap.CompressFormat.PNG, 50, stream);
                Glide.with(m_context)
                        .load(new File(m_path.get(p_position)))
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.empty_doc)
                        .into(m_viewHolder.m_ivIcon);
            }
        } else {
            m_viewHolder.m_ivIcon.setImageResource(R.drawable.closed_folders);
        }
*/

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

}