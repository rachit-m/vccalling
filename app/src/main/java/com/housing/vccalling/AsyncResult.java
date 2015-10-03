package com.housing.vccalling;

import org.json.JSONObject;

interface AsyncResult {
    void onResult(JSONObject object);
}