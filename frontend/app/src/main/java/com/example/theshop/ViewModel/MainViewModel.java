package com.example.theshop.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.theshop.BuildConfig;
import com.example.theshop.Model.CategoryModel;
import com.example.theshop.Model.ItemsModel;
import com.example.theshop.Model.OrderedProductsItem;
import com.example.theshop.Model.OrdersModelItem;
import com.example.theshop.Model.SliderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainViewModel extends ViewModel {

    private Context context;
    private MutableLiveData<List<SliderModel>> _slider = new MutableLiveData<>();
    private MutableLiveData<List<CategoryModel>> _category = new MutableLiveData<>();
    private MutableLiveData<List<ItemsModel>> _bestSeller = new MutableLiveData<>();
    private MutableLiveData<List<ItemsModel>> _search = new MutableLiveData<>();
    private MutableLiveData<List<OrdersModelItem>> _orders = new MutableLiveData<List<OrdersModelItem>>();

    private String baseUrl = BuildConfig.BASE_URL;


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
    public LiveData<List<ItemsModel>> getSearch(){return _search;}
    public LiveData<List<ItemsModel>> getBestSeller(){
        return  _bestSeller;
    }
    public LiveData<List<OrdersModelItem>> getOrders(){return _orders;}
    private boolean isLoading = false;
    public List<OrdersModelItem> itemModelList = new ArrayList<>();

    public void loadSlider(Context context){

        String url = baseUrl + "/api/v1/bannersliders";

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
        String url = BuildConfig.BASE_URL + "/api/v1/categories";

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

    public void loadBestSeller(Context context, int currentPage, int pageSize, boolean clearPrevious){
        System.out.println(currentPage);
        if (isLoading) return;
        isLoading = true;
        String url = baseUrl + "/api/v1/items?page=" + currentPage +"&size=" + pageSize;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("JSON Response", response.toString());
                try {
                    JSONArray products = response.getJSONArray("content");
                    List<ItemsModel> newItems = new ArrayList<>();
                    Set<Long> existingIds = new HashSet<>();

                    if (_bestSeller.getValue() != null){
                        for (ItemsModel item : _bestSeller.getValue()){
                            existingIds.add(item.getId());
                        }
                    }

                    for(int i = 0; i < products.length(); i++){
                        JSONObject itemsJson = products.getJSONObject(i);

                        ItemsModel itemsModel = new ItemsModel();
                        itemsModel.setId(itemsJson.getLong("product_id"));
                        itemsModel.setName(itemsJson.getString("product_name"));
                        itemsModel.setImageUrl(itemsJson.getString("image_url"));
                        itemsModel.setDescription(itemsJson.getString("product_description"));
                        itemsModel.setPrice(itemsJson.getDouble("product_price"));

                        if(!existingIds.contains(itemsModel.getId())) {
                            newItems.add(itemsModel);
                        }

                    }
                    if(clearPrevious)
                    if (_bestSeller.getValue() == null) {
                        _bestSeller.setValue(newItems);
                    } else {
                        List<ItemsModel> currentItems = new ArrayList<>(_bestSeller.getValue());
                        currentItems.addAll(newItems);
                        _bestSeller.setValue(currentItems);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.getMessage();
                    Toast.makeText(context, "Cannot retrieve image url", Toast.LENGTH_SHORT).show();
                } finally {
                    isLoading = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.getMessage();
                Toast.makeText(context, "Faileddd!!!", Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
        requestQueue.add(jsonArrayRequest);

    }


    public void loadSearch(Context context, String searchTerm, boolean clearPrevious){
        System.out.println(searchTerm);
        if (isLoading) return;
        isLoading = true;
        String url = baseUrl + "/api/v1/search?search=" + searchTerm;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON Response", response.toString());
                try {

                    List<ItemsModel> newItems = new ArrayList<>();
                    Set<Long> existingIds = new HashSet<>();

                    List<ItemsModel> searchValue = _search.getValue();
                    if (searchValue != null){
                        for (ItemsModel item : searchValue){
                            existingIds.add(item.getId());
                        }
                    }

                    for(int i = 0; i < response.length(); i++){
                        JSONObject itemsJson = response.getJSONObject(i);

                        ItemsModel itemsModel = new ItemsModel();
                        itemsModel.setId(itemsJson.getLong("product_id"));
                        itemsModel.setName(itemsJson.getString("productName"));
                        itemsModel.setImageUrl(itemsJson.getString("image_url"));
                        itemsModel.setDescription(itemsJson.getString("productDescription"));
                        itemsModel.setPrice(itemsJson.getDouble("product_price"));

                        if(!existingIds.contains(itemsModel.getId())) {
                            newItems.add(itemsModel);
                        }

                    }
                    if(clearPrevious)
                        if (_search.getValue() == null) {
                            _search.setValue(newItems);
                        } else {
                            List<ItemsModel> currentItems = new ArrayList<>(_search.getValue());
                            currentItems.addAll(newItems);
                            _search.setValue(currentItems);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.getMessage();
                    Toast.makeText(context, "Cannot retrieve image url", Toast.LENGTH_SHORT).show();
                } finally {
                    isLoading = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.getMessage();
                Toast.makeText(context, "Faileddd!!!", Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void loadOrders(Context context, String userId){
        String url = baseUrl + "/api/v1/orders/" + userId;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            Log.d("JsonResponse", response.toString());
            try {
                List<OrdersModelItem> ordersList = new ArrayList<>();

                // Iterate over each order object in the response
                for (int i = 0; i < response.length(); i++) {
                    JSONObject orderObject = response.getJSONObject(i);

                    // Extract basic order details
                    int orderId = orderObject.getInt("orderId");
                    String orderDate = orderObject.getString("orderDate");
                    int paymentId = orderObject.getInt("paymentId");
                    double amountPaid = orderObject.getDouble("amountPaid");

                    List<OrderedProductsItem> orderedProductsList = new ArrayList<>();

                    // Get the orderedProducts array
                    JSONArray orderedProductsArray = orderObject.getJSONArray("orderedProducts");

                    // Iterate over each product in orderedProducts
                    for (int j = 0; j < orderedProductsArray.length(); j++) {
                        JSONObject productObject = orderedProductsArray.getJSONObject(j);

                        int productId = productObject.getInt("productId");
                        int quantity = productObject.getInt("quantity");
                        String productName = productObject.getString("productName");
                        String imageUrl = productObject.getString("imageUrl");

                        // Create an OrderedProductsModel instance
                        OrderedProductsItem orderedProduct = new OrderedProductsItem(
                                productId, quantity, productName, imageUrl
                        );

                        // Add orderedProduct to the list
                        orderedProductsList.add(orderedProduct);
                    }

                    // Set the ordered products list to the current order
                    OrdersModelItem order = new OrdersModelItem(orderId, orderDate, paymentId, amountPaid, orderedProductsList);

                    // Add order to the main list
                    itemModelList.add(order);
                    _orders.setValue(itemModelList);
                }

                // Log the parsed orders for debugging purposes
                for (OrdersModelItem order : ordersList) {
                    Log.d("Order", order.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JsonParseError", "Error parsing order data", e);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", "Error loading orders", error);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
