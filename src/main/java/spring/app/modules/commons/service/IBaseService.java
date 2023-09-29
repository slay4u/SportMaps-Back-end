package spring.app.modules.commons.service;

import spring.app.modules.commons.constant.SystemConstants;

public interface IBaseService {
    default double getPagesNum(long count) {
        double pagesNum = (double) count / SystemConstants.PAGE_ELEMENTS_AMOUNT;
        return Math.ceil(pagesNum);
    }
}
