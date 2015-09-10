package com.td.innovate.loanless;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ImSuperGreg on 2015-09-09.
 */
public class DebtStorage {
    private static String STORAGE_FILE_NAME = "LoanLessSharedPreferences";
    private static String BASE_JSON_LIST_NAME = "JSONDebtData";
    private static String BASE_JSON_PARENT_NAME = "LoanLessJSON";

    public DebtStorage() {
    }

    public static void storeDebtToSharedPrefs(Context context, ArrayList<Debt> data) {
        try {
            SharedPreferences sp = context.getSharedPreferences(STORAGE_FILE_NAME, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            //TODO: remove clear method for shared preferences
            editor.clear();

            //editor.putBoolean("mSilent", false); // if you want to use shared preferences normally, uncomment this line
            editor.commit(); // ALWAYS commit your change at the end to actually have it save

            int index = 0;
            JSONObject parent = new JSONObject();
            JSONArray jArray = new JSONArray();

            // Add a JSON object for each debt object
            for (Debt d : data) {
                jArray.put(d.getJSONObject());
            }

            parent.put(BASE_JSON_LIST_NAME, jArray);
            editor.putString(BASE_JSON_PARENT_NAME, parent.toString());
            editor.commit();

        } catch (Exception e) {

        }
    }

    public static ArrayList<Debt> getDebtFromSharedPrefs (Context context) {
        ArrayList<Debt> debtList = new ArrayList<Debt>();

        // TO READ FROM SHARED PREFERENCES
        SharedPreferences sp = context.getSharedPreferences(STORAGE_FILE_NAME, Context.MODE_PRIVATE);
        String rawJSONString = sp.getString(BASE_JSON_PARENT_NAME, null);

        try {
            if (rawJSONString == null)
                Log.v("[Debt Storage]", "ERROR: NO JSON FOUND!");

            // Get parent object, then pull the JSONArray stored beneath it (list of debt objects)
            JSONObject parent = new JSONObject(rawJSONString);

            JSONArray jArray = parent.getJSONArray(BASE_JSON_LIST_NAME);

            // Iterate over each object and construct a "Debt" object
            for(int i = 0; i < jArray.length(); i++) {
                Object debtObj = jArray.get(i);
                JSONObject test = new JSONObject(debtObj.toString());

                debtList.add(new Debt(test));
            }
            return debtList;
        }
        catch(Exception e) {
            e.printStackTrace();
            return new ArrayList<Debt>();
        }
    }
}
