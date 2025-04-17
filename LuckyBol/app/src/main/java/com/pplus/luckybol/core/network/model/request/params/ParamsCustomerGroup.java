package com.pplus.luckybol.core.network.model.request.params;

import com.pplus.luckybol.core.network.model.dto.Customer;
import com.pplus.luckybol.core.network.model.dto.No;

import java.util.List;

/**
 * Created by 김종경 on 2016-09-19.
 */
public class ParamsCustomerGroup{
    private No group;
    private List<Customer> customerList;

    public No getGroup(){

        return group;
    }

    public void setGroup(No group){

        this.group = group;
    }

    public List<Customer> getCustomerList(){

        return customerList;
    }

    public void setCustomerList(List<Customer> customerList){

        this.customerList = customerList;
    }

    @Override
    public String toString(){

        return "ParamsCustomerGroup{" + "group=" + group + ", customerList=" + customerList + '}';
    }
}
