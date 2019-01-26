package com.tranxitpro.provider.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tranxitpro.provider.Fragment.OnGoingTrips;
import com.tranxitpro.provider.Fragment.PastTrips;
import com.tranxitpro.provider.Fragment.WelcomeScreen1;
import com.tranxitpro.provider.Fragment.WelcomeScreen2;
import com.tranxitpro.provider.Fragment.WelcomeScreen3;
import com.tranxitpro.provider.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WelcomeScreenActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    Button loginButton, signUpButton;
    TextView skipBtn;
    LinearLayout social_layout;
    ScalePageTransformer scalePageTransformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        setContentView(R.layout.activity_welcome_screen);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        loginButton = (Button) findViewById(R.id.sign_in_btn);
        skipBtn = (TextView) findViewById(R.id.skip);
        signUpButton = (Button) findViewById(R.id.sign_up_btn);
        social_layout = (LinearLayout) findViewById(R.id.social_layout);
        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3
        };
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeScreenActivity.this, ActivityEmail.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
//                finish();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeScreenActivity.this, RegisterActivity.class).putExtra("signup", true).putExtra("viewpager", "yes").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
//                finish();
            }
        });

        social_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeScreenActivity.this, ActivitySocialLogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
//                finish();
            }
        });
        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
//        skipBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(WelcomeScreenActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
//                finish();
//
//            }
//        });
        // adding bottom dots
        addBottomDots(0);
        // making notification bar transparent
        changeStatusBarColor();
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        scalePageTransformer = new ScalePageTransformer(viewPager);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
            dots[currentPage].setTextColor(colorsActive[currentPage]);
            dots[currentPage].startAnimation(animation);
        }

    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
//            params.setMargins((int) ((position + positionOffset) * 500), 0, 0, 0);
//            viewPager.setLayoutParams(params);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class ScalePageTransformer implements ViewPager.PageTransformer {
        private static final float SCALE_FACTOR = 0.95f;

        private final ViewPager mViewPager;

        public ScalePageTransformer(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        @SuppressLint("NewApi")
        @Override
        public void transformPage(View page, float position) {
            if (position <= 0) {
                // apply zoom effect and offset translation only for pages to
                // the left
                final float transformValue = Math.abs(Math.abs(position) - 1) * (1.0f - SCALE_FACTOR) + SCALE_FACTOR;
                int pageWidth = mViewPager.getWidth();
                final float translateValue = position * -pageWidth;
                page.setScaleX(transformValue);
                page.setScaleY(transformValue);
                if (translateValue > -pageWidth) {
                    page.setTranslationX(translateValue);
                } else {
                    page.setTranslationX(0);
                }
            }
        }

    }
    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        private LayoutInflater layoutInflater;
        FragmentManager fm;
        final int PAGE_COUNT = 3;


        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WelcomeScreen1();
                case 1:
                    return new WelcomeScreen2();
                case 2:
                    return new WelcomeScreen3();
                default:
                    return new WelcomeScreen1();
            }
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = layoutInflater.inflate(layouts[position], container, false);
//            container.addView(view);
//            return view;
//        }

//        @Override
//        public boolean isViewFromObject(View view, Object obj) {
//            return view == obj;
//        }

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            container.removeView(view);
//        }
    }
}

