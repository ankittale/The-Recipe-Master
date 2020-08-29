package com.ankittlabs.therecipemaster;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors instance;

    public static AppExecutors getInstance(){
        if(instance==null){
            instance=new AppExecutors();
        }
        return instance;
    }

    final ScheduledExecutorService mNetworkIO= Executors.newScheduledThreadPool(3);

    public ScheduledExecutorService networkIO(){
        return mNetworkIO;
    }
}
