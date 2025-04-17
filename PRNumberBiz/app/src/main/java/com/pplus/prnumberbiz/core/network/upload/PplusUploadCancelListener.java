package com.pplus.prnumberbiz.core.network.upload;

import java.util.Set;

/**
 * Created by j2n on 2016. 10. 4..
 */

public interface PplusUploadCancelListener{

    void onCancel(Set<String> tagSet);

}
