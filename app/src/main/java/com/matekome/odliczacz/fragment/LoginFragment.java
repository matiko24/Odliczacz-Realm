package com.matekome.odliczacz.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.matekome.odliczacz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.radio_button_1)
    RadioButton radioButton1;
    @BindView(R.id.radio_button_2)
    RadioButton radioButton2;
    @BindView(R.id.radio_button_3)
    RadioButton radioButton3;
    @BindView(R.id.radio_button_4)
    RadioButton radioButton4;
    @BindView(R.id.btn_0)
    Button btn_0;
    @BindView(R.id.btn_1)
    Button btn_1;
    @BindView(R.id.btn_2)
    Button btn_2;
    @BindView(R.id.btn_3)
    Button btn_3;
    @BindView(R.id.btn_4)
    Button btn_4;
    @BindView(R.id.btn_5)
    Button btn_5;
    @BindView(R.id.btn_6)
    Button btn_6;
    @BindView(R.id.btn_7)
    Button btn_7;
    @BindView(R.id.btn_8)
    Button btn_8;
    @BindView(R.id.btn_9)
    Button btn_9;
    @BindView(R.id.btn_clear)
    Button btn_clear;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    SharedPreferences sharedPreferences;
    String password = "";
    OnLoginListener loginListener;

    public interface OnLoginListener {
        void login();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            loginListener = (OnLoginListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                if (password.length() > 0)
                    password = password.substring(0, password.length() - 1);
                break;
            case R.id.btn_ok:
                if (password.length() == 4) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("odliczacz.preferences", Context.MODE_PRIVATE);
                    boolean firstRun = sharedPreferences.getBoolean("firstRun", true);
                    if (firstRun) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("firstRun", false);
                        editor.putString("password", password);
                        editor.commit();
                        getActivity().finish();
                    } else
                        login(password);
                } else {
                    resetPasswordAndDotsWithMessage("Hasło powinno zawierać 4 cyfry");
                }
                break;
            default:
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("odliczacz.preferences", Context.MODE_PRIVATE);
                boolean firstRun = sharedPreferences.getBoolean("firstRun", true);
                Button button = (Button) view;
                String buttonText = button.getText().toString();
                password += buttonText;
                if (password.length() == 4)
                    if (!firstRun)
                        login(password);
                break;
        }

        setDots(password.length());
    }

    private void resetPasswordAndDotsWithMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        password = "";
        setDots(0);
    }

    private void login(String password) {
        if (checkPassword(password)) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("odliczacz.preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLogin", true);
            editor.commit();
            loginListener.login();
        } else {
            resetPasswordAndDotsWithMessage("Błędne hasło");
        }
    }

    private boolean checkPassword(String password) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("odliczacz.preferences", Context.MODE_PRIVATE);
        String correctPassword = sharedPreferences.getString("password", "0");
        boolean passwordCorrect = correctPassword.equals(password);
        return passwordCorrect;
    }

    public void setDots(int passwordLength) {
        switch (passwordLength) {
            case 0:
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                radioButton4.setChecked(false);
                break;
            case 1:
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                radioButton4.setChecked(false);
                break;
            case 2:
                radioButton1.setChecked(true);
                radioButton2.setChecked(true);
                radioButton3.setChecked(false);
                radioButton4.setChecked(false);
                break;
            case 3:
                radioButton1.setChecked(true);
                radioButton2.setChecked(true);
                radioButton3.setChecked(true);
                radioButton4.setChecked(false);
                break;
            default:
                radioButton1.setChecked(true);
                radioButton2.setChecked(true);
                radioButton3.setChecked(true);
                radioButton4.setChecked(true);
                break;
        }
    }
}
