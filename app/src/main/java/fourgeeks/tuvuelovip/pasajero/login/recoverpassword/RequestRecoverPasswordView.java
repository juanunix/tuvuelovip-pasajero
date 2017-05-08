package fourgeeks.tuvuelovip.pasajero.login.recoverpassword;

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

import fourgeeks.tuvuelovip.pasajero.util.ViewTags;
import fourgeeks.tuvuelovip.pasajero.api.ApiResponseListener;
import fourgeeks.tuvuelovip.pasajero.login.RecoverPasswordModel;
import fourgeeks.tuvuelovip.pasajero.login.RecoverPasswordView;
import fourgeeks.tuvuelovip.pasajero.R;
import fourgeeks.tuvuelovip.pasajero.util.Util;


public class RequestRecoverPasswordView extends Fragment {

    private View view;
    private TextInputEditText emailTextInput;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_forgot, container, false);
        emailTextInput = (TextInputEditText) view.findViewById(R.id.email_text_forgot_view);

        Button forgotButton = (Button) view.findViewById(R.id.recover_button);
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestForgot();
            }
        });

        Button codeButton = (Button) view.findViewById(R.id.code_button);
        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();
            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void sendRequestForgot() {
        String email = emailTextInput.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(getActivity(), "Ingrese un correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Util.isEmailValid(email)) {
            Toast.makeText(getActivity(), "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        RecoverPasswordModel.requestRecoverPassword(email, new ApiResponseListener<String>() {
            @Override
            public void onResponse(String result) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("RequestRecoverPasswordV", "sendRequestForgot:onResponse:" + result);
                Util.addRollbackableTransaction(R.id.holder_content, getActivity().getSupportFragmentManager(), new RecoverPasswordView(), ViewTags.RECOVER_PASSWORD);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError(VolleyError error, String msg) {
                progressBar.setVisibility(View.VISIBLE);
                Log.e("RequestRecoverPasswordV", "sendRequestForgot:onErrorResponse:" + msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    public void recoverPassword() {
        Util.addRollbackableTransaction(R.id.holder_content, getActivity().getSupportFragmentManager(), new RecoverPasswordView(), ViewTags.RECOVER_PASSWORD);
    }
}
