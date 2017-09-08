package com.teamerp.ipechackathon.ipechackathon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyam on 8/9/17.
 */

public class RouteDetails {
    List<String> summary=new ArrayList<String>(),time=new ArrayList<String>(),distance=new ArrayList<String>(),traffic=new ArrayList<String>();

    public RouteDetails(List<String> distance,List<String> time,List<String> summary,List<String> traffic){

        this.distance=distance;
        this.traffic=traffic;
        this.time=time;
        this.summary=summary;
    }

    public List<String> getSummary(){
        return summary;
    }
    public List<String> getDistance(){
        return distance;
    }
    public List<String> getTime(){
        return time;
    }
    public List<String> getTraffic(){
        return traffic;
    }

}
