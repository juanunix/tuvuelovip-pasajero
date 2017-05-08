package fourgeeks.tuvuelovip.pasajero.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;

import fourgeeks.tuvuelovip.pasajero.util.Util;
import fourgeeks.tuvuelovip.pasajero.util.ViewTags;
import fourgeeks.tuvuelovip.pasajero.api.ApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.R;



public class RecoverPasswordView extends Fragment {

    private View view;
    private ProgressBar progressBar;
    private Button recoverButton;
    private TextInputEditText codeText, passwordText, confirPasswordText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null; // now cleaning up!
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_recover, container, false);
        codeText = (TextInputEditText) view.findViewById(R.id.code_text_recover_view);
        passwordText = (TextInputEditText) view.findViewById(R.id.password_text_recover_view);
        confirPasswordText = (TextInputEditText) view.findViewById(R.id.confirm_password_text_recover_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_recover_view);
        recoverButton = (Button) view.findViewById(R.id.recover_button_recover_view);
        //
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();
            }
        };

        recoverButton.setOnClickListener(listener);

    }

    private void recoverPassword() {
        String code = codeText.getText().toString();

        if(code.isEmpty()){
            Toast.makeText(getActivity(),"Ingrese el código enviado al correo electrónico",Toast.LENGTH_SHORT).show();
            //TuVueloVipDialog.showDialog(this, "Ingrese el código enviado al correo electrónico");
            return;
        }

        String password = passwordText.getText().toString();
        if(password.isEmpty()){
            Toast.makeText(getActivity(),"Ingrese la nueva contraseña",Toast.LENGTH_SHORT).show();
            //TuVueloVipDialog.showDialog(this, "Ingrese la nueva contraseña");
            return;
        }

        String confirmPassword = confirPasswordText.getText().toString();
        if(password.isEmpty()){
            Toast.makeText(getActivity(),"Confirme la nueva contraseña",Toast.LENGTH_SHORT).show();
            //TuVueloVipDialog.showDialog(this, "Confirme la nueva contraseña");
            return;
        }

        if(!password.equals(confirmPassword)){
            Toast.makeText(getActivity(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            //TuVueloVipDialog.showDialog(this, "Las contraseñas no coinciden");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        RecoverPasswordModel.recoverPassword(code, password, new ApiResponseListener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d("FORGOTPASSWORDVIEW", "forgotPassword:onResponse:" +result);
                Toast.makeText(getActivity(), "Contraseña cambiada con exito", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Util.addRollbackableTransaction(R.id.holder_content, RecoverPasswordView.this.getFragmentManager(), new LoginView(), ViewTags.LOGIN);
            }

            @Override
            public void onError(VolleyError error, String msg) {
                Log.e("FORGOTPASSWORDVIEW", "forgotPassword:onErrorResponse:" + msg);
                Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
