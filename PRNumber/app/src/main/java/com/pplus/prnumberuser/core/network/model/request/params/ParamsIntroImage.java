package com.pplus.prnumberuser.core.network.model.request.params;

import com.pplus.prnumberuser.core.network.model.dto.ImgUrl;

import java.util.List;

/**
 * Created by 김종경 on 2016-09-19.
 */
public class ParamsIntroImage{

    private Long no;
    private List<ImgUrl> introImageList;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public List<ImgUrl> getIntroImageList(){

        return introImageList;
    }

    public void setIntroImageList(List<ImgUrl> introImageList){

        this.introImageList = introImageList;
    }

    @Override
    public String toString(){

        return "ParamsIntroImage{" + "no=" + no + ", introImageList=" + introImageList + '}';
    }
}
