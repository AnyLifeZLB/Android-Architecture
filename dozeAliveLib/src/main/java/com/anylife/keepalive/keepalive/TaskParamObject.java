package com.anylife.keepalive.keepalive;


import android.os.Parcel;
import android.os.Parcelable;


/**
 * 包装一个异步任务
 *
 */
public class TaskParamObject implements Parcelable {

    private int periodTime;

    private Runnable runnable;

    public TaskParamObject(int periodTime, Runnable runnable) {
        this.periodTime = periodTime;
        this.runnable = runnable;
    }

    public int getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(int periodTime) {
        this.periodTime = periodTime;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.periodTime);
        dest.writeParcelable((Parcelable) this.runnable, flags);
    }

    public void readFromParcel(Parcel source) {
        this.periodTime = source.readInt();
        this.runnable = source.readParcelable(Runnable.class.getClassLoader());
    }

    protected TaskParamObject(Parcel in) {
        this.periodTime = in.readInt();
        this.runnable = in.readParcelable(Runnable.class.getClassLoader());
    }

    public static final Creator<TaskParamObject> CREATOR = new Creator<TaskParamObject>() {
        @Override
        public TaskParamObject createFromParcel(Parcel source) {
            return new TaskParamObject(source);
        }

        @Override
        public TaskParamObject[] newArray(int size) {
            return new TaskParamObject[size];
        }
    };
}
