package com.pplus.prnumberbiz.core.network.model.request.params;

import com.pplus.prnumberbiz.core.network.model.dto.ImgUrl;

import java.util.List;

/**
 * Created by 김종경 on 2016-09-19.
 */
public class ParamsIntroMovie{

    private Long no;
    private List<ImgUrl> introMovieList;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public List<ImgUrl> getIntroMovieList(){

        return introMovieList;
    }

    public void setIntroMovieList(List<ImgUrl> introMovieList){

        this.introMovieList = introMovieList;
    }

    @Override
    public String toString(){

        return "ParamsIntroMovie{" + "no=" + no + ", introMovieList=" + introMovieList + '}';
    }
}
