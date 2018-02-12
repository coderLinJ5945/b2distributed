package com.lin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
/**
 * 日期操作扩展类
 * @author linj
 */
public class DatetimeUtils extends DateUtils {

	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化时间
     * 把相应时间转化为相应格式
     *
     **/
    public static Date formatDate(Date date, String pattern)
    {

        Date reDate = new Date();
        if (date != null)
        {
            SimpleDateFormat ss = new SimpleDateFormat(
                    pattern);
            String orderdate = ss.format(date);
            //reDate = new Date();
            try
            {
                reDate = java.sql.Date.valueOf(orderdate);
            } catch (Exception e)
            {

            }
        }
        return reDate;
    }
	/**
	 * 把相应时间转化为相应格式的字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToString(Date date, String pattern)
	{
		String reDate = "";
		if (date != null)
		{
			SimpleDateFormat bartDateFormat = new SimpleDateFormat(pattern);
			try
			{
				reDate = bartDateFormat.format(date);
			} catch (Exception e)
			{

			}
		}
		return reDate;
	}

	public static String dateToString(Date date)
	{
		String reDate = "";
		if (date != null)
		{
			SimpleDateFormat bartDateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
			try
			{
				reDate = bartDateFormat.format(date);
			} catch (Exception e)
			{

			}
		}
		return reDate;
	}

	/**
	 * 由于apache日期验证格式严格，此方法为不严格实现
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException 
	 */
	public static Date stringToDate(String date,String pattern) throws ParseException{
		if(StringUtils.isEmpty(date)){
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.parse(date);
	}
	/**
	 * 获取俩个日期之间的天数
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long getDaySub(Date beginDate,Date endDate){
		beginDate = formatDate(beginDate, "yyyy-MM-dd");
		endDate = formatDate(endDate, "yyyy-MM-dd");
        return (endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
	}
	
	/**
	 * 获取俩个日期之间的分钟数
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long getMinSub(Date beginDate,Date endDate){
        return (endDate.getTime()-beginDate.getTime())/(60*1000);    
	}
}
