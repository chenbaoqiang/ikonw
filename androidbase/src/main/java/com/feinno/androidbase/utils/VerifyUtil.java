package com.feinno.androidbase.utils;

import android.text.TextUtils;

import com.feinno.androidbase.utils.log.LogFeinno;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {

    /**
     * 邮箱正则判断
     *
     * @param eMAIL
     * @return
     */
    public static boolean EmailFormat(String eMAIL) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher mc = pattern.matcher(eMAIL);
        LogFeinno.i("RF_VerifyUtil", "======EmailFormat======" + mc.matches());
        return mc.matches();
    }


    /**
     * 校验手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
    /*电信
      中国电信手机号码开头数字
      2G/3G号段（CDMA2000网络）133、153、180、181、189
      4G号段 177
      联通
      中国联通手机号码开头数字
      2G号段（GSM网络）130、131、132、155、156
      3G上网卡145
      3G号段（WCDMA网络）185、186
      4G号段 176、185[1]
      移动
      中国移动手机号码开头数字
      2G号段（GSM网络）有134x（0-8）、135、136、137、138、139、150、151、152、158、159、182、183、184。
      3G号段（TD-SCDMA网络）有157、187、188
      3G上网卡 147
      4G号段 178
      补充
      14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
      170号段为虚拟运营商专属号段，170号段的 11 位手机号前四位来区分基础运营商，其中 “1700” 为中国电信的转售号码标识，“1705” 为中国移动，“1709” 为中国联通。*/
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }


    /**
     * 身份证号验证
     *
     * @param identityNum
     * @return
     */
    public static boolean identityFormat(String identityNum) {
        //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        //通过Pattern获得Matcher  
        Matcher idNumMatcher = idNumPattern.matcher(identityNum);
        return idNumMatcher.matches();
    }

    /**
     * 银行卡号长度校验
     *
     * @param bankNo
     * @return
     */
    public static boolean bankNumFormat(String bankNo) {
        Pattern idNumPattern = Pattern.compile("^[1-9]([0-9]|[ ]){17,24}$");
        //通过Pattern获得Matcher  
        Matcher idNumMatcher = idNumPattern.matcher(bankNo);
        LogFeinno.i("RF_VerifyUtil", "======验证的串=====" + bankNo);
        LogFeinno.i("RF_VerifyUtil", "=====zhengze====" + idNumMatcher.matches());
        return idNumMatcher.matches();
    }

    /**
     * 安全码长度校验
     *
     * @param bankNo
     * @return
     */
    public static boolean securityNumFormat(String bankNo) {
        Pattern idNumPattern = Pattern.compile("^\\d{3}$");
        //通过Pattern获得Matcher  
        Matcher idNumMatcher = idNumPattern.matcher(bankNo);
        LogFeinno.i("RF_VerifyUtil", "======验证的串=====" + bankNo);
        LogFeinno.i("RF_VerifyUtil", "=====zhengze====" + idNumMatcher.matches());
        return idNumMatcher.matches();
    }

    /**
     * 有效期长度校验
     *
     * @param bankNo
     * @return
     */
    public static boolean validateNumFormat(String bankNo) {
        Pattern idNumPattern = Pattern.compile("^\\d{4}$");
        //通过Pattern获得Matcher  
        Matcher idNumMatcher = idNumPattern.matcher(bankNo);
        LogFeinno.i("RF_VerifyUtil", "======验证的串=====" + bankNo);
        LogFeinno.i("RF_VerifyUtil", "=====zhengze====" + idNumMatcher.matches());
        return idNumMatcher.matches();
    }

    /**
     * 中文输入校验
     *
     * @param bankNo
     * @return
     */
    public static boolean userNameFormat(String bankNo) {
        Pattern idNumPattern = Pattern.compile("[u4e00-u9fa5]");
        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(bankNo);
        LogFeinno.i("RF_VerifyUtil", "======验证的串=====" + bankNo);
        LogFeinno.i("RF_VerifyUtil", "=====zhengze====" + idNumMatcher.matches());
        return idNumMatcher.matches();
    }

    /**
     * 大于0的正整数输入校验
     *
     * @param bankNo
     * @return
     */
    public static boolean posIntFormat(String intNo) {
        Pattern idNumPattern = Pattern.compile("^[1-9]{1}[\\d]*$");
        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(intNo);
        LogFeinno.d("RF_VerifyUtil", "======验证的串=====" + intNo + ";=====zhengze====" + idNumMatcher.matches());
        return idNumMatcher.matches();
    }

    /**
     * 判断是否是网址链接
     *
     * @param url
     * @return
     */
    public static String validateWebUrl(String content) {
        // 超链接解析
        String regax = "((file|gopher|news|nntp|telnet|http|ftp|https|ftps|sftp)://)?((([a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})*(\\.com|\\.edu|\\.gov|\\.int|\\.mil|\\.net|\\.org|\\.biz|\\.info|\\.pro|\\.name|\\.museum|\\.coop|\\.aero|\\.xxx|\\.idv|\\.au|\\.mo|\\.ru|\\.fr|\\.ph|\\.kr|\\.ca|\\.kh|\\.la|\\.my|\\.mm|\\.jp|\\.tw|\\.th|\\.hk|\\.sg|\\.it|\\.in|\\.id|\\.uk|\\.vn|\\.cn)))|(((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)(\\.((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)){3}))(:((6[0-5][0-5][0-3][0-5])|([1-5][0-9][0-9][0-9][0-9])|([0-9]{1,4})))?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?";
        Pattern pattern = Pattern.compile(regax);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            LogFeinno.i("verify", String.format("--->> content url start=%s, end=%s", matcher.start(), matcher.end()));
            int start = matcher.start();
            int end = matcher.end();
            String url = content.substring(start, end);
            LogFeinno.i("verify", "verify url:" + url);
            return url;
        }
        return null;
    }

    /**
     * 判断手机号码是否为移动号
     *
     * @param phone
     * @return
     */
    public static boolean isMobileNum(String phone) {
        Pattern pattern = Pattern.compile("^((13[4-9])|(14[7])|(15[0-2,7-9])|(18[2-4,7-8])|(17[8]))\\d{8}$");
        Matcher mc = pattern.matcher(phone);
        return mc.matches();
    }

    /**
     * 判断手机号是否为移动号码
     *
     * @param phone
     * @return
     */
    public static boolean isMobile(String phone) {
        String str = phone.substring(0, 3);
        switch (str) {
            case "134":
            case "135":
            case "136":
            case "137":
            case "138":
            case "139":
            case "147":
            case "150":
            case "151":
            case "152":
            case "157":
            case "158":
            case "159":
            case "182":
            case "183":
            case "184":
            case "187":
            case "188":
            case "178":
                return true;
        }
        return false;
    }

    /**
     * 手机号正则判断
     *
     * @param Phone
     * @return
     */
    public static boolean PhoneFormat(String Phone) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,0-9])|(17[0-9]))\\d{8}$");
        Matcher mc = pattern.matcher(Phone);
        return mc.matches();
    }

    /**
     * 判断手机号码是否为+86开头
     *
     * @param phone
     * @return
     */
    public static String checkPhone(String phone) {
        if (phone != null) {
            if (phone.length() > 0) {
                phone = phone.trim().replaceAll("\\-", "");
                phone = phone.trim().replaceAll("[\\s]", "");

                if (phone.startsWith("+86")) {
                    if (phone.length() > 3) {
                        String s = phone.substring(3, phone.length());
                        return s;
                    }
                } else if (phone.startsWith("86")) {
                    if (phone.length() > 2) {
                        String s = phone.substring(2, phone.length());
                        return s;
                    }
                }
            }
            return phone;
        } else {
            return "";
        }
    }


    public static String checkTel(String tel) {
        if (tel != null) {
            if (tel.length() > 0) {
                if (tel.startsWith("+86")) {
                    if (tel.length() > 3) {
                        String s = tel.substring(3, tel.length());
                        return s;
                    }
                } else if (tel.startsWith("86")) {
                    if (tel.length() > 2) {
                        String s = tel.substring(2, tel.length());
                        return s;
                    }
                }
            }
            return tel;
        }
        return "";
    }
}
