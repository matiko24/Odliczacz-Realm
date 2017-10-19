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
    @BindView(R.id.fragment_login_rb_password_1)
    RadioButton radioButton1;
    @BindView(R.id.fragment_login_rb_password_2)
    RadioButton radioButton2;
    @BindView(R.id.fragment_login_rb_password_3)
    RadioButton radioButton3;
    @BindView(R.id.fragment_login_rb_password_4)
    RadioButton radioButton4;
    @BindView(R.id.fragment_login_btn_0)
    Button btn_0;
    @BindView(R.id.fragment_login_btn_1)
    Button btn_1;
    @BindView(R.id.fragment_login_btn_2)
    Button btn_2;
    @BindView(R.id.fragment_login_btn_3)
    Button btn_3;
    @BindView(R.id.fragment_login_btn_4)
    Button btn_4;
    @BindView(R.id.fragment_login_btn_5)
    Button btn_5;
    @BindView(R.id.fragment_login_btn_6)
    Button btn_6;
    @BindView(R.id.fragment_login_btn_7)
    Button btn_7;
    @BindView(R.id.fragment_login_btn_8)
    Button btn_8;
    @BindView(R.id.fragment_login_btn_9)
    Button btn_9;
    @BindView(R.id.fragment_login_btn_clear)
    Button btn_clear;
    @BindView(R.id.fragment_login_btn_ok)
    Button btn_ok;
    String stringToCompareWithPassword = "";
    String password;
    OnLoginListener loginListener;
    boolean firstRun;

    public interface OnLoginListener {
        void logIn(String password);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            loginListener = (OnLoginListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("odliczacz.preferences", Context.MODE_PRIVATE);
        firstRun = sharedPreferences.getBoolean("firstRun", true);
        password = sharedPreferences.getString("password", null);
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
            case R.id.fragment_login_btn_clear:
                if (stringToCompareWithPassword.length() > 0)
                    stringToCompareWithPassword = stringToCompareWithPassword.substring(0, stringToCompareWithPassword.length() - 1);
                break;
            case R.id.fragment_login_btn_ok:
                if (stringToCompareWithPassword.length() == 4) {
                    tryToLogIn(stringToCompareWithPassword);
                } else {
                    resetPasswordWithMessage(getString(R.string.wrong_password_length_message));
                }
                break;
            default:
                Button button = (Button) view;
                String buttonText = button.getText().toString();

                stringToCompareWithPassword += buttonText;
                if (stringToCompareWithPassword.length() == 4)
                    if (!firstRun)
                        tryToLogIn(stringToCompareWithPassword);
                break;
        }
        setDots(stringToCompareWithPassword.length());
    }

    private void tryToLogIn(String password) {
        if (firstRun || checkPassword(password)) {
            loginListener.logIn(password);
        } else {
            resetPasswordWithMessage("Błędne hasło");
        }
    }

    private boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    private void resetPasswordWithMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        stringToCompareWithPassword = "";
        setDots(0);
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
