package com.pplus.prnumberbiz.core.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.pplus.prnumberbiz.core.database.entity.Contact;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONTACT".
*/
public class ContactDao extends AbstractDao<Contact, String> {

    public static final String TABLENAME = "CONTACT";

    /**
     * Properties of entity Contact.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property MobileNumber = new Property(0, String.class, "mobileNumber", true, "MOBILE_NUMBER");
        public final static Property ImageUrl = new Property(1, String.class, "imageUrl", false, "IMAGE_URL");
        public final static Property MemberName = new Property(2, String.class, "memberName", false, "MEMBER_NAME");
        public final static Property MemberNickname = new Property(3, String.class, "memberNickname", false, "MEMBER_NICKNAME");
        public final static Property MemberNo = new Property(4, Long.class, "memberNo", false, "MEMBER_NO");
        public final static Property PageName = new Property(5, String.class, "pageName", false, "PAGE_NAME");
        public final static Property PageNo = new Property(6, Long.class, "pageNo", false, "PAGE_NO");
        public final static Property VirtualNumber = new Property(7, String.class, "virtualNumber", false, "VIRTUAL_NUMBER");
        public final static Property Delete = new Property(8, Boolean.class, "delete", false, "DELETE");
        public final static Property Update = new Property(9, Boolean.class, "update", false, "UPDATE");
        public final static Property BlogUrl = new Property(10, String.class, "blogUrl", false, "BLOG_URL");
        public final static Property HomepageUrl = new Property(11, String.class, "homepageUrl", false, "HOMEPAGE_URL");
        public final static Property PageType = new Property(12, String.class, "pageType", false, "PAGE_TYPE");
        public final static Property PageProfileImageUrl = new Property(13, String.class, "pageProfileImageUrl", false, "PAGE_PROFILE_IMAGE_URL");
    };


    public ContactDao(DaoConfig config) {
        super(config);
    }
    
    public ContactDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTACT\" (" + //
                "\"MOBILE_NUMBER\" TEXT PRIMARY KEY NOT NULL ," + // 0: mobileNumber
                "\"IMAGE_URL\" TEXT," + // 1: imageUrl
                "\"MEMBER_NAME\" TEXT," + // 2: memberName
                "\"MEMBER_NICKNAME\" TEXT," + // 3: memberNickname
                "\"MEMBER_NO\" INTEGER," + // 4: memberNo
                "\"PAGE_NAME\" TEXT," + // 5: pageName
                "\"PAGE_NO\" INTEGER," + // 6: pageNo
                "\"VIRTUAL_NUMBER\" TEXT," + // 7: virtualNumber
                "\"DELETE\" INTEGER," + // 8: delete
                "\"UPDATE\" INTEGER," + // 9: update
                "\"BLOG_URL\" TEXT," + // 10: blogUrl
                "\"HOMEPAGE_URL\" TEXT," + // 11: homepageUrl
                "\"PAGE_TYPE\" TEXT," + // 12: pageType
                "\"PAGE_PROFILE_IMAGE_URL\" TEXT);"); // 13: pageProfileImageUrl
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTACT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Contact entity) {
        stmt.clearBindings();
 
        String mobileNumber = entity.getMobileNumber();
        if (mobileNumber != null) {
            stmt.bindString(1, mobileNumber);
        }
 
        String imageUrl = entity.getImageUrl();
        if (imageUrl != null) {
            stmt.bindString(2, imageUrl);
        }
 
        String memberName = entity.getMemberName();
        if (memberName != null) {
            stmt.bindString(3, memberName);
        }
 
        String memberNickname = entity.getMemberNickname();
        if (memberNickname != null) {
            stmt.bindString(4, memberNickname);
        }
 
        Long memberNo = entity.getMemberNo();
        if (memberNo != null) {
            stmt.bindLong(5, memberNo);
        }
 
        String pageName = entity.getPageName();
        if (pageName != null) {
            stmt.bindString(6, pageName);
        }
 
        Long pageNo = entity.getPageNo();
        if (pageNo != null) {
            stmt.bindLong(7, pageNo);
        }
 
        String virtualNumber = entity.getVirtualNumber();
        if (virtualNumber != null) {
            stmt.bindString(8, virtualNumber);
        }
 
        Boolean delete = entity.getDelete();
        if (delete != null) {
            stmt.bindLong(9, delete ? 1L: 0L);
        }
 
        Boolean update = entity.getUpdate();
        if (update != null) {
            stmt.bindLong(10, update ? 1L: 0L);
        }
 
        String blogUrl = entity.getBlogUrl();
        if (blogUrl != null) {
            stmt.bindString(11, blogUrl);
        }
 
        String homepageUrl = entity.getHomepageUrl();
        if (homepageUrl != null) {
            stmt.bindString(12, homepageUrl);
        }
 
        String pageType = entity.getPageType();
        if (pageType != null) {
            stmt.bindString(13, pageType);
        }
 
        String pageProfileImageUrl = entity.getPageProfileImageUrl();
        if (pageProfileImageUrl != null) {
            stmt.bindString(14, pageProfileImageUrl);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Contact readEntity(Cursor cursor, int offset) {
        Contact entity = new Contact( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // mobileNumber
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // imageUrl
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // memberName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // memberNickname
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // memberNo
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // pageName
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // pageNo
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // virtualNumber
            cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0, // delete
            cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0, // update
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // blogUrl
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // homepageUrl
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // pageType
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // pageProfileImageUrl
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Contact entity, int offset) {
        entity.setMobileNumber(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setImageUrl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMemberName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMemberNickname(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMemberNo(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setPageName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPageNo(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setVirtualNumber(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDelete(cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0);
        entity.setUpdate(cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0);
        entity.setBlogUrl(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setHomepageUrl(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setPageType(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPageProfileImageUrl(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Contact entity, long rowId) {
        return entity.getMobileNumber();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Contact entity) {
        if(entity != null) {
            return entity.getMobileNumber();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
