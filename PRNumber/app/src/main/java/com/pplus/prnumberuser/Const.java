package com.pplus.prnumberuser;

/**
 * Created by 김종경 on 2016-08-10.
 */
public class Const {

    private static final String BASE_URL = "https://stage.prnumber.com/";
//    private static final String BASE_URL = "https://api.prnumber.com/";
    public static final String CS_URL = "https://stg-www.plusmember.co.kr/";
//    public static final String CS_URL = "https://www.plusmember.co.kr/";

    public static final boolean DEBUG_MODE = true;

    public static final String API_URL = BASE_URL + "store/api/";

    public static final int LOAD_DATA_LIMIT_CNT = 20;

    public static boolean onReceivePlus = false;
    public static String currentChatRoom = "";

    public static final String APP_TYPE = "pplus";

    //    public static final String YOUTUBE_WATCH_URL_PREFIX = "http://www.youtube.com/watch?v=";

//    public static final String GAL_COM_CODE = "A2820008";

    //key
    public static final String KEY = "key";
    public static final String TAB = "tab";
    public static final String GROUP = "group";
    public static final String LOGIN_ID = "login_id";
    public static final String FIND_ID = "find_id";
    public static final String FIND_PW = "find_pw";
    public static final String FEED = "feed";
    public static final String VERIFICATION = "verification";
    public static final String VERIFICATION_ME = "verification_me";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DELIVERY_LATITUDE = "delivery_latitude";
    public static final String DELIVERY_LONGITUDE = "delivery_longitude";
    public static final String DELIVERY_ADDRESS = "delivery_address";
    public static final String DELIVERY_ADDRESS_DETAIL = "delivery_address_detail";
    public static final String PRIORITY = "priority";
    public static final String NO = "no";
    public static final String PAGE = "page";
    public static final String PAGE_ATTENDANCE = "page_attendance";
    public static final String PAGE_SEQ_NO = "page_seq_no";
    public static final String PAGE2 = "page2";
    public static final String MODE = "mode";
    public static final String REPLY = "reply";
    public static final String REPLY_CHILD = "reply_child";
    public static final String POST = "post";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String GUIDE_POINT_PAGE = "guide_point_page";
    public static final String GUIDE_HOT_DEAL_LIVE = "guide_hot_deal_live";
    public static final String GUIDE_PROFILE_SET = "guide_profile_set";
    public static final String PLUS_GROUP_ADD = "plus_group_add";
    public static final String PLUS_GROUP_DEL = "plus_group_del";
    public static final String CUSTOMER = "customer";
    public static final String RECOMMEND = "recommend";
    public static final String JOIN = "join";
    public static final String LEAVE = "leave";
    public static final String CANCEL_LEAVE = "cancel_leave";
    public static final String USER = "user";
    public static final String DATA = "data";
    public static final String POINT = "point";
    public static final String TERMS_LIST = "terms_list";
    public static final String POSITION = "category_position";
    public static final String CATEGORY = "category";
    public static final String PASSWORD_DEFAULT = "**********";
    public static final String PUSH_DATA = "push_data";
    public static final String NUMBER = "number";
    public static final String CROPPED_IMAGE = "cropped_image";
    public static final String GOODS = "goods";
    public static final String GOODS_HISTORY = "goods_history";
    public static final String HISTORY = "history";
    public static final String TICKET_HISTORY = "ticket_history";
    public static final String NOTICE = "notice";
    public static final String FAQ = "faq";
    public static final String OID = "oid";
    public static final String TID = "tid";
    public static final String PG_TYPE = "pg_type";
    public static final String COUNT = "count";
    public static final String SECRET_KEY = "!@#$QWER";
    public static final String SCHEMA_SEARCH = "schema_search";
    public static final String SCHEMA = "schema";
    public static final String EVENT_RESULT = "event_result";
    public static final String EVENT_WIN = "event_win";
    public static final String PAD = "pad";
    public static final String SIGN_UP = "sign_up";
    public static final String BIRTH_DAY = "birth_day";
    public static final String ID = "id";
    public static final String PASSWORD = "password";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String NICKNAME = "nickname";
    public static final String TIME = "time";
    public static final String PUSH_ID = "push_id";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String TERMS1 = "terms1";
    public static final String TERMS2 = "terms2";
    public static final String BUY_GOODS = "buy_goods";
    public static final String EVENT_GUIDE = "event_guide";
    public static final String PLAY_GUIDE = "play_guide";
    public static final String NUMBER_EVENT_GUIDE = "number_event_guide";
    public static final String LOGIN = "login";
    public static final String TUTORIAL = "tutorial";
    public static final String TYPE = "type";
    public static final String MENU = "menu";
    public static final String ADDRESS = "address";
    public static final String CONFIRM_LOTTO = "confirm_lotto";
    public static final String EVENT = "event";
    public static final String LOTTO_TIMES = "lotto_times";
    public static final String LOTTO = "lotto";
    public static final String WIN_CODE = "win_code";
    public static final String LOTTO_GUIDE = "lotto_guide";
    public static final String EXCHANGE = "exchange";
    public static final String START = "start";
    public static final String END = "end";
    public static final String PROFILE = "profile";
    public static final String PLUS = "plus";
    public static final String PLUS_INFO = "plus_info";
    public static final String PLUS_INFO_PAGE_SEQ_NO = "plus_info_page_seq_no";
    public static final String VISIT_PAGE_SEQ_NO = "visit_page_seq_no";
    public static final String BENEFIT_PAGE_SEQ_NO = "benefit_page_seq_no";
    public static final String EVENT_DETAIL_SEQ_NO = "event_detail_seq_no";
    public static final String REFUND = "refund";
    public static final String WARNING = "warning";
    public static final String BUY = "buy";
    public static final String INSTALLMENT = "installment";
    public static final String PRICE = "price";
    public static final String PROPERTIES = "properties";
    public static final String IS_FIRST = "is_first";
    public static final String IS_ME = "is_me";
    public static final String NAME = "name";
    public static final String ITEM = "item";
    public static final String DETAIL = "detail";
    public static final String SHIPPING_SITE = "shipping_site";
    public static final String EVENT_GIFT = "event_gift";
    public static final String GOODS_PRICE = "goods_price";
    public static final String OPTION_TYPE = "option_type";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PURCHASE_PRODUCT = "purchase_product";
    public static final String ORDER_PURCHASE = "order_purchase";
    public static final String JOIN_DATE = "join_date";
    public static final String JOIN_TERM = "join_term";
    public static final String ROOM_NAME = "room_name";
    public static final String SALES_TYPE = "sales_type";
    public static final String GOOGLE_REVIEW = "google_review";
    public static final String PLUS_GIFT = "plus_gift";
    public static final String CASH = "cash";
    public static final String SUBSCRIPTION_DOWNLOAD = "subscription_download";
    public static final String SUBSCRIPTION = "subscription";
    public static final String PREPAYMENT = "prepayment";
    public static final String SUBSCRIPTION_PRODUCT_SEQ_NO = "subscription_product_seq_no";
    public static final String ORDER = "order";
    public static final String TICKET = "ticket";


