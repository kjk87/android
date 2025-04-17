package com.pplus.prnumberbiz.core.code.common;

import android.content.Context;

import com.pplus.prnumberbiz.R;

/**
 * <pre>
 *
 * 페이지공개범위
 *
 * kr.co.pplus.core.code.common
 *  └ PageOpenBoundsCode.java
 * <pre>
 * @author			: SiHoon.Lee
 * @Date			: 2016. 9. 8.
 * @Version			: 1.0.0
 */
public enum PageOpenBoundsCode {

  /**
   *
   * 전체공개
   *
   * @type		: PageOpenBoundsCode
   * @variable	: everybody
   */
  everybody(R.string.word_public),
  
  /**
   *
   * FAN_GROUP
   *
   * @type		: PageOpenBoundsCode
   * @variable	: fan
   */
  fan(R.string.word_fan_open_bounds),
  
  /**
   *
   * 검색
   *
   * @type		: PageOpenBoundsCode
   * @variable	: search
   */
  search(R.string.word_search),
  
  /**
   *
   * 주소록에만 공개
   *
   * @type		: PageOpenBoundsCode
   * @variable	: contactList
   */
  contactList(R.string.word_public_contact),
  
  /**
   *
   * 비공개
   *
   * @type		: PageOpenBoundsCode
   * @variable	: nobody
   */
  nobody(R.string.word_private);

  public int value;

  public String getValue(Context context){


    return context.getString(value);
  }

  public void setValue(int value){

    this.value = value;
  }

  PageOpenBoundsCode(int value){

    this.value = value;
  }

}
