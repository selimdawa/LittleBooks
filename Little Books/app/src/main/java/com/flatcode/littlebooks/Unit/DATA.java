package com.flatcode.littlebooks.Unit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DATA {
    //Database
    public static String USERS = "Users";
    public static String CATEGORIES = "Categories";
    public static String BOOKS = "Books";
    public static String TOOLS = "Tools";
    public static String PRIVACY_POLICY = "privacyPolicy";
    public static String FOLLOW = "Follow";
    public static String FOLLOWERS = "followers";
    public static String FOLLOWING = "following";
    public static String COMMENTS = "Comments";
    public static String LOVES = "Loves";
    public static String VERSION = "version";
    public static String BOOKS_COUNT = "booksCount";
    public static String EMAIL = "email";
    public static String BASIC = "basic";
    public static String USER_NAME = "username";
    public static String PROFILE_IMAGE = "profileImage";
    public static String EMPTY = "";
    public static String SPACE = " ";
    public static String TIMESTAMP = "timestamp";
    public static String COMMENT = "comment";
    public static String URL = "url";
    public static String ID = "id";
    public static String IMAGE = "image";
    public static String SLIDER_SHOW = "SliderShow";
    public static String PUBLISHER = "publisher";
    public static String CATEGORY = "category";
    public static String DESCRIPTION = "description";
    public static String TITLE = "title";
    public static String NULL = "null";
    public static String FAVORITES = "Favorites";
    public static String VIEWS_COUNT = "viewsCount";
    public static String DOWNLOADS_COUNT = "downloadsCount";
    public static String LOVES_COUNT = "lovesCount";
    public static String EDITORS_CHOICE = "editorsChoice";
    public static String NAME = "name";
    public static String ADS_LOADED_COUNT = "adsLoadedCount";
    public static String ADS_CLICKED_COUNT = "adsClickedCount";
    public static String AD_CLICK = "adClick";
    public static String AD_LOAD = "adLoad";
    //Others
    public static String DOT = ".";
    public static int ZERO = 0;
    public static int CURRENT_VERSION = 1;
    public static int SPLASH_TIME = 2000;
    public static int ITEM_DOUBLE = 20;
    public static int MAX_BYTES_PDF = 50000000; // Here Max Size PDF 50MB
    public static int ORDER_MAIN = 2; // Here Max Item Show
    public static int MIN_SQUARE = 500;
    public static Boolean searchStatus = false;
    //Shared
    public static String SHOW_MORE_TYPE = "showMoreType";
    public static String PROFILE_ID = "profileId";
    public static String SHOW_MORE_NAME = "showMoreName";
    public static String SHOW_MORE_BOOLEAN = "showMoreBoolean";
    public static String COLOR_OPTION = "color_option";
    public static String BOOK_ID = "bookId";
    public static String CATEGORY_ID = "categoryId";
    public static String CATEGORY_NAME = "categoryName";
    //Other
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final FirebaseUser FIREBASE_USER = AUTH.getCurrentUser();
    public static final String FirebaseUserUid = FIREBASE_USER.getUid();
    public static final String WEB_SITE = "";
    public static final String FB_ID = "";
    //ADs
    public static String AD_S = "ADs";
    public static String BANNER_SMART_HOME = "BannerSmartHome";
    public static String BANNER_SMART_HOME_2 = "BannerSmartHome2";
    public static String INTERSTITIAL_MAIN = "InterstitialMain";
    public static String BANNER_SMART_FOLLOWERS_BOOKS = "BannerSmartFollowersBooks";
    public static String BANNER_SMART_CATEGORY_BOOKS = "BannerSmartCategoryBooks";
    public static String BANNER_SMART_EXPLORE_PUBLISHERS = "BannerSmartExplorePublishers";
    public static String BANNER_SMART_FAVORITES = "BannerSmartFavorites";
    public static String BANNER_SMART_FOLLOWERS = "BannerSmartFollowers";
    public static String BANNER_SMART_FOLLOWING = "BannerSmartFollowing";
    public static String BANNER_SMART_MORE_BOOKS = "BannerSmartMoreBooks";
    public static String BANNER_SMART_MY_BOOKS = "BannerSmartMyBooks";
    public static String BANNER_SMART_PUBLISHERS_BOOKS = "BannerSmartPublishersBooks";
}