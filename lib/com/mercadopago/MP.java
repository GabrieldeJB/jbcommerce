package com.mercadopago;

import org.json.JSONObject;

public class MP {
    private String accessToken;

    public MP(String accessToken) {
        this.accessToken = accessToken;
    }

    public JSONObject createPreference(JSONObject preference) throws MPException {
        JSONObject response = new JSONObject();
        response.put("init_point", "https://sandbox.mercadopago.com/init_point_simulado");
        return response;
    }
}
