package com.pplus.prnumberbiz;

/**
 * Created by 김종경 on 2016-08-10.
 */
public class Const {

    public static final String BASE_URL = "https://stage.prnumber.com/";
//    public static final String BASE_URL = "https://api.prnumber.com/";

    public static final String API_URL = BASE_URL + "store/api/";
    public static final boolean DEBUG_MODE = true;

    public static final int LOAD_DATA_LIMIT_CNT = 20;

    //key
    public static final String KEY = "key";
    public static final String URL = "url";
    public static final String GROUP = "group";
    public static final String LOGIN_ID = "login_id";
    public static final String FIND_ID = "find_id";
    public static final String FIND_PW = "find_pw";
    public static final String VERIFICATION = "verification";
    public static final String VERIFICATION_MASTER = "verification_master";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "lon";
    public static final String PRIORITY = "priority";
    public static final String NO = "no";
    public static final String PAGE = "page";
    public static final String MODE = "mode";
    public static final String REPLY = "reply";
    public static final String REPLY_CHILD = "reply_child";
    public static final String POST = "post";
    public static final String ADVERTISE = "advertise";
    public static final String POST_NO = "post_no";
    public static final String COUPON = "coupon";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String GUIDE_SNS = "guide_sns";
    public static final String GUIDE_BOL = "guide_bol";
    public static final String GUIDE_PUSH = "guide_push";
    public static final String GUIDE_SMS = "guide_sms";
    public static final String GUIDE_CUSTOMER = "guide_customer";
    public static final String GUIDE_PLUS = "guide_plus";
    public static final String GUIDE_PRIVACY = "guide_privacy";
    public static final String GUIDE_PERSON_ADDRESS = "guide_person_address";
    public static final String GUIDE_STORE_ADDRESS = "guide_store_address";
    public static final String GUIDE1 = "guide1";
    public static final String GUIDE2 = "guide2";
    public static final String GUIDE3 = "guide3";
    public static final String GUIDE4 = "guide4";
    public static final String GUIDE5 = "guide5";
    public static final String GUIDE6 = "guide6";
    public static final String GUIDE_PERSON_ALL = "guide_person_all";
    public static final String GUIDE_STORE_ALL = "guide_store_all";
    public static final String CUSTOMER_GROUP_ADD = "customer_group_add";
    public static final String CUSTOMER_GROUP_DEL = "customer_group_del";
    public static final String PLUS_GROUP_ADD = "plus_group_add";
    public static final String PLUS_GROUP_DEL = "plus_group_del";
    public static final String CUSTOMER = "customer";
    public static final String WRITE = "write";
    public static final String RECOMMEND = "recommend";
    public static final String JOIN = "join";
    public static final String RE_REG = "re_reg";
    public static final String CANCEL_LEAVE = "cancel_leave";
    public static final String LEAVE = "leave";
    public static final String GOODS = "goods";
    public static final String PG_TYPE = "pg_type";
    public static final String TID = "tid";
    public static final String OID = "oid";
    public static final String USER = "user";
    public static final String DATA = "data";
    public static final String RE_REG_DATA = "re_reg_data";
    public static final String NAME = "name";
    public static final String TERMS_LIST = "terms_list";
    public static final String POSITION = "category_position";
    public static final String CATEGORY = "category";
    public static final String PASSWORD_DEFAULT = "**********";
    public static final String MSG = "msg";
    public static final String ADDRESS = "address";
    public static final String PUSH_DATA = "push_data";
    public static final String SNS_URL = "sns_url";
    public static final String PARAMS = "params";
    public static final String COUNT = "count";
    public static final String CONTENTS = "contents";
    public static final String FIRST = "first";
    public static final String CROPPED_IMAGE = "cropped_image";
    public static final String BOL = "bol";
    public static final String AMOUNT = "amount";
    public static final String PAYMETHOD = "paymethod";
    public static final String NOTICE = "notice";
    public static final String FAQ = "faq";
    public static final String TYPE = "type";
    public static final String SORT = "sort";
    public static final String IS_ME = "is_me";
    public static final String AUTH_CODE = "auth_code";
    public static final String PUSH_ID = "push_id";
    public static final String ID = "id";
    public static final String PASSWORD = "password";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String SIGN_UP = "sign_up";
    public static final String BIRTH_DAY = "birth_day";
    public static final String FREE = "free";
    public static final String PATH = "path";
    public static final String SALE_CAL = "sale_cal";
    public static final String OTHER = "other";
    public static final String KAKAO = "kakao";
    public static final String NIGHT_ADS = "night_sms";
    public static final String FREE_NUMBER_PREFIX = "free_number_prefix";
    public static final String ALERT_GOODS = "alert_goods";
    public static final String ALERT_NUMBER = "alert_number";
    public static final String TAB = "tab";
    public static final String START = "start";
    public static final String END = "end";
    public static final String REG = "reg";
    public static final String SELECT = "select";
    public static final String PENDING = "pending";
    public static final String IS_LINK = "is_link";
    public static final String LEVEL_UP = "level_up";
    public static final String LPNG = "lpng";
    public static final String INSTALLMENT = "installment";
    public static final String ORDER_ID = "order_id";

