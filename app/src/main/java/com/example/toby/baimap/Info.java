package com.example.toby.baimap;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toby on 2017/4/9.
 */

public class Info extends AppCompatActivity implements Serializable
{
    private static final long serialVersionUID = -1010711775392052966L;
    private double latitude;
    private double longitude;
    private int imgId;
    private String name;
    private String distance;
    private String zan;
    private String user;
    //private static Dao dap = new Dao();
    //public static Data count = ;

    public  static List<Info> infos = new ArrayList<Info>();


    static
    {
        //count = (Data) getApplication();
        infos.add(new Info(39.9772,116.424472, R.drawable.car1, "化大停车场",//004
                "距离1000米", 10+"","004"));
        infos.add(new Info(39.981719,116.424561, R.drawable.car2, "樱花停车",//003
                "距离800米", 12+"","003"));
        infos.add(new Info(39.975734,116.431407, R.drawable.car3, "和平东桥停车场",//002
                "距离500米", 2+"","002"));
        infos.add(new Info(39.981567,116.431011, R.drawable.car4, "易停停车场",//001
                "距离200km", 2+"","001" ));
    }//改动了它的类型，zan的

    public Info(double latitude, double longitude, int imgId, String name,
                String distance, String zan,String user)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgId = imgId;
        this.name = name;
        this.distance = distance;
        this.zan = zan;
        this.user=user;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public int getImgId()
    {
        return imgId;
    }

    public void setImgId(int imgId)
    {
        this.imgId = imgId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDistance()
    {
        return distance;
    }

    public void setDistance(String distance)
    {
        this.distance = distance;
    }

    public String getZan()
    {
        return zan;
    }

    public void setZan(String zan)
    {
        this.zan = zan;
    }
    public String getUser()
    {
    return user;
}
    public void setUser(String user)
    {
    this.user=user;
    }

}