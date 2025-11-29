package com.example.aaa;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.splashscreen.SplashScreen;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Serves as the app's front door, handling the splash screen, login, and initial user routing.
 */
public class MainActivity extends AppCompatActivity {

    private TextView welcomeText, errorBanner;
    private CardView loginCard;
    private TextInputEditText accountInput, passwordInput;
    private Button loginButton;
    private TextView forgotPasswordText, signupText;
    private CheckBox rememberMeCheckbox;
    private ProgressBar loadingProgressBar;

    private SharedPreferences sharedPreferences;
    private boolean isDataReady = false;
    private List<String> registeredUsers;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_ACCOUNT = "account";
    private static final String PREF_REMEMBER = "remember";

    /**
     * Kicks off the activity, setting up the splash screen and loading initial data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        prepareLoginScreen();

        splashScreen.setKeepOnScreenCondition(() -> !isDataReady);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isDataReady = true;
            setupLoginForm();
            loadPreferences();
            registeredUsers = new ArrayList<>(Arrays.asList("test@example.com", "user@domain.com"));
            animateLoginScreenIn();
        }, 2000);
    }

    /**
     * Connects all the UI components from the XML layout to our activity.
     */
    private void initializeViews() {
        welcomeText = findViewById(R.id.welcome_text);
        loginCard = findViewById(R.id.login_card);
        accountInput = findViewById(R.id.account_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordText = findViewById(R.id.forgot_password_text);
        signupText = findViewById(R.id.signup_text);
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox);
        loadingProgressBar = findViewById(R.id.loading_progressbar);
        errorBanner = findViewById(R.id.error_banner);
    }

    /**
     * Configures the listeners and initial state for the login form elements.
     */
    private void setupLoginForm() {
        loginButton.setEnabled(false);
        loginButton.setAlpha(0.5f);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (errorBanner.getVisibility() == View.VISIBLE) {
                    errorBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean accountFilled = !Objects.requireNonNull(accountInput.getText()).toString().trim().isEmpty();
                boolean passwordFilled = !Objects.requireNonNull(passwordInput.getText()).toString().trim().isEmpty();
                loginButton.setEnabled(accountFilled && passwordFilled);
                loginButton.setAlpha(accountFilled && passwordFilled ? 1.0f : 0.5f);
            }
        };

        accountInput.addTextChangedListener(textWatcher);
        passwordInput.addTextChangedListener(textWatcher);

        loginButton.setOnClickListener(v -> performLogin());
        makeLinksClickable();
    }

    /**
     * Loads the user's saved preferences, like their account and "remember me" choice.
     */
    private void loadPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean shouldRemember = sharedPreferences.getBoolean(PREF_REMEMBER, false);
        rememberMeCheckbox.setChecked(shouldRemember);

        if (shouldRemember) {
            String account = sharedPreferences.getString(PREF_ACCOUNT, "");
            accountInput.setText(account);
        }
    }

    /**
     * Saves the user's "remember me" preference and account details.
     */
    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberMeCheckbox.isChecked()) {
            editor.putString(PREF_ACCOUNT, Objects.requireNonNull(accountInput.getText()).toString());
            editor.putBoolean(PREF_REMEMBER, true);
        } else {
            editor.remove(PREF_ACCOUNT);
            editor.putBoolean(PREF_REMEMBER, false);
        }
        editor.apply();
    }

    /**
     * Validates user credentials and attempts to log them in.
     */
    private void performLogin() {
        String email = Objects.requireNonNull(accountInput.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordInput.getText()).toString();

        if (email.equals("admin") && password.equals("admin123")) {
            savePreferences();
            Toast.makeText(this, "Admin login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, StaffDashboardActivity.class);
            startActivity(intent);
        } else if (registeredUsers != null && registeredUsers.contains(email)) {
            errorBanner.setVisibility(View.GONE);
            loginButton.setText("");
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                loginButton.setText("Login");
                loadingProgressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                savePreferences();
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                // TODO: Navigate to the Guest Dashboard screen
            }, 2000);
        } else {
            errorBanner.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Makes the "Forgot Password" and "Sign up" text views behave like clickable links.
     */
    private void makeLinksClickable() {
        SpannableString forgotPasswordSpannable = new SpannableString(forgotPasswordText.getText());
        ClickableSpan forgotPasswordClick = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showForgotPasswordDialog();
            }
        };
        String forgotPasswordFullText = forgotPasswordText.getText().toString();
        int forgotStart = forgotPasswordFullText.indexOf("here");
        int forgotEnd = forgotStart + "here".length();
        forgotPasswordSpannable.setSpan(forgotPasswordClick, forgotStart, forgotEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgotPasswordText.setText(forgotPasswordSpannable);
        forgotPasswordText.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString signUpSpannable = new SpannableString(signupText.getText());
        ClickableSpan signUpClick = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        };
        String signUpFullText = signupText.getText().toString();
        int signUpStart = signUpFullText.indexOf("Sign up");
        int signUpEnd = signUpStart + "Sign up".length();
        signUpSpannable.setSpan(signUpClick, signUpStart, signUpEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupText.setText(signUpSpannable);
        signupText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Shows a simple dialog for users who have forgotten their password.
     */
    private void showForgotPasswordDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Forgot Password")
                .setMessage("Please contact your administrator to reset your password.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Hides the login UI elements to prepare for the entrance animation.
     */
    private void prepareLoginScreen() {
        welcomeText.setAlpha(0f);
        loginCard.setAlpha(0f);
        welcomeText.setTranslationY(200f);
        loginCard.setTranslationY(200f);
    }

    /**
     * Fades and slides the login form into view.
     */
    private void animateLoginScreenIn() {
        welcomeText.setVisibility(View.VISIBLE);
        loginCard.setVisibility(View.VISIBLE);

        ObjectAnimator welcomeTextAlpha = ObjectAnimator.ofFloat(welcomeText, View.ALPHA, 0f, 1f);
        ObjectAnimator welcomeTextTranslate = ObjectAnimator.ofFloat(welcomeText, View.TRANSLATION_Y, 200f, 0f);
        ObjectAnimator loginCardAlpha = ObjectAnimator.ofFloat(loginCard, View.ALPHA, 0f, 1f);
        ObjectAnimator loginCardTranslate = ObjectAnimator.ofFloat(loginCard, View.TRANSLATION_Y, 200f, 0f);

        AnimatorSet loginAnimatorSet = new AnimatorSet();
        loginAnimatorSet.playTogether(welcomeTextAlpha, welcomeTextTranslate, loginCardAlpha, loginCardTranslate);
        loginAnimatorSet.setDuration(500);
        loginAnimatorSet.start();
    }
}