    public static final float NEXT_RECYCLERVIEW_MARGIN = 0.8f;

    // webview
    public static final String WEBVIEW_URL = "url";
    public static final String TITLE = "title";
    public static final String TOOLBAR_RIGHT_ARROW = "right_arrow";

    //req
    public static final int REQ_LOCATION_CODE = 1002;
    public static final int REQ_DETAIL = 1030;
    public static final int REQ_EVENT_DETAIL = 1031;
    public static final int REQ_USE = 1036;
    public static final int REQ_PAGE = 1037;
    public static final int REQ_REVIEW = 1038;
    public static final int REQ_ALERT_REVIEW = 1039;
    public static final int REQ_RESULT = 1040;
    public static final int REQ_JOIN_ALERT = 1044;
    public static final int REQ_MODIFY = 1045;
    public static final int REQ_GOODS_DETAIL = 1047;
    public static final int REQ_BUY_CANCEL = 1051;
    public static final int REQ_PLUS = 1052;
    public static final int REQ_CHROME = 1053;
    public static final int REQ_EVENT_AGREE = 1059;

    public static final int IMAGE_UPLOAD_MAX_COUNT = 10;

    public static final double CHROME_WEB_MEMORY_MIN = 200.0; // 200 MegaByte
    public static final double CHROME_WEB_MEMORY_MAX = 1000.0; // 최소 1G 이상인 경우

    public static String ADMOB_ID = "ca-app-pub-6932639067773475~3249177850";
    public static String ADMOB_INTERSTITIAL_ID = "ca-app-pub-6932639067773475/6645249962";

}
