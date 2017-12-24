package com.example.tony.nodandshake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HistoryValue {
    private float[][] mValue;
    private int mSize;
    private int PrePoint;
    private float[] EachData = new float[2];

    public HistoryValue(int size)
    {
        mSize = size;
        mValue = new float[2][mSize];
        PrePoint = 0;
    }

    public void AddValue(float time, float tmp)
    {
        mValue[0][PrePoint] = time;
        mValue[1][PrePoint] = tmp;
        PrePoint = (PrePoint + 1)%mSize;
    }

    public float[] GetIndex(int index)
    {
        if (PrePoint + index - 1 >= 0)
        {
            EachData[0] = mValue[0][PrePoint + index - 1];
            EachData[1] = mValue[1][PrePoint + index - 1];
        }
        else
        {
            int ActualIndex = PrePoint + index + mSize - 1;
            EachData[0] = mValue[0][ActualIndex];
            EachData[1] = mValue[1][ActualIndex];
        }
        return EachData;
    }

    public float GetMaxValue()
    {
        float tmp = 0;
        for (int i = 0;i < mSize;i++)
        {
            if (mValue[1][i] > tmp)
                tmp = mValue[1][i];
        }
        return tmp;
    }

    public float GetMinValue()
    {
        float tmp = 0;
        for (int i = 0;i < mSize;i++)
        {
            if (mValue[1][i] < tmp)
                tmp = mValue[1][i];
        }
        return tmp;
    }
}
