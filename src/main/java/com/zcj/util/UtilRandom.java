package com.zcj.util;

import java.util.Date;
import java.util.Random;

import com.zcj.util.random.DateRandom;
import com.zcj.util.random.IdCardGenerator;
import com.zcj.util.random.RandomValue;

public class UtilRandom {

    /**
     * 获取随机数
     */
    public static int getRandom(final int min, final int max) {
        int tmp = Math.abs(new Random().nextInt());
        return tmp % (max - min + 1) + min;
    }

    /**
     * 随机生成身份证号码
     */
    public static String getIdcard() {
        IdCardGenerator cardGen = new IdCardGenerator();
        return cardGen.generate();
    }

    /**
     * 随机生成EMAIL
     */
    public static String getEmail() {
        return RandomValue.getEmail(5, 12);
    }

    /**
     * 随机生成姓名
     */
    public static String getChineseName() {
        return RandomValue.getChineseName();
    }

    /**
     * 随机生成地址
     */
    public static String getAddress() {
        return RandomValue.getRoad();
    }

    /**
     * 随机生成日期
     */
    public static String getDateString() {
        Date randomDate = DateRandom.randomDate("2000-01-01", "2015-12-31");
        return UtilDate.SDF_DATETIME.get().format(randomDate);
    }

    /**
     * 随机生成11位手机号码
     */
    public static String getTel() {
        return RandomValue.getTel();
    }

}
