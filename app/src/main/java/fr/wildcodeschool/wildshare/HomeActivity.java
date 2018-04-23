package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static ListAdapter mItemAdapter1;
    private static ListAdapter mItemAdapter2;
    private static ListAdapter mItemAdapter3;
    private static FriendListAdapter mFriendAdapter;
    private FirebaseAuth mAuth;
    private String mUid;
    private FirebaseDatabase mDatabase;
    private ImageView mIvProfilNav;
    private TextView mTvPseudoNav;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddItem.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_autoreniew);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_archive);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_rss);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_supervisor);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        View headerLayout = navigationView.getHeaderView(0);
        mDatabase = FirebaseDatabase.getInstance();
        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mIvProfilNav = (ImageView) headerLayout.findViewById(R.id.iv_profil_nav);
        mTvPseudoNav = (TextView) headerLayout.findViewById(R.id.tv_pseudo_nav);

        DatabaseReference pathID = mDatabase.getReference("User").child(mUid);

        pathID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("profilPic").getValue() != null)){
                    String url = dataSnapshot.child("profilPic").getValue(String.class);
                    Glide.with(HomeActivity.this).load(url).apply(RequestOptions.circleCropTransform()).into(mIvProfilNav);
                }

                if ((dataSnapshot.child("pseudo").getValue() != null)){
                    String pseudo = dataSnapshot.child("pseudo").getValue(String.class);
                    mTvPseudoNav.setText(pseudo);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profil) {
            startActivity(new Intent(HomeActivity.this, ProfilActivity.class));

        } else if (id == R.id.nav_deconnexion) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));



        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                final View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);


                final ListView lv1 = rootView.findViewById(R.id.lv_own_item_list);
                final ArrayList<ItemModel> itemData = new ArrayList<>();


                mItemAdapter1 = new ListAdapter(this.getActivity(), itemData, new ListAdapter.ItemClickListerner() {
                    @Override
                    public void onClick(ItemModel itemModel) {
                        Intent intent = new Intent(rootView.getContext(), ItemInfo.class);

                        startActivity(intent);
                    }
                });

                lv1.setAdapter(mItemAdapter1);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference itemRef = database.getReference("Item");
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                itemRef.orderByChild("ownerId").equalTo(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        itemData.clear();
                        for (DataSnapshot itemDataSnapshot : dataSnapshot.getChildren()) {
                            ItemModel itemModel = itemDataSnapshot.getValue(ItemModel.class);
                            itemData.add(new ItemModel(itemModel.getName(), itemModel.getImage()));
                        }
                        Collections.reverse(itemData);
                        mItemAdapter1.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                lv1.setAdapter(mItemAdapter1);
                SearchView searchView = rootView.findViewById(R.id.search_view_one);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        mItemAdapter1.getFilter().filter(newText);


                        return false;
                    }
                });
                return rootView;



            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                final View rootView = inflater.inflate(R.layout.fragment_two, container, false);

                ListView lv2 = rootView.findViewById(R.id.take_list);
                final ArrayList<ItemModel> itemData = new ArrayList<>();
                itemData.add(new ItemModel("ObjetTest5", null, "ownerProfilPic"));
                itemData.add(new ItemModel("ObjetTest6", null, "ownerProfilPic"));
                itemData.add(new ItemModel("ObjetTest7", null, "ownerProfilPic"));

                mItemAdapter2 = new ListAdapter(this.getActivity(), itemData, new ListAdapter.ItemClickListerner() {
                    @Override
                    public void onClick(ItemModel itemModel) {
                        Intent intent = new Intent(rootView.getContext(), ItemInfo.class);

                        startActivity(intent);
                    }
                });
                lv2.setAdapter(mItemAdapter2);
                SearchView searchView2 = rootView.findViewById(R.id.search_view_two);
                searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        mItemAdapter2.getFilter().filter(newText);


                        return false;
                    }
                });

                return rootView;
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                final View rootView = inflater.inflate(R.layout.fragment_three, container, false);

                ListView lv3 = rootView.findViewById(R.id.listView_wall);
                final ArrayList<ItemModel> itemData = new ArrayList<>();
                itemData.add(new ItemModel("ObjetTest5", null, "ownerProfilPic"));
                itemData.add(new ItemModel("ObjetTest6", null, "ownerProfilPic"));
                itemData.add(new ItemModel("ObjetTest7", null, "ownerProfilPic"));

                mItemAdapter3 = new ListAdapter(this.getActivity(), itemData, new ListAdapter.ItemClickListerner() {
                    @Override
                    public void onClick(ItemModel itemModel) {
                        Intent intent = new Intent(rootView.getContext(), ItemInfo.class);

                        startActivity(intent);
                    }
                });
                lv3.setAdapter(mItemAdapter3);
                SearchView searchView3 = rootView.findViewById(R.id.search_view_three);
                searchView3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        mItemAdapter3.getFilter().filter(newText);


                        return false;
                    }
                });

                return rootView;
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                final View rootView = inflater.inflate(R.layout.fragment_four, container, false);

                ListView lvFriends = rootView.findViewById(R.id.lv_friends);
                final ArrayList<FriendModel> friendData = new ArrayList<>();

                mFriendAdapter = new FriendListAdapter(this.getActivity(), friendData, new FriendListAdapter.FriendClickListerner() {
                    @Override
                    public void onClick(FriendModel friend) {
                        Intent intent = new Intent(rootView.getContext(), FriendItemsList.class);
                        startActivity(intent);
                    }
                });

                lvFriends.setAdapter(mFriendAdapter);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference userRef = database.getReference("User");
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        friendData.clear();
                        for (DataSnapshot friendDataSnapshot : dataSnapshot.getChildren()) {
                            FriendModel friendModel = friendDataSnapshot.getValue(FriendModel.class);
                            friendData.add(new FriendModel(friendModel.getPseudo(), friendModel.getProfilPic()));
                        }
                        Collections.reverse(friendData);
                        mFriendAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                SearchView searchView4 = rootView.findViewById(R.id.search_view_four);
                searchView4.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            mFriendAdapter.getFilter().filter(newText);


                            return false;
                        }
                    });


                return rootView;
            }

            return null;

        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}


