package cme.pointmila.com;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by amoln on 12-09-2016.
 */
public class DoctorProfileFragment extends Fragment
{
    ViewPager viewPager;
    private int[] tabicon =
            {
                    R.drawable.down,
                    R.drawable.ic_drawer,
                    R.drawable.document
            };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, null);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        try {
           // tabLayout.getTabAt(0).setIcon(tabicon[0]);
            //tabLayout.getTabAt(1).setIcon(tabicon[1]);
            //tabLayout.getTabAt(2).setIcon(tabicon[2]);
              tabLayout.addTab(tabLayout.newTab().setText("Profile"));
              tabLayout.addTab(tabLayout.newTab().setText("Address"));
              tabLayout.addTab(tabLayout.newTab().setText("Academic Records"));
              tabLayout.addTab(tabLayout.newTab().setText("Additional Qualification"));

            viewPager = (ViewPager) view.findViewById(R.id.pager);
            ProfileAdapter adapter = new ProfileAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return view;
    }

    }
