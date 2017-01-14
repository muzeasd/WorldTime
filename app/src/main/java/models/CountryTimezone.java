package models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Muzammil on 09/01/2017.
 */

public class CountryTimezone implements Parcelable
{
    private int id = -1;
    private String name = "";
    private String timezone = "";
    private Date dateTime = null;
    private boolean isHomeTimezone = false;
    private Bitmap flag = null;

    public CountryTimezone(int id, String name, Bitmap bitmap)
    {
        this.id = id;
        this.name = name;
        this.flag = bitmap;
    }


    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean getIsHomeTimezone() {
        return isHomeTimezone;
    }

    public void setIsHomeTimezone(boolean isHomeTimezone) {
        this.isHomeTimezone = isHomeTimezone;
    }

    public Bitmap getFlag() {
        return flag;
    }

    public void setFlag(Bitmap flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    private CountryTimezone(Parcel in)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        //try
        //{
            this.id = in.readInt();
            this.name = in.readString();
            this.timezone = in.readString();
            this.dateTime = new Date(in.readLong());
            this.isHomeTimezone = in.readByte() != 0;
            this.flag = in.readParcelable(getClass().getClassLoader()); //CREATOR.createFromParcel(in);
        //}
        //catch (ParseException ex) {
        //    Logger.getLogger(CountryTimezone.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }

    public static final Parcelable.Creator<CountryTimezone> CREATOR
            = new Parcelable.Creator<CountryTimezone>() {
        public CountryTimezone createFromParcel(Parcel in) {
            return new CountryTimezone(in);
        }

        public CountryTimezone[] newArray(int size) {
            return new CountryTimezone[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        if(this.id != -1)
            dest.writeInt(this.id);
        if(this.name.trim().length() > 0)
            dest.writeString(this.name);
        if(this.timezone.trim().length() > 0)
            dest.writeString(this.timezone);
        if(this.dateTime != null)
            dest.writeLong(this.dateTime.getTime());

        dest.writeByte((byte) (isHomeTimezone ? 1 : 0));

        if(this.flag != null)
            this.flag.writeToParcel(dest, flags);
    }
}
