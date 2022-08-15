package com.flatcode.littlebooks.Unit;

import com.flatcode.littlebooks.Auth.AuthActivity;
import com.flatcode.littlebooks.Activity.BookAddActivity;
import com.flatcode.littlebooks.Activity.BookDetailsActivity;
import com.flatcode.littlebooks.Activity.BookEditActivity;
import com.flatcode.littlebooks.Activity.BookViewActivity;
import com.flatcode.littlebooks.Activity.BooksCategoryActivity;
import com.flatcode.littlebooks.Activity.ExplorePublishersActivity;
import com.flatcode.littlebooks.Activity.FavoritesActivity;
import com.flatcode.littlebooks.Activity.FollowersActivity;
import com.flatcode.littlebooks.Activity.FollowingActivity;
import com.flatcode.littlebooks.Auth.ForgetPasswordActivity;
import com.flatcode.littlebooks.Auth.LoginActivity;
import com.flatcode.littlebooks.Activity.MainActivity;
import com.flatcode.littlebooks.Activity.MoreBooksActivity;
import com.flatcode.littlebooks.Activity.MyBooksActivity;
import com.flatcode.littlebooks.Activity.PrivacyPolicyActivity;
import com.flatcode.littlebooks.Activity.ProfileActivity;
import com.flatcode.littlebooks.Activity.ProfileEditActivity;
import com.flatcode.littlebooks.Activity.ProfileInfoActivity;
import com.flatcode.littlebooks.Auth.RegisterActivity;
import com.flatcode.littlebooks.Activity.SplashActivity;

public class CLASS {
    public static Class AUTH = AuthActivity.class;
    public static Class MAIN = MainActivity.class;
    public static Class SPLASH = SplashActivity.class;
    public static Class LOGIN = LoginActivity.class;
    public static Class REGISTER = RegisterActivity.class;
    public static Class CATEGORY_BOOKS = BooksCategoryActivity.class;
    public static Class BOOK_ADD = BookAddActivity.class;
    public static Class BOOK_EDIT = BookEditActivity.class;
    public static Class BOOK_DETAIL = BookDetailsActivity.class;
    public static Class BOOK_VIEW = BookViewActivity.class;
    public static Class PROFILE = ProfileActivity.class;
    public static Class PROFILE_EDIT = ProfileEditActivity.class;
    public static Class PROFILE_INFO = ProfileInfoActivity.class;
    public static Class EXPLORE_PUBLISHERS = ExplorePublishersActivity.class;
    public static Class FOLLOWERS = FollowersActivity.class;
    public static Class FOLLOWING = FollowingActivity.class;
    public static Class MY_BOOKS = MyBooksActivity.class;
    public static Class FAVORITES = FavoritesActivity.class;
    public static Class PRIVACY_POLICY = PrivacyPolicyActivity.class;
    public static Class FORGET_PASSWORD = ForgetPasswordActivity.class;
    public static Class MORE_BOOKS = MoreBooksActivity.class;
}