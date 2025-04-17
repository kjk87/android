package com.root37.buflexz.core.network.upload;

import com.root37.buflexz.core.network.model.response.NewResultResponse;

/**
 * Created by j2n on 2016. 9. 30..
 */

public interface PplusUploadListener<T extends Object>{

    // 성공 처리
    void onResult(String tag, NewResultResponse<T> resultResponse);

    // 실패 처리
    void onFailure(String tag, NewResultResponse resultResponse);

}
