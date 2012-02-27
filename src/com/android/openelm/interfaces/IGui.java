package com.android.openelm.interfaces;

import com.android.openelm.ElmBankElement;

public interface IGui {
    public void  AddError(String aError);
    public void ProcMessages();
    public int GetPortSelected();
    public int GetElmProtoSelected();
    public int GetBankLayoutElementsSelected();
    public void SetPidValue(int bankPosition, ElmBankElement elem, double value);

}
