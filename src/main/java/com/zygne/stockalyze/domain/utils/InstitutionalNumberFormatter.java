package com.zygne.stockalyze.domain.utils;

class InstitutionalNumberFormatter {

    public static int formatInstitutional(int original){
        return (original % 2 == 0) ? original : (original - 1);
    }
}
