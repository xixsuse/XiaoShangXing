package com.xiaoshangxing.yujian.Schoolfellow.ItemBean;

/**
 * Created by FengChaoQun
 * on 2016/10/22
 */

public class GradeItem extends BaseItemBean {
    public static final String GRADE_2012 = "2012级";
    public static final String GRADE_2013 = "2013级";
    public static final String GRADE_2014 = "2014级";
    public static final String GRADE_2015 = "2015级";
    public static final String GRADE_2016 = "2016级";

    private String grade;

    public GradeItem(String id, BaseItemBean parent, String grade) {
        super(id, parent, BaseItemBean.GRADE);
        this.grade = grade;
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getShowName() {
        return grade;
    }

    @Override
    public String getExText() {
        return null;
    }

    public String getGrade() {
        return grade.substring(0, 4);
    }
}
