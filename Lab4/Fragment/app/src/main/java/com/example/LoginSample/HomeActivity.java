package com.example.LoginSample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.LoginSample.fragments.CuadrosFragment;
import com.example.LoginSample.fragments.HomeFragment;
import com.example.LoginSample.fragments.MapaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager  fragmentManager = null ;

    private FragmentTransaction fragmentTransaction = null ;

    private HomeFragment homeFragment= null;

    private CuadrosFragment cuadrosFragment = null ;
    private MapaFragment mapaFragment = null ;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //String accountEntity = getIntent().getStringExtra("ACCOUNT");
        //Log.d("HomeActivity", accountEntity);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_home){
                    homeFragment = HomeFragment.newInstance("", "");
                    loadFragment(homeFragment);
                    return  true ;
                } else if (menuItem.getItemId() == R.id.menu_cuadros) {
                    cuadrosFragment = CuadrosFragment.newInstance("","");
                    loadFragment(cuadrosFragment);
                    return  true ;

                } else if (menuItem.getItemId() == R.id.menu_mapa) {
                    mapaFragment = mapaFragment.newInstance("","");
                    loadFragment(mapaFragment);
                    return  true ;

                }else{
                    return false;
                }



            }
        });
    }

    private void loadFragment (Fragment fragment){
        if( fragmentManager !=null ){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
            fragmentTransaction.commit();
        }
    }

}
