package com.example.homework41;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homework41.databinding.FragmentHomeBinding;
import com.example.homework41.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private String verificationId;
    private OTP_Receiver otpReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonNumber();
        quit();
    }

    private void quit() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish();
                    }
                });
    }

    private void buttonNumber() {
        System.out.println("buttonNumber()");
        binding.btnSend.setOnClickListener(v -> {
            String data = Objects.requireNonNull(binding.tiedNumber.getText()).toString().trim();
            System.out.println("data   ---= = = " + data);
            if (TextUtils.isEmpty(data)) {
                binding.tilNumber.setError("Введите номер правильно");
                return;
            } else {
                binding.line1.setVisibility(View.GONE);
                binding.tilNumber.setError(null);
                binding.tilCode.setVisibility(View.VISIBLE);
            }
            binding.btnSend.setText("Отправить код");
            String finalNumber = "+996" + data;
            System.out.println("finalNumber----" + finalNumber + " country ");
            register(finalNumber.trim());
            checkingSMSCode("");
        });
    }

    public void checkingSMSCode(String otp) {
        otpAutoReceiver();
        binding.btnSend.setOnClickListener(v -> {
            String mOtp = Objects.requireNonNull(binding.tiedCode.getText()).toString().trim();
            System.out.println("Code   ----- " + otp);
            if (mOtp.isEmpty() || mOtp.length() < 6) {
                binding.tilCode.setError("Введите правильный код");
                binding.tiedCode.requestFocus();
                return;
            } else {
                binding.tilCode.setError(null);
                verifyCode(mOtp);
            }
        });
    }

    private void verifyCode(String mOtp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, mOtp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        showSnackbar("signInWithCredential:success");
                        NavController navController = Navigation.findNavController(LoginFragment.this.requireActivity(),
                                R.id.nav_host_fragment_activity_main);
                        navController.navigate(R.id.boardFragment);
                    } else {
                        showSnackbar("signInWithCredential:failure" + task.getException());
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                    super.onCodeAutoRetrievalTimeOut(s);
                    showSnackbar("super.onCodeAutoRetrievalTimeOut(s);-----" + s);
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    showSnackbar("onVerificationCompleted:" + phoneAuthCredential);
                    String code = phoneAuthCredential.getSmsCode();
                    showSnackbar("String code = phoneAuthCredential.getSmsCode();" + code);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        showSnackbar("onVerificationFailed " + e);
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        showSnackbar("onVerificationFailed " + e);
                    }
                    showSnackbar("onVerificationFailed " + e);

                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken
                        forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    showSnackbar("verificationId" + verificationId);
                    verificationId = s;
                    showSnackbar("onCodeSent:" + s);
                }
            };

    private void showSnackbar(String s) {
        System.out.println(s);
        Snackbar snackbar = Snackbar.make(requireView(), s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void register(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void otpAutoReceiver() {
        showSnackbar("otp------");
        otpReceiver = new OTP_Receiver();
        this.requireActivity().registerReceiver(otpReceiver, new IntentFilter(SmsRetriever
                .SMS_RETRIEVED_ACTION));
        otpReceiver.initListener(new OTP_Receiver.OtpReceiverListener() {
            @Override
            public void onOtpSuccess(String otp) {
                binding.tiedCode.setText(otp);
                showSnackbar("otp ---success---");
            }

            @Override
            public void otpTimeOut() {
                showSnackbar("something wroth with you");
            }
        });
    }

    private void resendForOtp(){
        buttonNumber();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (otpReceiver != null) {
            requireActivity().unregisterReceiver(otpReceiver);
        }
    }
}