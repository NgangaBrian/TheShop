package com.example.theshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.example.theshop.Model.SliderModel;
import com.example.theshop.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderModel> sliderModels;
    private ViewPager2 viewPager2;
    private Context context;

    public SliderAdapter(List<SliderModel> sliderModels, ViewPager2 viewPager2) {
        this.context = context;
        this.sliderModels = sliderModels;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.slider_image_container, parent, false);

        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewHolder holder, int position) {
        holder.setImage(sliderModels.get(position), context);
        if(position == sliderModels.size() -1){
            viewPager2.post(() -> notifyDataSetChanged());
        }
    }

    @Override
    public int getItemCount() {
        return sliderModels.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        public void setImage(SliderModel sliderModel, Context context){
            Glide.with(context).clear(imageView);
            RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop());
            Glide.with(context)
                    .load(sliderModel.getimageUrl())
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
}
