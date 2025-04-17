package com.pplus.luckybol.core.network.upload;

import com.google.gson.reflect.TypeToken;
import com.pplus.luckybol.Const;
import com.pplus.luckybol.core.network.model.dto.Attachment;
import com.pplus.luckybol.core.network.model.response.NewResultResponse;

/**
 * Created by j2n on 2016. 9. 30..
 */

public class S3Upload extends AbstractUpload<Attachment>{

    public S3Upload(PplusUploadListener<Attachment> pplusCallback){

        super(pplusCallback);
    }

    @Override
    String getUploadUrl(){

        return Const.API_URL + "common/saveOnlyS3";
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
