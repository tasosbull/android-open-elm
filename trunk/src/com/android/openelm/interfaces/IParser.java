package com.android.openelm.interfaces;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.android.openelm.ElmBankElement;


public interface IParser {
    public List<ElmBankElement> parse() throws XmlPullParserException, IOException ;
}
