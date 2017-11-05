package com.abubakar.watergatesecurity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Time t = new Time(i,i1,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String time = formatter.format(t);
        TextView tv_time = getActivity().findViewById(R.id.tv_time);
        tv_time.setText(time);
    }
}
