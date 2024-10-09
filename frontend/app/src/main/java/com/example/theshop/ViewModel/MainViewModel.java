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
import com.example.theshop.Model.CategoryModel;
import com.example.theshop.Model.ItemsModel;
import com.example.theshop.Model.SliderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private Context context;
    private MutableLiveData<List<SliderModel>> _slider = new MutableLiveData<>();
    private MutableLiveData<List<CategoryModel>> _category = new MutableLiveData<>();
    private MutableLiveData<List<ItemsModel>> _bestSeller = new MutableLiveData<>();


    public MainViewModel(){}
    public MainViewModel (Context context){
        this.context = context.getApplicationContext();
    }

    public LiveData<List<SliderModel>> getSlider(){
        return _slider;
    }

    public LiveData<List<CategoryModel>> getCategory(){
        return  _category;
    }
    public LiveData<List<ItemsModel>> getBestSeller(){
        return  _bestSeller;
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

    public void loadCategory(Context context){
        String url = "http://192.168.43.233:8080/api/v1/categories";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON Response", response.toString());
                try {
                    List<CategoryModel> categoryModelList = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++){
                        JSONObject categoriesJson = response.getJSONObject(i);

                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.setId(categoriesJson.getLong("categories_id"));
                        categoryModel.setName(categoriesJson.getString("name"));
                        categoryModel.setImageUrl(categoriesJson.getString("image_url"));

                        categoryModelList.add(categoryModel);

                    }
                    _category.setValue(categoryModelList);
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

    public void loadBestSeller(Context context){
        String url = "http://192.168.43.233:8080/api/v1/items";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON Response", response.toString());
                try {
                    List<ItemsModel> itemModelList = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++){
                        JSONObject itemsJson = response.getJSONObject(i);

                        ItemsModel itemsModel = new ItemsModel();
                        itemsModel.setId(itemsJson.getLong("product_id"));
                        itemsModel.setName(itemsJson.getString("product_name"));
                        itemsModel.setImageUrl(itemsJson.getString("image_url"));
                        itemsModel.setDescription(itemsJson.getString("product_description"));
                        itemsModel.setPrice(itemsJson.getDouble("product_price"));

                        itemModelList.add(itemsModel);

                    }
                    _bestSeller.setValue(itemModelList);
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
