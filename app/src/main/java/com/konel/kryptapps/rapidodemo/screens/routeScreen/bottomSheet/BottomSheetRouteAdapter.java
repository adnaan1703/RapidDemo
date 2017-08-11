package com.konel.kryptapps.rapidodemo.screens.routeScreen.bottomSheet;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.konel.kryptapps.rapidodemo.R;
import com.konel.kryptapps.rapidodemo.model.directionsAPI.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 10:28 PM
 */


public class BottomSheetRouteAdapter extends RecyclerView.Adapter<BottomSheetRouteAdapter.ViewHolder> {


    private List<Step> steps;

    public BottomSheetRouteAdapter(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.bottom_sheet_direction_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updateUI(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvBottomSheetRouteItemDistanceText)
        TextView tvBottomSheetRouteItemDistanceText;

        @BindView(R.id.tvBottomSheetRouteItemDurationText)
        TextView tvBottomSheetRouteItemDurationText;

        @BindView(R.id.tvBottomSheetRouteItemInstructionText)
        TextView tvBottomSheetRouteItemInstructionText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void updateUI(Step step) {
            tvBottomSheetRouteItemDistanceText.setText(step.distance.text);
            tvBottomSheetRouteItemDurationText.setText(step.duration.text);
            //noinspection deprecation
            tvBottomSheetRouteItemInstructionText.setText(Html.fromHtml(step.htmlInstructions));
        }
    }
}
