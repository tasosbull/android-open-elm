package com.android.openelm.interfaces;

public interface IGui {
    public void  AddError(String aError);
    public void ProcMessages();
    public int GetPortSelected();
    public String GetElmProtoSelected();
    public int GetBankLayoutElementsSelected();

}
