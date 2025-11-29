package com.example.aaa;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * The SignUpActivity is where new users can create an account.
 * This screen includes real-time validation on the input fields and a password strength
 * meter to guide the user.
 */
public class SignUpActivity extends AppCompatActivity {

    // region UI Components
    private TextInputLayout emailLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText emailInput, passwordInput, confirmPasswordInput;
    private Button signupButton;
    private ProgressBar loadingProgressBar, passwordStrengthBar;
    private TextView passwordStrengthText;
    private LinearLayout passwordStrengthIndicator;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This custom animation makes the screen slide up from the bottom.
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        setContentView(R.layout.activity_sign_up);

        initializeViews();
        setupValidation();
        setupClickListeners();
    }

    /**
     * By overriding finish(), we can apply a custom animation when the user
     * leaves this screen, making it feel like it's sliding back down.
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
    }

    /**
     * A helper to keep onCreate() tidy. It just finds and assigns all our views.
     */
    private void initializeViews() {
        emailLayout = findViewById(R.id.email_input_layout);
        passwordLayout = findViewById(R.id.password_input_layout);
        confirmPasswordLayout = findViewById(R.id.confirm_password_input_layout);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);

        signupButton = findViewById(R.id.signup_button);
        loadingProgressBar = findViewById(R.id.loading_progressbar);

        passwordStrengthIndicator = findViewById(R.id.password_strength_indicator);
        passwordStrengthBar = findViewById(R.id.password_strength_bar);
        passwordStrengthText = findViewById(R.id.password_strength_text);
    }

    /**
     * This is where we set up all the real-time checks for the form fields.
     */
    private void setupValidation() {
        // This watcher is used on most fields to check the button state after any text change.
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Not needed */ }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { /* Not needed */ }

            @Override
            public void afterTextChanged(Editable s) {
                validateAllFields();
            }
        };

        emailInput.addTextChangedListener(textWatcher);
        confirmPasswordInput.addTextChangedListener(textWatcher);

        // The password field gets a special watcher to update the strength meter.
        passwordInput.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Not needed */ }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) { /* Not needed */ }

             @Override
             public void afterTextChanged(Editable s) {
                 updatePasswordStrength(s.toString());
                 validateAllFields();
             }
         });

        // We use a focus change listener to avoid showing errors while the user is still typing.
        // The error will only appear after they move to the next field.
        View.OnFocusChangeListener focusChangeListener = (v, hasFocus) -> {
            if (!hasFocus) {
                // The view's parent's parent is the TextInputLayout, which is what we want to validate.
                validateField((View) v.getParent().getParent());
            }
        };
        emailInput.setOnFocusChangeListener(focusChangeListener);
        passwordInput.setOnFocusChangeListener(focusChangeListener);
        confirmPasswordInput.setOnFocusChangeListener(focusChangeListener);
    }

    /**
     * Central place to set up our click listeners.
     */
    private void setupClickListeners() {
        signupButton.setOnClickListener(v -> performSignUp());

        TextView loginPromptText = findViewById(R.id.login_prompt_text);
        makeLoginPromptClickable(loginPromptText);
    }

    /**
     * Updates the password strength meter based on the current password.
     * @param password The text from the password input field.
     */
    private void updatePasswordStrength(String password) {
        if (password.isEmpty()) {
            passwordStrengthIndicator.setVisibility(View.GONE);
            return;
        }

        passwordStrengthIndicator.setVisibility(View.VISIBLE);
        int score = calculatePasswordStrength(password);
        passwordStrengthBar.setProgress(score);

        // Adjust the color and text of the indicator based on the score.
        if (score < 30) {
            passwordStrengthText.setText("Weak");
            passwordStrengthBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        } else if (score < 70) {
            passwordStrengthText.setText("Medium");
            passwordStrengthBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(255, 165, 0))); // Orange
        } else {
            passwordStrengthText.setText("Strong");
            passwordStrengthBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }
    }

    /**
     * A simple algorithm to score a password.
     * @param password The password to check.
     * @return A score between 0 and 100.
     */
    private int calculatePasswordStrength(String password) {
        int score = 0;
        if (password.length() >= 8) score += 25;
        if (password.matches(".*[0-9].*")) score += 25;
        if (password.matches(".*[A-Z].*")) score += 25;
        if (password.matches(".*[@#$%^&+=].*")) score += 25;
        return score;
    }

    /**
     * Makes the "Log in" portion of the text clickable, allowing the user to easily
     * return to the login screen.
     * @param textView The TextView that contains the prompt.
     */
    private void makeLoginPromptClickable(TextView textView) {
        SpannableString spannableString = new SpannableString(textView.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                finish(); // Just close this screen.
            }
        };

        String fullText = textView.getText().toString();
        String linkText = "Log in";
        int start = fullText.indexOf(linkText);
        int end = start + linkText.length();

        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance()); // Makes the link work.
    }

    /**
     * A helper that delegates to the correct validation method based on the view.
     * @param textInputLayout The view to check.
     * @return true if valid, false otherwise.
     */
    private boolean validateField(View textInputLayout) {
        if (textInputLayout == emailLayout) return validateEmail();
        if (textInputLayout == passwordLayout) return validatePassword();
        if (textInputLayout == confirmPasswordLayout) return validateConfirmPassword();
        return true;
    }

    private boolean validateEmail() {
        String email = Objects.requireNonNull(emailInput.getText()).toString().trim();
        if (email.isEmpty()) {
            emailLayout.setError("Email is required.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email format.");
            return false;
        } else {
            emailLayout.setError(null); // Clear the error.
            return true;
        }
    }

    private boolean validatePassword() {
        String password = Objects.requireNonNull(passwordInput.getText()).toString();
        if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters.");
            return false;
        } else {
            passwordLayout.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String password = Objects.requireNonNull(passwordInput.getText()).toString();
        String confirmPassword = Objects.requireNonNull(confirmPasswordInput.getText()).toString();
        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match.");
            return false;
        } else {
            confirmPasswordLayout.setError(null);
            return true;
        }
    }

    /**
     * Silently checks all fields to decide if the main sign-up button should be active.
     * Doesn't show error messages, just updates the button state.
     */
    private void validateAllFields() {
        boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(emailInput.getText()).toString().trim()).matches();
        boolean isPasswordValid = Objects.requireNonNull(passwordInput.getText()).toString().length() >= 6;
        boolean isConfirmPasswordValid = Objects.requireNonNull(passwordInput.getText()).toString().equals(Objects.requireNonNull(confirmPasswordInput.getText()).toString());

        signupButton.setEnabled(isEmailValid && isPasswordValid && isConfirmPasswordValid);
        signupButton.setAlpha(isEmailValid && isPasswordValid && isConfirmPasswordValid ? 1.0f : 0.5f);
    }

    /**
     * The final step when the user hits the "Sign Up" button.
     */
    private void performSignUp() {
        // Run all validations one last time to be safe.
        boolean isEmailValid = validateEmail();
        boolean isPasswordValid = validatePassword();
        boolean isConfirmPasswordValid = validateConfirmPassword();

        if (!isEmailValid || !isPasswordValid || !isConfirmPasswordValid) {
            return; // Don't proceed if something is wrong.
        }

        // Switch to a loading state.
        signupButton.setText("");
        loadingProgressBar.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);

        // Fake a 2-second network call.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Restore the button.
            signupButton.setText("Sign Up");
            loadingProgressBar.setVisibility(View.GONE);
            signupButton.setEnabled(true);

            Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();

            // Give the user a moment to see the success message before finishing.
            new Handler(Looper.getMainLooper()).postDelayed(this::finish, 1000);

        }, 2000);
    }
}
