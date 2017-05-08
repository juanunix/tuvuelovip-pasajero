package fourgeeks.tuvuelovip.pasajero;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import fourgeeks.tuvuelovip.pasajero.login.InstallView;
import fourgeeks.tuvuelovip.pasajero.login.LoginView;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Cache;
import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.util.ViewTags;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_content);

        if(Cache.isAppInstalled()) {
            Util.addNonRollbackableTransaction(R.id.holder_content, getSupportFragmentManager(), new LoginView());
        }else{
            Util.addNonRollbackableTransaction(R.id.holder_content, getSupportFragmentManager(), new InstallView());
        }
    }

}