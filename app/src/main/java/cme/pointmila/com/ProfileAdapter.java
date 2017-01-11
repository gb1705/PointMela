package cme.pointmila.com;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by amoln on 12-09-2016.
 */
public class ProfileAdapter extends FragmentStatePagerAdapter
{
    int mNumofTabs;

    public ProfileAdapter(FragmentManager fm, int NumOfTabs)
    {
        super(fm);
        //this.mNumofTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position)
    {

        switch(position)
        {
            case 0:
                ProfileTabFragment tab1= new ProfileTabFragment();
                return tab1;

            case 1:
                AddressTabFragment tab2 = new AddressTabFragment();
                return tab2;
            case 2:
                AcademicRecordTabFragment tab3 = new AcademicRecordTabFragment();
                return tab3;
            case 3:
                AdditionalQualificationFragment tab4 = new AdditionalQualificationFragment();
                return tab4;

            default:
                return new ProfileTabFragment();
        }
    }

    @Override
    public int getCount()
    {
        return 4;
    }


}
