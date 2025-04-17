package com.pplus.prnumberuser.core.code.common;

/**
 * <pre>
 *
 * 번호종류
 *
 * kr.co.pplus.core.code.common
 *  └ VirtualNumberTypeCode.java
 * <pre>
 * @author			: MyungJin.Shin
 * @Date			: 2016. 9. 12.
 * @Version			: 1.0.0
 */
public enum VirtualNumberTypeCode {
  
  /**
   *
   * 일반가상번호
   *
   * @type		: VirtualNumberTypeCode
   * @variable	: normal
   */
  normal, 
  
  /**
   *
   * 예약가상번호
   *
   * @type		: VirtualNumberTypeCode
   * @variable	: reserved
   */
  reserved, 
  
  /**
   *
   * 골드번호
   *
   * @type		: VirtualNumberTypeCode
   * @variable	: gold
   */
  gold;
  
}
