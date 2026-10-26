package com.nomio.app;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import android.text.*;
import java.util.*;

public class MainActivity extends Activity {

    int cream = Color.rgb(250, 248, 240);
    int green = Color.rgb(105, 139, 112);
    int dark = Color.rgb(39, 45, 40);
    int muted = Color.rgb(120, 126, 119);

    LinearLayout root, list;
    EditText search;
    String category = "All";

    String[][] foods = {
        {"Veg Burger", "Juicy veggie patty with fresh vegetables", "₹99", "🍔", "Fast Food"},
        {"Margherita Pizza", "Cheese, tomato and fresh basil", "₹179", "🍕", "Pizza"},
        {"Masala Dosa", "Crispy dosa with chutney and sambar", "₹89", "🥞", "South Indian"},
        {"Paneer Wrap", "Spiced paneer with crunchy vegetables", "₹129", "🌯", "Fast Food"},
        {"French Fries", "Golden crispy salted fries", "₹79", "🍟", "Snacks"},
        {"Cold Coffee", "Chilled creamy coffee", "₹99", "🥤", "Drinks"}
    };

    ArrayList<String> cart = new ArrayList<>();

    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(cream);
        getWindow().setNavigationBarColor(cream);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        );

        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 16, 20, 24);
        root.setBackgroundColor(cream);
        scroll.addView(root);
        setContentView(scroll);

        TextView logo = text("nomio.", 32, green, true);
        root.addView(logo);

        TextView tagline = text("GOOD FOOD. GOOD MOOD.", 11, muted, true);
        root.addView(tagline);

        space(22);

        TextView heading = text("What are you\ncraving today?", 29, dark, true);
        root.addView(heading);

        space(16);

        search = new EditText(this);
        search.setSingleLine(true);
        search.setTextSize(15);
        search.setHint(" 🔍  Search food...");
        search.setPadding(16, 8, 16, 8);
        search.setBackground(shape(Color.WHITE, 16));
        root.addView(search, params(-1, 54));

        space(20);

        TextView section = text("Explore categories", 19, dark, true);
        root.addView(section);

        LinearLayout chips = new LinearLayout(this);
        chips.setOrientation(LinearLayout.HORIZONTAL);

        String[] cats = {"All", "Fast Food", "Pizza", "South Indian", "Snacks", "Drinks"};
        for (String c : cats) {
            TextView chip = text(c, 12, green, true);
            chip.setPadding(14, 10, 14, 10);
            chip.setBackground(shape(Color.WHITE, 20));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(
                -2, -2);
            cp.setMargins(0, 10, 8, 10);
            chips.addView(chip, cp);
            chip.setOnClickListener(v -> {
                category = c;
                showFoods();
            });
        }

        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.setHorizontalScrollBarEnabled(false);
        hsv.addView(chips);
        root.addView(hsv);

        space(12);

        TextView popular = text("Popular near you", 21, dark, true);
        root.addView(popular);

        TextView subtitle = text("Made fresh, just for you.", 13, muted, false);
        root.addView(subtitle);

        space(12);

        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        root.addView(list);

        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showFoods();
            }
            public void afterTextChanged(Editable e) {}
        });

        showFoods();
    }

    void showFoods() {
        if (list == null) return;
        list.removeAllViews();

        String q = search == null ? "" : search.getText().toString().toLowerCase();

        for (String[] f : foods) {
            if (!category.equals("All") && !f[4].equals(category)) continue;
            if (!f[0].toLowerCase().contains(q) &&
                !f[1].toLowerCase().contains(q)) continue;

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setPadding(14, 14, 14, 14);
            card.setGravity(Gravity.CENTER_VERTICAL);
            card.setBackground(shape(Color.WHITE, 20));

            LinearLayout.LayoutParams cardp = new LinearLayout.LayoutParams(-1, -2);
            cardp.setMargins(0, 6, 0, 10);
            list.addView(card, cardp);

            TextView emoji = text(f[3], 36, dark, false);
            emoji.setGravity(Gravity.CENTER);
            emoji.setBackground(shape(cream, 16));
            card.addView(emoji, new LinearLayout.LayoutParams(82, 82));

            LinearLayout info = new LinearLayout(this);
            info.setOrientation(LinearLayout.VERTICAL);
            info.setPadding(12, 0, 0, 0);
            card.addView(info, new LinearLayout.LayoutParams(0, -2, 1));

            info.addView(text(f[0], 16, dark, true));
            TextView desc = text(f[1], 11, muted, false);
            info.addView(desc);
            spaceIn(info, 7);

            LinearLayout bottom = new LinearLayout(this);
            bottom.setGravity(Gravity.CENTER_VERTICAL);
            info.addView(bottom);

            bottom.addView(text(f[2], 16, green, true));

            TextView add = text(" + Add ", 12, Color.WHITE, true);
            add.setGravity(Gravity.CENTER);
            add.setPadding(12, 8, 12, 8);
            add.setBackground(shape(green, 18));
            LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-2, -2);
            ap.setMargins(10, 0, 0, 0);
            bottom.addView(add, ap);

            add.setOnClickListener(v -> {
                cart.add(f[0]);
                Toast.makeText(this, f[0] + " added to cart", Toast.LENGTH_SHORT).show();
                showCartButton();
            });
        }

        if (list.getChildCount() == 0) {
            list.addView(text("No matching food found.", 14, muted, false));
        }

        showCartButton();
    }

    TextView cartButton;

    void showCartButton() {
        if (cartButton == null) {
            cartButton = text("", 15, Color.WHITE, true);
            cartButton.setGravity(Gravity.CENTER);
            cartButton.setPadding(16, 14, 16, 14);
            cartButton.setBackground(shape(green, 18));
            root.addView(cartButton, params(-1, -2));
            cartButton.setOnClickListener(v -> openCart());
        }

        cartButton.setText("🛒  View cart  •  " + cart.size() + " items");
        cartButton.setVisibility(cart.size() == 0 ? View.GONE : View.VISIBLE);
    }

    void openCart() {
        StringBuilder message = new StringBuilder();
        int total = 0;

        for (String name : cart) {
            for (String[] f : foods) {
                if (f[0].equals(name)) {
                    message.append(name).append(" — ").append(f[2]).append("\n");
                    total += Integer.parseInt(f[2].replace("₹", ""));
                }
            }
        }

        new AlertDialog.Builder(this)
            .setTitle("Your NOMIO cart")
            .setMessage(message + "\nTotal: ₹" + total +
                "\n\nDemo cart only — checkout isn't connected yet.")
            .setPositiveButton("Continue shopping", (d, w) -> {})
            .setNegativeButton("Clear cart", (d, w) -> {
                cart.clear();
                showFoods();
            })
            .show();
    }

    TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    LinearLayout.LayoutParams params(int w, int h) {
        return new LinearLayout.LayoutParams(w, h);
    }

    void space(int height) {
        View v = new View(this);
        root.addView(v, params(1, height));
    }

    void spaceIn(LinearLayout parent, int height) {
        View v = new View(this);
        parent.addView(v, params(1, height));
    }
          }
