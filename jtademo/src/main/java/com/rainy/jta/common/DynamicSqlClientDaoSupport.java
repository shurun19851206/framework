package com.rainy.jta.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.support.DaoSupport;

public class DynamicSqlClientDaoSupport extends DaoSupport implements InitializingBean{
    
    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {
        
    }
    
}

