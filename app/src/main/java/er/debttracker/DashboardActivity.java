package er.debttracker;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import fragments.CreditsFragment;
import fragments.DebtsFragment;
import fragments.NewDebtFragment;
import fragments.SettingsFragment;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //loading the default home fragment
        loadFragment(new DebtsFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    // Switch between fragment displays depending on the nav item selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_debts:
                fragment = new DebtsFragment();
                break;

            case R.id.nav_credits:
                fragment = new CreditsFragment();
                break;

            case R.id.nav_add_debt:
                fragment = new NewDebtFragment();
                break;

            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
