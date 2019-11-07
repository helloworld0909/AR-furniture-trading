package edu.uci.cs297p.arfurniture;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import edu.uci.cs297p.arfurniture.buyer.BuyerFragment;
import edu.uci.cs297p.arfurniture.seller.SellerFragment;

public class TabAdapter extends FragmentStatePagerAdapter {
    private static int mNumTabs;

    public TabAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        mNumTabs = numTabs;
    }

    @Override
    public int getCount() {
        return mNumTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BuyerFragment();
            case 1:
                return new SellerFragment();
            default:
                return null;
        }
    }
}
