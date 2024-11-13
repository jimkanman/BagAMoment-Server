package com.jkm.jimkanman.util;

import com.jkm.jimkanman.domain.Point;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class GeometryUtilImpl implements GeometryUtil {
    private final String apiKey;
    GeometryUtilImpl(@Value("${api-keys.vworld}") String apiKey) {
        this.apiKey = apiKey;
        System.out.println("GeometryUtilImpl initialized with api key " + apiKey );
    }

    @Override
    public Point convertToPoint(String address) {

        // TODO
        /* Java 코드 사용예제 */
        String searchType = "parcel";
        String searchAddr = "삼평동 624";
        String epsg = "epsg:4326";

        StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/address");
        sb.append("?service=address");
        sb.append("&request=getCoord");
        sb.append("&format=json");
        sb.append("&crs=" + epsg);
        sb.append("&key=" + apiKey);
        sb.append("&type=" + searchType);
        sb.append("&address=" + URLEncoder.encode(searchAddr, StandardCharsets.UTF_8));

        /*
        try{
            URL url = new URL(sb.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            JSONParser jspa = new JSONParser();
            JSONObject jsob = (JSONObject) jspa.parse(reader);
            JSONObject jsrs = (JSONObject) jsob.get("response");
            JSONObject jsResult = (JSONObject) jsrs.get("result");
            JSONObject jspoitn = (JSONObject) jsResult.get("point");

            System.out.println(jspoitn.get("x"));
            System.out.println(jspoitn.get("y"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
         */


        return null;
    }
}
