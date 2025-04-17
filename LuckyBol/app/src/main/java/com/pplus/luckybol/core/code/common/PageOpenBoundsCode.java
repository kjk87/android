package com.pplus.luckybol.core.code.common;

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
  everybody,
  
  /**
   *
   * FAN_GROUP
   *
   * @type		: PageOpenBoundsCode
   * @variable	: fan
   */
  fan,
  
  /**
   *
   * 검색
   *
   * @type		: PageOpenBoundsCode
   * @variable	: search
   */
  search,
  
  /**
   *
   * 주소록에만 공개
   *
   * @type		: PageOpenBoundsCode
   * @variable	: contactList
   */
  contactList,
  
  /**
   *
   * 비공개
   *
   * @type		: PageOpenBoundsCode
   * @variable	: nobody
   */
  nobody;

}
