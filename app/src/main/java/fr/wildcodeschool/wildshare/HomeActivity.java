package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.widget.AdapterView;
import android.widget.Button;
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
    private ImageView mbuttonTake;
    private ImageView mbuttonGiveBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_face_white);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_priority_high_white);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_rss);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_group_add_white);


        this.setTitle("My List");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0 :
                        HomeActivity.this.setTitle("My List");

                        break;
                    case 1 :
                        HomeActivity.this.setTitle("Borrow");
                        loadBorrowed();

                        break;
                    case 2 :
                        HomeActivity.this.setTitle("New Share");
                        loadFreeItem();

                        break;
                    case 3 :
                        HomeActivity.this.setTitle("FriendList");

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

                if ((dataSnapshot.child("Profil").child("profilPic").getValue() != null)){
                    String url = dataSnapshot.child("Profil").child("profilPic").getValue(String.class);
                    Glide.with(getApplicationContext()).load(url).apply(RequestOptions.circleCropTransform()).into(mIvProfilNav);
                }

                if ((dataSnapshot.child("Profil").child("pseudo").getValue() != null)){
                    String pseudo = dataSnapshot.child("Profil").child("pseudo").getValue(String.class);
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
        private static DialogListener sListener;

        public PlaceholderFragment() {
        }

        public void setListener(DialogListener listener) {
            sListener = listener;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, DialogListener listener) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setListener(listener);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // ONGLET 1
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                final View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);

                FloatingActionButton fab = rootView.findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(rootView.getContext(), AddItem.class);
                        startActivity(intent);
                    }
                });


                final ListView lv1 = rootView.findViewById(R.id.lv_own_item_list);
                final ArrayList<ItemModel> itemData = new ArrayList<>();


                mItemAdapter1 = new ListAdapter(this.getActivity(), itemData, "myItem", new ListAdapter.ItemClickListerner() {
                    @Override
                    public void onClick(ItemModel itemModel) {

                        Intent intent = new Intent(rootView.getContext(), ItemInfo.class);
                        intent.putExtra("itemName", itemModel.getName());
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


                // ONGLET 2
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                final View rootView = inflater.inflate(R.layout.fragment_two, container, false);

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


                // ONGLET 3
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                final View rootView = inflater.inflate(R.layout.fragment_three, container, false);

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

                // ONGLET 4
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                final View rootView = inflater.inflate(R.layout.fragment_four, container, false);

                FloatingActionButton fab = rootView.findViewById(R.id.fab2);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialog();
                    }
                    public void openDialog(){
                        sListener.onDialog();
                    }

                });

                ListView lvFriends = rootView.findViewById(R.id.lv_friends);
                final ArrayList<FriendModel> friendData = new ArrayList<>();


                mFriendAdapter = new FriendListAdapter(this.getActivity(), friendData, new FriendListAdapter.FriendClickListerner() {
                    @Override
                    public void onClick(FriendModel friend) {
                        Intent intent = new Intent(rootView.getContext(), FriendItemsList.class);
                        intent.putExtra("pseudo", friend.getPseudo());
                        startActivity(intent);
                    }
                });

                lvFriends.setAdapter(mFriendAdapter);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference userRef = database.getReference("User");
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference myFriendsRef = database.getReference("User").child(uid).child("Friends");

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        friendData.clear();
                        for (final DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                            myFriendsRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot friendDataSnapshot : dataSnapshot.getChildren()) {
                                        if (userDataSnapshot.getKey().toString().equals(friendDataSnapshot.getKey().toString())) {
                                            FriendModel friendModel = userDataSnapshot.child("Profil").getValue(FriendModel.class);
                                            friendData.add(new FriendModel(friendModel.getPseudo(), friendModel.getProfilPic()));

                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
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

        public interface DialogListener {

            void onDialog();
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
            return PlaceholderFragment.newInstance(position + 1, new PlaceholderFragment.DialogListener() {
                @Override
                public void onDialog() {
                    PopUpAddFriends popupadd = new PopUpAddFriends();
                    popupadd.show(getSupportFragmentManager(), "PopUpAddFriends");
                }
            });
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }



    private void loadBorrowed() {

        final ArrayList<ItemModel> itemData = new ArrayList<>();

        mItemAdapter2 = new ListAdapter(this, itemData, "myBorrowed", new ListAdapter.ItemClickListerner() {
            @Override
            public void onClick(ItemModel itemModel) {

                Intent intent = new Intent(HomeActivity.this, ItemInfo.class);
                intent.putExtra("itemName", itemModel.getName());
                startActivity(intent);
            }
        });

        ListView lv2 = this.findViewById(R.id.take_list);

        lv2.setAdapter(mItemAdapter2);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("User");
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference friendRef = mDatabase.getReference("User").child(userId).child("Friends");
        final DatabaseReference itemRef = mDatabase.getReference("Item");



        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot friendDataSnapshot : dataSnapshot.getChildren()) {

                    final String friendId = friendDataSnapshot.getKey();
                    final DatabaseReference friendItemRef = mDatabase.getReference("User").child(friendId).child("Item");

                    friendItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            itemData.clear();
                            for (DataSnapshot friendItemDataSnapshot : dataSnapshot.getChildren()) {

                                final String itemId = friendItemDataSnapshot.getKey();
                                final String itemValue = friendItemDataSnapshot.getValue(String.class);

                                if (itemValue.equals(userId)) {

                                    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final ItemModel itemModel = dataSnapshot.child(itemId).getValue(ItemModel.class);
                                            userRef.child(friendId).child("Profil").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    UserModel userModel = dataSnapshot.getValue(UserModel.class);

                                                    itemModel.setOwnerProfilPic(userModel.getProfilPic());
                                                    itemData.add(itemModel);

                                                    mItemAdapter2.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void loadFreeItem() {

        ListView lv3 = this.findViewById(R.id.listView_wall);
        final ArrayList<ItemModel> itemData = new ArrayList<>();

        mItemAdapter3 = new ListAdapter(this, itemData, "freeItem", new ListAdapter.ItemClickListerner() {
            @Override
            public void onClick(ItemModel itemModel) {

                Intent intent = new Intent(HomeActivity.this, ItemInfo.class);
                intent.putExtra("itemName", itemModel.getName());
                startActivity(intent);
            }
        });

        lv3.setAdapter(mItemAdapter3);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference itemRef = database.getReference("Item");
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userRef = database.getReference("User");
        final DatabaseReference myFriendsRef = database.getReference("User").child(uid).child("Friends");


        myFriendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemData.clear();


                for (DataSnapshot myFriendDataSnapshot : dataSnapshot.getChildren()) {
                    final String friendId = myFriendDataSnapshot.getKey();

                    userRef.child(friendId).child("Item").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot myFriendItemDataSnapshot : dataSnapshot.getChildren()) {
                                if (myFriendItemDataSnapshot.getValue().toString().equals("0")) {
                                    String itemId = myFriendItemDataSnapshot.getKey();
                                    itemRef.child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);

                                            userRef.child(friendId).child("Profil").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                                    itemModel.setOwnerProfilPic(userModel.getProfilPic());
                                                    itemData.add(itemModel);

                                                    mItemAdapter3.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


