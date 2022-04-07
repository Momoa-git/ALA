package com.example.ala.Inventory;

import com.example.ala.R;

import java.util.ArrayList;
import java.util.List;

public class StatusData {

    public static List<Status> getStatusList(){
        List<Status> statusList = new ArrayList<>();

        Status All = new Status();
        All.setName("Všechny");
        statusList.add(All);

        Status Pending = new Status();
        Pending.setName("Čekající");
        Pending.setStatus_bar(R.drawable.status_bar_pe);
        statusList.add(Pending);

        Status InProgress = new Status();
        InProgress.setName("Vyřizuje se");
        InProgress.setStatus_bar(R.drawable.status_bar_ip);
        statusList.add(InProgress);

        Status Completed = new Status();
        Completed.setName("Vyřízena");
        Completed.setStatus_bar(R.drawable.status_bar_co);
        statusList.add(Completed);

        Status Canceled = new Status();
        Canceled.setName("Stornována");
        Canceled.setStatus_bar(R.drawable.status_bar_ca);
        statusList.add(Canceled);

     return statusList;

    }
}
