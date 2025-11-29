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
 * MainActivity serves as the app's front door. It handles the initial splash screen, manages the
 * login form, and directs users to the right place after they authenticate.
 */
public class MainActivity extends AppCompatActivity {

    // region UI Components
    private TextView welcomeText, errorBanner;
    private CardView loginCard;
    private TextInputEditText accountInput, passwordInput;
    private Button loginButton;
    private TextView forgotPasswordText, signupText;
    private CheckBox rememberMeCheckbox;
    private ProgressBar loadingProgressBar;
    // endregion

    // region State & Data
    private SharedPreferences sharedPreferences;
    private boolean isDataReady = false; // A flag to know when our initial data is ready.
    private List<String> registeredUsers; // This is just a stand-in for a real user database.
    // endregion

    // SharedPreferences keys
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This is the modern way to handle splash screens. It will show until we tell it we're ready.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_main);

        // Wire up all the UI components from our XML layout.
        initializeViews();
        // Set up the login form UI to be ready to animate in.
        prepareLoginScreen();

        // This is the crucial part of the new API. The splash screen will stay visible
        // as long as this condition returns false.
        splashScreen.setKeepOnScreenCondition(() -> !isDataReady);

        // In a real app, this is where you'd load data from a database or network.
        // We'll simulate a 2-second delay.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isDataReady = true; // Signal that we're ready for the splash screen to go away.
            setupLoginForm();
            loadPreferences();
            registeredUsers = new ArrayList<>(Arrays.asList("test@example.com", "user@domain.com"));
            // The splash screen is now dismissing, so let's animate our login form in.
            animateLoginScreenIn();
        }, 2000);
    }

    /**
     * A helper method to keep the onCreate() clean. Just finds and assigns all our views.
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
     * Sets up all the logic for our login form.
     */
    private void setupLoginForm() {
        // The login button should start off disabled.
        loginButton.setEnabled(false);
        loginButton.setAlpha(0.5f);

        // This watcher lets us enable/disable the login button in real-time.
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
     * Checks SharedPreferences to see if we need to pre-fill the user's email.
     */
    private void loadPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean shouldRemember = sharedPreferences.getBoolean(PREF_REMEMBER, false);
        rememberMeCheckbox.setChecked(shouldRemember);

        if (shouldRemember) {
            String email = sharedPreferences.getString(PREF_EMAIL, "");
            accountInput.setText(email);
        }
    }

    /**
     * Saves the user's preference for the "Remember me" feature.
     */
    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberMeCheckbox.isChecked()) {
            editor.putString(PREF_EMAIL, Objects.requireNonNull(accountInput.getText()).toString());
            editor.putBoolean(PREF_REMEMBER, true);
        } else {
            editor.remove(PREF_EMAIL);
            editor.putBoolean(PREF_REMEMBER, false);
        }
        editor.apply();
    }

    /**
     * This is where the actual login magic happens.
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
     * A little trick to make parts of our TextViews act like hyperlinks.
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
     * Just a standard info dialog for the "forgot password" case.
     */
    private void showForgotPasswordDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Forgot Password")
                .setMessage("Please contact your administrator to reset your password.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Sets the initial state of the login UI (invisible and off-screen) before animating it in.
     */
    private void prepareLoginScreen() {
        welcomeText.setAlpha(0f);
        loginCard.setAlpha(0f);
        welcomeText.setTranslationY(200f);
        loginCard.setTranslationY(200f);
    }

    /**
     * Animates the login form components into view.
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
