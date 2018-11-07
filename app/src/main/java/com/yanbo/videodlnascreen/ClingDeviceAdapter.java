package com.yanbo.videodlnascreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanbo.lib_screen.entity.ClingDevice;
import com.yanbo.lib_screen.listener.ItemClickListener;
import com.yanbo.lib_screen.manager.DeviceManager;

import java.util.List;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class ClingDeviceAdapter extends RecyclerView.Adapter<ClingDeviceAdapter.ClingHolder> {

    private LayoutInflater layoutInflater;
    private List<ClingDevice> clingDevices;
    private ItemClickListener clickListener;

    public ClingDeviceAdapter(Context context) {
        super();
        layoutInflater = LayoutInflater.from(context);
        clingDevices = DeviceManager.getInstance().getClingDeviceList();
    }

    @NonNull
    @Override
    public ClingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_common_layout, parent, false);
        return new ClingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClingHolder holder, final int position) {
        final ClingDevice device = clingDevices.get(position);
        if (device == DeviceManager.getInstance().getCurrClingDevice()) {
            holder.iconView.setVisibility(View.VISIBLE);
        } else {
            holder.iconView.setVisibility(View.INVISIBLE);
        }
        holder.nameView.setText(device.getDevice().getDetails().getFriendlyName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemAction(position, device);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clingDevices.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    static class ClingHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        ImageView iconView;

        public ClingHolder(View itemView) {
            super(itemView);
           nameView=itemView.findViewById(R.id.text_name);
            iconView=itemView.findViewById(R.id.img_icon);
        }
    }


}
