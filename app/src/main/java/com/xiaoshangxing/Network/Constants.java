package com.xiaoshangxing.Network;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface Constants {

    interface MyBatis {
        String GET = "get";
        String GET_COUNT = "getCount";
        String GET_MAX_ID = "getMaxId";
        String GET_BY_ID = "getById";
        String GET_BY_ALIAS = "getByAlias";
        String GET_BY_ENTITY = "getByEntity";
        String GET_BY_PARAM = "getByParam";
        String SAVE = "save";
        String SAVE_BATCH = "saveBatch";
        String UPDATE_BY_PARAM = "updateByParam";
        String UPDATE = "update";
        String DELETE_BY_PK = "deleteByPk";
        String DELETE_BY_PARAM = "deleteByParam";
        String DELETE_BATCH_BY_PKS = "deleteBatchByPks";
        String GET_PREVIOUS = "getPrevious";
        String GET_NEXT = "getNext";
        String FIND_BATCH_BY_PKS = "findBatchByPks";
        String FIND_BATCH = "findBatch";
    }

    interface Template {
        String CONTEXT_PATH = "ctx";
        String RESOURCES_PATH = "res";
        String TPL_INDEX = "index";
        String TPLS_FRONT = "main/front/";
        String TPL_STYLE_LIST = "sys_defined/list/list_style_";
        String TPL_STYLE_PAGE_CATEGORY = "sys_defined/page/page_style_";
        String TPL_STYLE_PAGE_POST = "sys_defined/page/post_";
        String TPL_SUFFIX = ".ftl";
        String DEFAULT_PAGE_CSS_CLASS = "cyan";
    }

    interface Encoding {
        String UTF8 = "UTF-8";
    }

    interface Cookie {
        int ACCOUNT_AGE = 2 * 7 * 24 * 60 * 60; // two weeks
    }

    interface System {
        String OK = "ok";
        String USER = "user";
        String TIPS = "tips";
        String ACCOUNT_ID = "accountId";
        String PASSWORD_RESET_CODE = "passwordRestCode";
        String PAGE_STRING = "page";
        String CAPTCHA = "captcha";
    }

    interface System2 {
        String OK = "ok";
        String COMPANY = "company";


    }

    interface System3 {
        String OK = "ok";
        String COMPANYS = "companys";


    }

    interface Common {
        /**
         * 最小日期
         */
        Date MIN_DATE = new Date(0L);

        /**
         * 默认页面大小
         */
        int DEFAULT_PAGE_SIZE = 10;

        String DETE_FORMAT = "yyyy-MM-dd";
    }
    
    interface Map4Redis{
    	Map<String, Object> map = new HashMap<>();
    }
}
