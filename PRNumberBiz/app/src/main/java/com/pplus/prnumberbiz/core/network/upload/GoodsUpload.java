package com.pplus.prnumberbiz.core.network.upload;

import com.google.gson.reflect.TypeToken;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

/**
 * Created by j2n on 2016. 9. 30..
 */

public class GoodsUpload extends AbstractUpload<Attachment>{

    public GoodsUpload(PplusUploadListener<Attachment> pplusCallback){

        super(pplusCallback);
    }

    @Override
    String getUploadUrl(){

        return Const.API_URL + "goodsImage";
    }

    @Override
    boolean isMultiThreadEnable(){

        return true;
    }

    @Override
    TypeToken<NewResultResponse<Attachment>> getResultType(){

        return new TypeToken<NewResultResponse<Attachment>>(){};
    }
}
