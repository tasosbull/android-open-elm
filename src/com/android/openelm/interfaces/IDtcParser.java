package com.android.openelm.interfaces;

import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
import com.android.openelm.ElmDtcElement;

public interface IDtcParser {
    public List<ElmDtcElement> parse() throws XmlPullParserException, IOException ;
}
