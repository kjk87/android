package com.pplus.prnumberuser.core.code.common;

/**
 * <pre>
 *
 * 번호종류
 *
 * kr.co.pplus.core.code.common
 *  └ NumberTypeCode.java
 * <pre>
 * @author			: SiHoon.Lee
 * @Date			: 2016. 9. 8.
 * @Version			: 1.0.0
 */
public enum NumberTypeCode {
  
  /**
   *
   * 일반번호
   *
   * @type		: NumberTypeCode
   * @variable	: phoneNumber
   */
  phoneNumber, 
  
  /**
   *
   * 핸드폰번호
   *
   * @type		: NumberTypeCode
   * @variable	: mobileNumber
   */
  mobileNumber, 
  
  /**
   *
   * 가상번호
   *
   * @type		: NumberTypeCode
   * @variable	: virtualNumber
   */
  virtualNumber;
  
}
