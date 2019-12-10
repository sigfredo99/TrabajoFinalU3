package com.example.sistemagestioncuentas.Fragmento;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sistemagestioncuentas.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class EgresosReportesFragment extends Fragment {
    View view;
    private static String TAG = "MainActivity";
    private float rainfall[] = {25.3f, 10.6f, 66.76f, 44.32f, 46.01f, 16.89f, 23.9f};
    private String xData[]  = {"Mitch", "Jessica" , "Mohammad" , "Kelsey", "Sam", "Robert", "Ashley"};
    PieChart pieChart;
    public EgresosReportesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_egresos_reportes, container, false);

        Log.d(TAG, "onCreate: starting to create chart");

        pieChart = (PieChart) view.findViewById(R.id.idPieChart);
        addDataSet();
        // drawChart();
        return view;
    }

    private void addDataSet() {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for(int i=0; i<rainfall.length;i++)
        {
            pieEntries.add(new PieEntry(rainfall[i],xData[i]));
        }
        PieDataSet dataset = new PieDataSet(pieEntries,"Rainfall for Vancouver");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataset);

        pieChart.setData(data);
        pieChart.invalidate();

    }
}
