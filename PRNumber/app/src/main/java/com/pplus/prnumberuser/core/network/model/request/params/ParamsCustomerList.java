package com.pplus.prnumberuser.core.network.model.request.params;

import com.pplus.prnumberuser.core.network.model.dto.Customer;
import com.pplus.prnumberuser.core.network.model.dto.No;

import java.util.List;

/**
 * Created by 김종경 on 2016-09-19.
 */
public class ParamsCustomerList{

    private No page;
    private List<Customer> customerList;

    public ParamsCustomerList(){

    }

    public No getPage(){

        return page;
    }

    public void setPage(No page){

        this.page = page;
    }

    public List<Customer> getCustomerList(){

        return customerList;
    }

    public void setCustomerList(List<Customer> customerList){

        this.customerList = customerList;
    }
}
