package adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.test.worldtime.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import interfaces.OnItemClickListener;
import models.CountryTimezone;

import static android.support.v7.appcompat.R.styleable.View;

/**
 * Created by Muzammil on 09/01/2017.
 */

public class TimezoneAdapter extends RecyclerView.Adapter<TimezoneAdapter.MyViewHolder>
{

    private List<CountryTimezone> m_List;
    private OnItemClickListener mListener;

    public class MyViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView location, timezone, date_time;
        public ImageView flag;

        public MyViewHolder(android.view.View view)
        {
            super(view);
            location = (TextView)view.findViewById(R.id.location);
            timezone = (TextView)view.findViewById(R.id.timezone);
            date_time = (TextView)view.findViewById(R.id.datetime);
            flag = (ImageView)view.findViewById(R.id.flag);
            flag.setScaleType(ImageView.ScaleType.FIT_XY);
        }


        public void bind(final CountryTimezone item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public TimezoneAdapter(List<CountryTimezone> list, OnItemClickListener listener)
    {
        this.m_List = list;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timezone_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {

        CountryTimezone country = m_List.get(position);

        if(country.getIsHomeTimezone())
            holder.itemView.setBackgroundColor(Color.GREEN);

        holder.location.setText(country.getName());
        holder.timezone.setText(country.getTimezone());

        DateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy kk:mm"); //"MM/dd/yyyy HH:mm"


        String dateInString = formatter.format(country.getDateTime());
        String newDateInString = "";
        StringBuilder builder = new StringBuilder(dateInString);
        if(builder.charAt(17) == '2' && dateInString.charAt(18) == '4')
        {
            builder.replace(17, 19, "00");// = dateInString.replaceFirst("24", "00");
            newDateInString = builder.toString();
        }

        if(country.getDateTime() != null) holder.date_time.setText(newDateInString.trim().length() > 0 ? newDateInString : dateInString);
        if(country.getFlag() != null) holder.flag.setImageBitmap(country.getFlag());

        holder.bind(m_List.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        if(m_List == null) return 0;
        return m_List.size();
    }
}
