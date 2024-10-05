package com.example.theshop.ViewModel;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.theshop.Model.SliderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private Context context;
    private MutableLiveData<List<SliderModel>> _slider = new MutableLiveData<>();

    public MainViewModel(){}
    public MainViewModel (Context context){
        this.context = context.getApplicationContext();
    }

    public LiveData<List<SliderModel>> getSlider(){
        return _slider;
    }

    public void loadSlider(Context context){
        String url = "http://192.168.43.233:8080/api/v1/bannersliders";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON Response", response.toString());
                try {
                    List<SliderModel> sliderList = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++){
                        JSONObject bannerJson = response.getJSONObject(i);

                        SliderModel sliderModel = new SliderModel();
                        sliderModel.setId(bannerJson.getLong("id"));
                        sliderModel.setimageUrl(bannerJson.getString("imageUrl"));
                        sliderModel.setName(bannerJson.getString("bannerName"));
                        sliderList.add(sliderModel);

                    }
                    _slider.setValue(sliderList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.getMessage();
                    Toast.makeText(context, "Cannot retrieve image url", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.getMessage();
                Toast.makeText(context, "Faileddd!!!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}
