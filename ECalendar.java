/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppSettings;

import java.util.Calendar;

/**
 *
 * @author wac
 */
public class ECalendar {

    public ECalendar() {
    }
    
    //Test Main
   /* public static void main(String[] args) throws Exception {
        // TODO code application logic here
        int year, month, date;
        year = 1900;
        month = 4;
        date = 29;
        String date1=convertToGregorian(year, month, date);
        System.out.println(date1);
        
        String str[] = date1.split("/");
        date = Integer.parseInt(str[0]);
        month = Integer.parseInt(str[1]);
        year = Integer.parseInt(str[2]);
        date1=convertToEthiopic(year, month, date);
        System.out.println(date1);
        
    }*/

    private int startOfTheEthiopianYear(int year) {
        //Returns the first day of the year

        //magic formula gives start of year
        int newYearDay = (year / 100) - (year / 400) - 4;

        // if the prev ethiopian year is a leap year, new-year occrus on 12th
        if (((year - 1) % 4) == 3) {
            newYearDay += 1;
        }
        return newYearDay;
    }

    //Gregorian date object representation of provided Ethiopian date
    public String convertToGregorian(int year, int month, int date) throws Exception {

        final String greg;
        greg = toGregorian(year, month, date);
        return greg;

    }

    private String toGregorian(int year, int month, int day) throws Exception {
        int gregorianDate = 0;
        //Prevent incorrect inputs
        int[] inputs = {year, month, day};
        for (int i = 0; i < inputs.length; i++) {
            //System.out.println(inputs[i]);
            if (inputs[i] == 0 || inputs.length != 3) {
                throw new Exception("Malformed input can't be converted.");
            }
        }

        // Ethiopian new year in Gregorian calendar
        int newYearDay = startOfTheEthiopianYear(year);
        //September (Ethiopian) sees 7y difference
        int gregorianYear = year + 7;

        //September (Ethiopian) sees 7y difference
        //Number of days in gregorian months
        //starting with September (index 1)
        // Index 0 is reserved for leap years switches.
        int[] gregorianMonths = {0, 30, 31, 30, 31, 31, 28, 31, 30, 31, 30, 31, 31, 30};

        // if next gregorian year is leap year, February has 29 days.
        int nextYear = gregorianYear + 1;

        if (((nextYear % 4) == 0 && ((nextYear % 100) != 0)) || (nextYear % 400 == 0)) {
            gregorianMonths[6] = 29;
        }

        //calculate number of days up to that date
        int until = ((month - 1) * 30) + day;
        // mysterious rule
        if (until <= 37 && year <= 1575) {
            until += 28;
            gregorianMonths[0] = 31;
        } else {
            until += newYearDay - 1;
        }

        //if ethiopian year is leap year, paguemain has six days
        if ((year - 1 % 4 == 3)) {
            until += 1;
        }

        //calculate month and date incremently
        int m = 0;
        for (int i = 0; i < gregorianMonths.length; i++) {
            if (until <= gregorianMonths[i]) {
                m = i;
                gregorianDate = until;
                break;
            } else {
                m = i;
                until -= gregorianMonths[i];
            }
        }
        //if m > 4, we're already on next Gregorian year
        if (m > 4) {
            gregorianYear += 1;
        }

        //Gregorian months ordered according to Ethiopian
        int[] order = {8, 9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int gregorianMonth = order[m];
        Calendar cal = Calendar.getInstance();

        return gregorianDate + "/" + gregorianMonth + "/" + gregorianYear;
    }

    public String convertToEthiopic(int year, int month, int date) throws Exception {
        final String greg;
        greg = toEthiopic(year, month, date);
        return greg;
    }

    private String toEthiopic(int year, int month, int date) throws Exception {
        //Prevent incorrect inputs
        int[] inputs = {year, month, date};
        for (int i = 0; i < inputs.length; i++) {
            //System.out.println(inputs[i]);
            if (inputs[i] == 0 || inputs.length != 3) {
                throw new Exception("Malformed input can't be converted.");
            }
        }

        //date between 5 and 14 of May 1582 are invalid
        if ((month == 10 && date >= 5) && (date <= 15 && year == 1582)) {
            throw new Exception("Invalid Date between 5-14 May 1582");
        }
        //Number of days in gregorian months
        // starting with January (index 1)
        //Index 0 is reserved for leap years switches.
        int[] gregorianMonths = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        //Number of days in ethiopian months
        //starting with January (index 1)
        //Index 0 is reserved for leap years switches.
        int[] ethiopianMonths = {0, 30, 30, 30, 30, 30, 30, 30, 30, 30, 5, 30, 30, 30, 30};

        //if gregorian leap year, February has 29 days.
        if ((year % 4) == 0 && (year % 100) != 0 || (year % 400) == 0) {
            gregorianMonths[2] = 29;
        }

        //September sees 8y difference
        int ethiopianYear = year - 8;
        if ((ethiopianYear % 4) == 3) {
            ethiopianMonths[10] = 6;
        } else {
            ethiopianMonths[10] = 5;
        }

        //Ethiopian new year in Gregorian calendar
        int newYearDay = startOfTheEthiopianYear(year - 8);

        //calculate number of days up to that date
        int until = 0;
        for (int i = 0; i < month; i++) {
            until += gregorianMonths[i];
        }
        until += date;
        //update tahissas (december) to match january 1st
        int tahissas = 0;
        if (ethiopianYear % 4 == 0) {
            tahissas = 26;
        } else {
            tahissas = 25;
        }

        //take into account the 1582 change
        if (year < 1582) {
            ethiopianMonths[1] = 0;
            ethiopianMonths[2] = tahissas;
        } else if (until <= 277 && year == 1582) {
            ethiopianMonths[1] = 0;
            ethiopianMonths[2] = tahissas;
        } else {
            tahissas = newYearDay - 3;
            ethiopianMonths[1] = tahissas;
             
        }

        //calculate month and date incremently
        int m = 0;
        int ethiopianDate = 0;
        for (m = 1; m < ethiopianMonths.length; m++) {
            if (until <= ethiopianMonths[m]) {
                if (m == 1 || ethiopianMonths[m] == 0) {
                    ethiopianDate = until + (30 - tahissas);
                    //System.out.println(tahissas);
                    //System.out.println(until);
                    break;
                } else {
                    ethiopianDate = until;
                    break;
                }

            } else {
                until -= ethiopianMonths[m];
            }
        }

        //if m > 4, we're already on next Ethiopian year
        if (m > 10) {
            ethiopianYear += 1;
        }
        //Ethiopian months ordered according to Gregorian
        int[] order = {0, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1, 2, 3, 4};
        int ethiopianMonth = order[m];
        return ethiopianDate + "/" + ethiopianMonth + "/" + ethiopianYear;
    }
}