    public static final float NEXT_RECYCLERVIEW_MARGIN = 0.8f;

    public static final String SECRET_KEY = "!@#$QWER";

    // webview
    public static final String WEBVIEW_URL = "url";
    public static final String TITLE = "title";
    public static final String TOOLBAR_RIGHT_ARROW = "right_arrow";

    //req
    public static final int REQ_CHANGE_PHONE = 1000;
    public static final int REQ_JOIN = 1001;
    public static final int REQ_LOCATION_CODE = 1002;
    public static final int REQ_GROUP_CONFIG = 1003;
    public static final int REQ_CHANGE_PW = 1004;
    public static final int REQ_RESTRICTION = 1005; // 제재 팝업에서 FAQ키로 이동 뒤에는 메인으로 이동 처리
    public static final int REQ_ADS_SETTING = 1006;
    public static final int REQ_IMAGE_FILTER = 1007;
    public static final int REQ_SET_PAGE = 1008;
    public static final int REQ_SECRET_MODE = 1009;
    public static final int REQ_VERIFICATION = 1010;
    public static final int REQ_BACKGROUND_IMAGE = 1011;
    public static final int REQ_SEARCH = 1012;
    public static final int REQ_POST = 1013;
    public static final int REQ_PERMISSION = 1014;
    public static final int REQ_CAMERA_PERMISSION = 1015;
    public static final int REQ_CAMERA = 1016;
    public static final int REQ_GALLERY = 1017;
    public static final int REQ_IMAGE_DELETE = 1018;
    public static final int REQ_CONFIRM = 1019;
    public static final int REQ_SIGN_IN = 1020;
    public static final int REQ_CASH_CHANGE = 1021;
    public static final int REQ_BACKGROUND_DEFAULT_IMAGE = 1022;
    public static final int REQ_PICK_FROM_FILE = 1023;
    public static final int REQ_CROP_IMAGE = 1024;
    public static final int REQ_CONTENTS = 1025;
    public static final int REQ_RESERVATION = 1026;
    public static final int REQ_DETAIL = 1027;
    public static final int REQ_LEAVE_CANCEL = 1028;
    public static final int REQ_LEAVE = 1029;
    public static final int REQ_FIND_ID = 1030;
    public static final int REQ_FIND_PW = 1031;
    public static final int REQ_RE_REG_NUMBER = 1032;
    public static final int REQ_CUSTOMER = 1033;
    public static final int REQ_REPLY = 1034;
    public static final int REQ_POST_SNS = 1035;
    public static final int REQ_APPLY = 1036;
    public static final int REQ_CATEGORY_APPLY = 1037;
    public static final int REQ_REVIEW = 1038;
    public static final int REQ_SMS_LOCKER = 1039;
    public static final int REQ_INTRODUCE_IMAGE = 1040;
    public static final int REQ_INTRODUCE_IMAGE_DETAIL = 1041;
    public static final int REQ_FRANCHISE = 1042;
    public static final int REQ_PG_PURCHASE = 1043;
    public static final int REQ_CONTACT = 1044;
    public static final int REQ_SYNC_SNS = 1045;
    public static final int REQ_CONFIG = 1046;
    public static final int REQ_GOODS_DETAIL = 1047;
    public static final int REQ_SET_PAGE_TYPE = 1048;
    public static final int REQ_ALARM = 1049;
    public static final int REQ_REG = 1050;
    public static final int REQ_START_DATE = 1051;
    public static final int REQ_END_DATE = 1052;
    public static final int REQ_MODIFY = 1053;
    public static final int REQ_ALERT_GOODS = 1054;
    public static final int REQ_EXPIRE_DATE = 1055;
    public static final int REQ_SALE_HISTORY = 1056;
    public static final int REQ_START_TIME = 1057;
    public static final int REQ_END_TIME = 1058;
    public static final int REQ_TIME = 1059;
    public static final int REQ_SEND = 1060;
    public static final int REQ_SELECT = 1061;
    public static final int REQ_GOODS_NOTICE = 1062;
    public static final int REQ_PENDING = 1063;
    public static final int REQ_BANK_SELECT = 1064;
    public static final int REQ_MAKE_PRNUMBER = 1065;
    public static final int REQ_PAY = 1066;
    public static final int REQ_INSTALLMENT = 1067;
    public static final int REQ_PAY_COMPLETE = 1068;
    public static final int REQ_REFUSE = 1069;

    public static final int IMAGE_UPLOAD_MAX_COUNT = 10;

    public static final double CHROME_WEB_MEMORY_MIN = 200.0; // 200 MegaByte
    public static final double CHROME_WEB_MEMORY_MAX = 1000.0; // 최소 1G 이상인 경우
}
