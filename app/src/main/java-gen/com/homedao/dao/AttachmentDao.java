package com.homedao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.homedao.bean.Attachment;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ATTACHMENT".
*/
public class AttachmentDao extends AbstractDao<Attachment, Long> {

    public static final String TABLENAME = "ATTACHMENT";
    /**
     * Properties of entity Attachment.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Order_code = new Property(1, String.class, "order_code", false, "ORDER_CODE");
        public final static Property User_code = new Property(2, String.class, "user_code", false, "USER_CODE");
        public final static Property File_id = new Property(3, String.class, "file_id", false, "FILE_ID");
        public final static Property Type = new Property(4, String.class, "type", false, "TYPE");
        public final static Property File_path = new Property(5, String.class, "file_path", false, "FILE_PATH");
        public final static Property Create_time = new Property(6, String.class, "create_time", false, "CREATE_TIME");
        public final static Property Status = new Property(7, String.class, "status", false, "STATUS");
        public final static Property Type_id = new Property(8, String.class, "type_id", false, "TYPE_ID");
        public final static Property File_size = new Property(9, String.class, "file_size", false, "FILE_SIZE");
    };


    public AttachmentDao(DaoConfig config) {
        super(config);
    }
    
    public AttachmentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ATTACHMENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ORDER_CODE\" TEXT," + // 1: order_code
                "\"USER_CODE\" TEXT," + // 2: user_code
                "\"FILE_ID\" TEXT," + // 3: file_id
                "\"TYPE\" TEXT," + // 4: type
                "\"FILE_PATH\" TEXT," + // 5: file_path
                "\"CREATE_TIME\" TEXT," + // 6: create_time
                "\"STATUS\" TEXT," + // 7: status
                "\"TYPE_ID\" TEXT," + // 8: type_id
                "\"FILE_SIZE\" TEXT);"); // 9: file_size
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ATTACHMENT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Attachment entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String order_code = entity.getOrder_code();
        if (order_code != null) {
            stmt.bindString(2, order_code);
        }
 
        String user_code = entity.getUser_code();
        if (user_code != null) {
            stmt.bindString(3, user_code);
        }
 
        String file_id = entity.getFile_id();
        if (file_id != null) {
            stmt.bindString(4, file_id);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
 
        String file_path = entity.getFile_path();
        if (file_path != null) {
            stmt.bindString(6, file_path);
        }
 
        String create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindString(7, create_time);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(8, status);
        }
 
        String type_id = entity.getType_id();
        if (type_id != null) {
            stmt.bindString(9, type_id);
        }
 
        String file_size = entity.getFile_size();
        if (file_size != null) {
            stmt.bindString(10, file_size);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Attachment readEntity(Cursor cursor, int offset) {
        Attachment entity = new Attachment( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // order_code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // user_code
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // file_id
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // type
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // file_path
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // create_time
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // status
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // type_id
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // file_size
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Attachment entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOrder_code(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUser_code(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFile_id(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFile_path(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCreate_time(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setStatus(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setType_id(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFile_size(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Attachment entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Attachment entity) {
        if(entity != null) {
            return entity.getId();
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
