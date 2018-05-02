package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
    private static ListAdapter mUserItemsAdapter = null;
    private static ListAdapter mBorrowedItemsAdapter = null;
    private static ListAdapter mFriendsItemsAdapter = null;
    private static FriendListAdapter mFriendAdapter = null;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String mUserId;

    // listes mise à jour par Firebase
    private ArrayList<ItemModel> mItems = new ArrayList<>();
    private ArrayList<ItemModel> mBorrowed = new ArrayList<>();
    private ArrayList<FriendModel> mFriends = new ArrayList<>();
    private ArrayList<ItemModel> mFriendsItems = new ArrayList<>();

    private boolean mIsItemsLoaded = false;
    private boolean mIsBorrowedLoaded = false;
    private boolean mIsFriendItemsLoaded = false;
    private boolean mIsFriendsLoaded = false;

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
                    case 0:
                        HomeActivity.this.setTitle("My List");
                        loadItems();

                        break;
                    case 1:
                        HomeActivity.this.setTitle("Borrow");
                        loadBorrowed();

                        break;
                    case 2:
                        HomeActivity.this.setTitle("New Share");
                        loadFriendsItems();

                        break;
                    case 3:
                        HomeActivity.this.setTitle("FriendList");
                        loadFriends();
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

        // chargement du profil dans le menu
        final View headerLayout = navigationView.getHeaderView(0);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mUserId = mAuth.getCurrentUser().getUid();
            DatabaseReference userRef = mDatabase.getReference("User").child(mUserId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserModel user = dataSnapshot.child("Profil").getValue(UserModel.class);
                    if (user != null) {
                        TextView tvPseudoNav = headerLayout.findViewById(R.id.tv_pseudo_nav);
                        tvPseudoNav.setText(user.getPseudo());
                        ImageView ivProfilNav = headerLayout.findViewById(R.id.iv_profil_nav);
                        Glide.with(HomeActivity.this).load(user.getProfilPic()).apply(RequestOptions.circleCropTransform()).into(ivProfilNav);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        // charge la liste juste après la création de la page
        new Handler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        loadItems();
                    }
                }, 100);
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


                SearchView searchView = rootView.findViewById(R.id.search_view_one);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        if (mUserItemsAdapter != null) {
                            mUserItemsAdapter.getFilter().filter(newText);
                        }

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

                        if (mBorrowedItemsAdapter != null) {
                            mBorrowedItemsAdapter.getFilter().filter(newText);
                        }

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

                        if (mFriendsItemsAdapter != null) {
                            mFriendsItemsAdapter.getFilter().filter(newText);
                        }

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

                    public void openDialog() {
                        sListener.onDialog();
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

                        if (mFriendAdapter != null) {
                            mFriendAdapter.getFilter().filter(newText);
                        }

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
                    popupadd.setListener(new PopUpAddFriends.AddFriendListener() {
                        @Override
                        public void onError(String pseudo) {
                            Toast.makeText(HomeActivity.this, String.format(getString(R.string.pseudo_doesnt_exists), pseudo), Toast.LENGTH_SHORT).show();
                        }
                    });
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

        if (mIsBorrowedLoaded) {
            return;
        }
        mIsBorrowedLoaded = true;

        mBorrowedItemsAdapter = new ListAdapter(this, mBorrowed, "myBorrowed",
                new ListAdapter.ItemClickListerner() {
                    @Override
                    public void onClick(ItemModel itemModel) {

                        Intent intent = new Intent(HomeActivity.this, ItemInfo.class);
                        intent.putExtra("itemName", itemModel.getName());
                        startActivity(intent);
                    }
                });

        ListView lv2 = findViewById(R.id.take_list);
        lv2.setAdapter(mBorrowedItemsAdapter);

        DatabaseReference userRef = mDatabase.getReference("User").child(mUserId).child("Borrowed");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBorrowed.clear();
                for (DataSnapshot itemsDataSnapshot : dataSnapshot.getChildren()) {

                    final String itemId = itemsDataSnapshot.getKey();
                    String friendId = itemsDataSnapshot.getValue(String.class);

                    DatabaseReference friendRef = mDatabase.getReference("User").child(friendId);
                    friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FriendModel friend = dataSnapshot.child("Profil").getValue(FriendModel.class);

                            loadItem(itemId, friend.getProfilPic(), "borrowed");
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

    private void loadFriendsItems() {

        if (mIsFriendItemsLoaded) {
            return;
        }
        mIsFriendItemsLoaded = true;
        mFriendsItemsAdapter = new ListAdapter(this, mFriendsItems, "freeItem",
                new ListAdapter.ItemClickListerner() {
                    @Override
                    public void onClick(ItemModel itemModel) {

                        Intent intent = new Intent(HomeActivity.this, ItemInfo.class);
                        intent.putExtra("itemName", itemModel.getName());
                        startActivity(intent);
                    }
                });

        ListView lv3 = findViewById(R.id.listView_wall);
        lv3.setAdapter(mFriendsItemsAdapter);

        DatabaseReference userFriendsRef = mDatabase.getReference("User").child(mUserId).child("Friends");
        userFriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFriendsItems.clear();
                for (DataSnapshot friendsSnapshot : dataSnapshot.getChildren()) {

                    final String friendId = friendsSnapshot.getKey();
                    DatabaseReference friendRef = mDatabase.getReference("User").child(friendId);
                    friendRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FriendModel friend = dataSnapshot.child("Profil").getValue(FriendModel.class);

                            for (DataSnapshot itemsDataSnapshot : dataSnapshot.child("Item").getChildren()) {

                                String itemId = itemsDataSnapshot.getKey();
                                String value = itemsDataSnapshot.getValue(String.class);
                                if (value.equals("0")) {
                                    loadItem(itemId, friend.getProfilPic(), "friends");
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


    private void loadItems() {

        if (mIsItemsLoaded) {
            return;
        }
        mIsItemsLoaded = true;

        mUserItemsAdapter = new ListAdapter(this, mItems, "myItem",
                new ListAdapter.ItemClickListerner() {
                    @Override
                    public void onClick(ItemModel itemModel) {

                        Intent intent = new Intent(HomeActivity.this, ItemInfo.class);
                        intent.putExtra("itemName", itemModel.getName());
                        startActivity(intent);
                    }
                });

        final ListView lv1 = findViewById(R.id.lv_own_item_list);
        lv1.setAdapter(mUserItemsAdapter);

        DatabaseReference userRef = mDatabase.getReference("User").child(mUserId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mItems.clear();
                UserModel user = dataSnapshot.child("Profil").getValue(UserModel.class);
                for (DataSnapshot itemsDataSnapshot : dataSnapshot.child("Item").getChildren()) {

                    String itemId = itemsDataSnapshot.getKey();
                    loadItem(itemId, user.getProfilPic(), "user");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadItem(String itemId, final String profilePic, final String type) {

        DatabaseReference itemRef = mDatabase.getReference("Item").child(itemId);
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String itemId = dataSnapshot.getKey();
                ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);
                if (itemModel != null) {
                    itemModel.setItemId(itemId);
                    itemModel.setOwnerProfilPic(profilePic);
                    switch (type) {
                        case "user":
                            mItems.add(itemModel);
                            mUserItemsAdapter.notifyDataSetChanged();
                            break;
                        case "friends":
                            mFriendsItems.add(itemModel);
                            mFriendsItemsAdapter.notifyDataSetChanged();
                            break;
                        case "borrowed":
                            mBorrowed.add(itemModel);
                            mBorrowedItemsAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadFriends() {

        if (mIsFriendsLoaded) {
            return;
        }
        mIsFriendsLoaded = true;

        mFriendAdapter = new FriendListAdapter(this, mFriends, new FriendListAdapter.FriendClickListerner() {
            @Override
            public void onClick(FriendModel friend) {
                Intent intent = new Intent(HomeActivity.this, FriendItemsList.class);
                intent.putExtra("pseudo", friend.getPseudo());
                startActivity(intent);
            }
        });

        ListView lvFriends = findViewById(R.id.lv_friends);
        lvFriends.setAdapter(mFriendAdapter);

        DatabaseReference userFriendsRef = mDatabase.getReference("User").child(mUserId).child("Friends");
        userFriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFriends.clear();
                for (DataSnapshot friendsSnapshot : dataSnapshot.getChildren()) {

                    final String friendId = friendsSnapshot.getKey();
                    DatabaseReference friendProfileRef = mDatabase.getReference("User").child(friendId).child("Profil");
                    friendProfileRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FriendModel friend = dataSnapshot.getValue(FriendModel.class);
                            mFriends.add(friend);
                            mFriendAdapter.notifyDataSetChanged();
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


