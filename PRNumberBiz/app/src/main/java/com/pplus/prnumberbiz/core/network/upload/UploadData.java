package com.pplus.prnumberbiz.core.network.upload;

import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment;

/**
 * Created by Administrator on 2016-10-12.
 */

public class UploadData{

    /**
     * 업로드 상태 표시함
     */
    private UploadData.Status status;
    /**
     * 업로드 객체
     */
    private ParamsAttachment paramsAttachment;

    /**
     * 결과 객체
     */
    private Attachment attachment;

    public Status getStatus(){

        return status;
    }

    public void setStatus(Status status){

        this.status = status;
    }

    public ParamsAttachment getParamsAttachment(){

        return paramsAttachment;
    }

    public void setParamsAttachment(ParamsAttachment paramsAttachment){

        this.paramsAttachment = paramsAttachment;
    }

    public Attachment getAttachment(){

        return attachment;
    }

    public void setAttachment(Attachment attachment){

        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object o){

        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        UploadData that = (UploadData) o;

        return attachment != null ? attachment.equals(that.attachment) : that.attachment == null;

    }

    @Override
    public int hashCode(){

        return attachment != null ? attachment.hashCode() : 0;
    }

    public enum Status{
        UPLOAD, ERROR
    }

}
