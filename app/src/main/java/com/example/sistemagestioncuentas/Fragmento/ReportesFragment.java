package com.example.sistemagestioncuentas.Fragmento;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sistemagestioncuentas.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.data.BarData;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ReportesFragment extends Fragment {
    //reportes
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private  View ReportesView;
    public ReportesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ReportesView= inflater.inflate(R.layout.fragment_reportes, container, false);
        tabLayout=(TabLayout) ReportesView.findViewById(R.id.tablayout_reportes);
        viewPager = (ViewPager)ReportesView.findViewById(R.id.viewpager_reportes);
        ViewPagerAdapter adapter =new ViewPagerAdapter(getFragmentManager());
        adapter.AddFragment(new EgresosReportesFragment(), "Egresos");
        adapter.AddFragment(new IngresosReportesFragment(), "Ingresos");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return ReportesView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
