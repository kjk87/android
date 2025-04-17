package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by j2n on 2017. 1. 16..
 */

public class Comment implements Parcelable{

    private Long no;
    private Long group_seq_no;
    private String comment;
    private boolean blind;
    private Integer depth;
    private Integer priority;
    private boolean deleted;
    private Comment parent;
    private User author;
    private Post post;
    private String regDate;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public Long getGroup_seq_no(){

        return group_seq_no;
    }

    public void setGroup_seq_no(Long group_seq_no){

        this.group_seq_no = group_seq_no;
    }

    public String getComment(){

        return comment;
    }

    public void setComment(String comment){

        this.comment = comment;
    }

    public boolean isBlind(){

        return blind;
    }

    public void setBlind(boolean blind){

        this.blind = blind;
    }

    public Integer getDepth(){

        return depth;
    }

    public void setDepth(Integer depth){

        this.depth = depth;
    }

    public Integer getPriority(){

        return priority;
    }

    public void setPriority(Integer priority){

        this.priority = priority;
    }

    public boolean isDeleted(){

        return deleted;
    }

    public void setDeleted(boolean deleted){

        this.deleted = deleted;
    }

    public User getAuthor(){

        return author;
    }

    public void setAuthor(User author){

        this.author = author;
    }

    public Post getPost(){

        return post;
    }

    public void setPost(Post post){

        this.post = post;
    }

    public Comment getParent(){

        return parent;
    }

    public void setParent(Comment parent){

        this.parent = parent;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    @Override
    public String toString(){

        return "Comment{" + "no=" + no + ", group_seq_no=" + group_seq_no + ", comment='" + comment + '\'' + ", blind=" + blind + ", depth=" + depth + ", priority=" + priority + ", deleted=" + deleted + ", parent=" + parent + ", author=" + author + ", post=" + post + ", regDate='" + regDate + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeValue(this.group_seq_no);
        dest.writeString(this.comment);
        dest.writeByte(this.blind ? (byte) 1 : (byte) 0);
        dest.writeValue(this.depth);
        dest.writeValue(this.priority);
        dest.writeByte(this.deleted ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.parent, flags);
        dest.writeParcelable(this.author, flags);
        dest.writeParcelable(this.post, flags);
        dest.writeString(this.regDate);
    }

    public Comment(){

    }

    protected Comment(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.group_seq_no = (Long) in.readValue(Long.class.getClassLoader());
        this.comment = in.readString();
        this.blind = in.readByte() != 0;
        this.depth = (Integer) in.readValue(Integer.class.getClassLoader());
        this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
        this.deleted = in.readByte() != 0;
        this.parent = in.readParcelable(Comment.class.getClassLoader());
        this.author = in.readParcelable(User.class.getClassLoader());
        this.post = in.readParcelable(Post.class.getClassLoader());
        this.regDate = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>(){

        @Override
        public Comment createFromParcel(Parcel source){

            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size){

            return new Comment[size];
        }
    };
}
