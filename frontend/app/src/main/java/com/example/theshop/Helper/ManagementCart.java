package com.example.theshop.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.theshop.Model.ItemsModel;

import java.util.ArrayList;

public class ManagementCart {
    private TinyDB tinyDB;
    private Context context;

    public ManagementCart(Context context) {
        tinyDB = new TinyDB(context);
        this.context = context;
    }

    public void inserItems(ItemsModel item){
        ArrayList<ItemsModel> listItems = getListCart();
        boolean existAlready = false;
        int index = -1;

        for (int i = 0; i < listItems.size(); i++){
            if (listItems.get(i).getName().equals(item.getName())){
                existAlready = true;
                index = i;
                break;
            }
        }

        if (existAlready){
            listItems.get(index).setNumberInCart(item.getNumberInCart());
        }
        else {
            listItems.add(item);
        }
        tinyDB.putListObject("CartList", listItems);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<ItemsModel> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener){
        if (listItems.get(position).getNumberInCart() == 1){
            listItems.remove(position);
        } else {
            listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }

    public void plusItems(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener){
        listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart());
        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }
    public double getTotalFee(){
        ArrayList<ItemsModel> listItems = getListCart();
        double fee = 0.0;
        for (ItemsModel item : listItems){
            fee += item.getPrice() * item.getNumberInCart();
        }
        return fee;
    }
}
