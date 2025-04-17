package com.pplus.prnumberuser.core.code.common;

/**
 * <pre>
 *
 * 번호종류
 *
 * kr.co.pplus.core.code.common
 *  └ VirtualNumberMappingTypeCode.java
 * <pre>
 * @author			: MyungJin.Shin
 * @Date			: 2016. 9. 12.
 * @Version			: 1.0.0
 */
public enum VirtualNumberMappingTypeCode {
  
  /**
   *
   * FRONT매핑
   *
   * @type		: VirtualNumberMappingTypeCode
   * @variable	: front
   */
  front, 
  
  /**
   *
   * CMS매핑
   *
   * @type		: VirtualNumberMappingTypeCode
   * @variable	: cms
   */
  cms, 
  
  /**
   *
   * BULK
   *
   * @type      : VirtualNumberMappingTypeCode
   * @variable  : bulk
   */
  bulk, 
  
  /**
   *
   * PMS
   *
   * @type      : VirtualNumberMappingTypeCode
   * @variable  : pms
   */
  pms, 
  
  /**
   *
   * CS처리로 매핑해제
   *
   * @type      : VirtualNumberMappingTypeCode
   * @variable  : cs
   */
  cs, 
  
  /**
   *
   * 자동권한이전 매핑해제
   *
   * @type		: VirtualNumberMappingTypeCode
   * @variable	: auto
   */
  auto;
 
}
