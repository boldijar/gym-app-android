package com.gym.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gym.app.R;
import com.gym.app.data.model.Availability;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.ApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.util.ByDay;
import biweekly.util.DayOfWeek;
import biweekly.util.Frequency;
import biweekly.util.Recurrence;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerInputEditText;
import io.blackbox_vision.datetimepickeredittext.view.TimePickerInputEditText;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.05.19
 */
public class AddAvailabilityActivity extends BaseActivity {

    private final static String ARG_SPOT = "spot";
    private final static String ARG_SCHEDULE = "schedule";
    private final static String ARG_AVAI = "avail";
    private int mSpotId;
    private String mSchedule;
    private int mAvaiId;

    private final SimpleDateFormat sDayFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat sHourFormat = new SimpleDateFormat("HH:mm");

    @BindView(R.id.from_date_picker)
    DatePickerInputEditText mFromDate;
    @BindView(R.id.from_time_picker)
    TimePickerInputEditText mFromTime;
    @BindView(R.id.to_date_picker)
    DatePickerInputEditText mToDate;
    @BindView(R.id.to_time_picker)
    TimePickerInputEditText mToTime;
    @BindView(R.id.repeating_event_checkbox)
    CheckBox mRepeatCheck;
    @BindView(R.id.repeating_layout)
    View mRepeatLayout;
    @BindView(R.id.day1)
    CheckBox mDay1;
    @BindView(R.id.day2)
    CheckBox mDay2;
    @BindView(R.id.day3)
    CheckBox mDay3;
    @BindView(R.id.day4)
    CheckBox mDay4;
    @BindView(R.id.day5)
    CheckBox mDay5;
    @BindView(R.id.day6)
    CheckBox mDay6;
    @BindView(R.id.day7)
    CheckBox mDay7;
    @BindView(R.id.repeat_frequency)
    Spinner mRequencySpinner;
    @BindView(R.id.repeat_time)
    EditText mRepeatTimeCount;
    @BindView(R.id.add_availability)
    TextView mAddText;
    @Inject
    ApiService mApiService;

    public static Intent createIntent(Context context, int spotId) {
        Intent intent = new Intent(context, AddAvailabilityActivity.class);
        intent.putExtra(ARG_SPOT, spotId);
        return intent;
    }

    public static Intent createIntent(Context context, int spotId, String schedule, int availabilityId) {
        Intent intent = new Intent(context, AddAvailabilityActivity.class);
        intent.putExtra(ARG_SPOT, spotId);
        intent.putExtra(ARG_SCHEDULE, schedule);
        intent.putExtra(ARG_AVAI, availabilityId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_availability);
        ButterKnife.bind(this);
        InjectionHelper.getApplicationComponent().inject(this);
        mFromDate.setManager(getSupportFragmentManager());
        mFromTime.setManager(getSupportFragmentManager());
        mToDate.setManager(getSupportFragmentManager());
        mToTime.setManager(getSupportFragmentManager());
        mSpotId = getIntent().getIntExtra(ARG_SPOT, -1);
        mSchedule = getIntent().getStringExtra(ARG_SCHEDULE);
        mAvaiId = getIntent().getIntExtra(ARG_AVAI, -1);
        if (mSchedule != null) {
            mAddText.setText("Save");
        }
        loadData();
    }

    private void loadData() {
        if (mSchedule == null) {
            return;
        }
        ICalendar calendar = null;
        try {
            calendar = Biweekly.parse(mSchedule).first();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (calendar.getEvents().size() == 0) {
            return;
        }
        VEvent event = calendar.getEvents().get(0);
        Date dateStart = event.getDateStart().getValue().getRawComponents().toDate();
        Date dateEnd = event.getDateEnd().getValue().getRawComponents().toDate();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(dateStart);
        endCalendar.setTime(dateEnd);
        mFromDate.setText(sDayFormat.format(dateStart));
        mToDate.setText(sDayFormat.format(dateEnd));
        mFromTime.setText(sHourFormat.format(dateStart));
        mToTime.setText(sHourFormat.format(dateEnd));
        mFromDate.setDate(startCalendar);
        mToDate.setDate(endCalendar);
        mFromTime.setTime(startCalendar);
        mToTime.setTime(endCalendar);

        if (event.getRecurrenceRule() == null) {
            mRepeatCheck.setChecked(false);
        } else {
            mRepeatCheck.setChecked(true);
            Recurrence recurrenceRule = event.getRecurrenceRule().getValue();
            switch (recurrenceRule.getFrequency()) {
                case DAILY:
                    mRequencySpinner.setSelection(0);
                    break;
                case WEEKLY:
                    mRequencySpinner.setSelection(1);
                    break;
                case MONTHLY:
                    mRequencySpinner.setSelection(2);
                    break;
                case YEARLY:
                    mRequencySpinner.setSelection(3);
                    break;
            }
            mRepeatTimeCount.setText(recurrenceRule.getInterval() + "");
            List<ByDay> byDays = recurrenceRule.getByDay();
            for (ByDay day : byDays) {
                if (day.getDay() == DayOfWeek.MONDAY) {
                    mDay1.setChecked(true);
                }
                if (day.getDay() == DayOfWeek.TUESDAY) {
                    mDay2.setChecked(true);
                }
                if (day.getDay() == DayOfWeek.WEDNESDAY) {
                    mDay3.setChecked(true);
                }
                if (day.getDay() == DayOfWeek.THURSDAY) {
                    mDay4.setChecked(true);
                }
                if (day.getDay() == DayOfWeek.FRIDAY) {
                    mDay5.setChecked(true);
                }
                if (day.getDay() == DayOfWeek.SATURDAY) {
                    mDay6.setChecked(true);
                }
                if (day.getDay() == DayOfWeek.SUNDAY) {
                    mDay7.setChecked(true);
                }
            }
        }
    }

    @OnCheckedChanged(R.id.repeating_event_checkbox)
    void repeatingChanged(boolean checked) {
        if (checked) {
            mRepeatLayout.setVisibility(View.VISIBLE);
        } else {
            mRepeatLayout.setVisibility(View.GONE);
        }
    }

    private Frequency getFrequency() {
        switch (mRequencySpinner.getSelectedItem().toString()) {
            case "days":
                return Frequency.DAILY;
            case "weeks":
                return Frequency.WEEKLY;
            case "years":
                return Frequency.YEARLY;
            case "months":
                return Frequency.MONTHLY;
            default:
                throw new RuntimeException("Invalid frequency, pls check arrays.xml");
        }

    }

    private List<DayOfWeek> getRecurrencyDays() {
        List<DayOfWeek> days = new ArrayList<>();
        if (mDay1.isChecked()) {
            days.add(DayOfWeek.MONDAY);
        }
        if (mDay2.isChecked()) {
            days.add(DayOfWeek.TUESDAY);
        }
        if (mDay3.isChecked()) {
            days.add(DayOfWeek.WEDNESDAY);
        }
        if (mDay4.isChecked()) {
            days.add(DayOfWeek.THURSDAY);
        }
        if (mDay5.isChecked()) {
            days.add(DayOfWeek.FRIDAY);
        }
        if (mDay6.isChecked()) {
            days.add(DayOfWeek.SATURDAY);
        }
        if (mDay7.isChecked()) {
            days.add(DayOfWeek.SUNDAY);
        }
        return days;
    }

    @OnClick(R.id.add_availability)
    void addAvailability() {
        ICalendar iCalendar = new ICalendar();
        VEvent vEvent = new VEvent();
        if (TextUtils.isEmpty(mFromTime.getText())
                || TextUtils.isEmpty(mFromDate.getText())
                || TextUtils.isEmpty(mToTime.getText())
                || TextUtils.isEmpty(mToDate.getText())) {
            Toast.makeText(this, "Please fill in all inputs!", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar from = mFromDate.getDate();
        from.set(Calendar.HOUR_OF_DAY, mFromTime.getTime().get(Calendar.HOUR_OF_DAY));
        Calendar to = mToDate.getDate();
        to.set(Calendar.HOUR_OF_DAY, mToTime.getTime().get(Calendar.HOUR_OF_DAY));

        if (from.getTimeInMillis() > to.getTimeInMillis()) {
            Toast.makeText(this, "From interval must be before the to interval!", Toast.LENGTH_SHORT).show();
            return;
        }
        vEvent.setDateStart(from.getTime());
        vEvent.setDateEnd(to.getTime());

        if (mRepeatCheck.isChecked()) {
            int interval = 0;
            try {
                interval = Integer.parseInt(mRepeatTimeCount.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number: " + mRepeatTimeCount.getText().toString(), Toast.LENGTH_SHORT).show();
            }
            Recurrence recurence = new Recurrence.Builder(getFrequency())
                    .interval(interval)
                    .byDay(getRecurrencyDays()).build();
            vEvent.setRecurrenceRule(recurence);
        }
        iCalendar.addEvent(vEvent);
        String icalFormat = Biweekly.write(iCalendar).go();
        if (mSchedule == null) {
            mApiService.addAvailability(Availability.createAvailabilityBody(icalFormat), mSpotId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getResultObserver());
        } else {
            mApiService.fixAvailability(Availability.createAvailabilityBody(icalFormat), mSpotId, mAvaiId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getResultObserver());
        }

    }

    private CompletableObserver getResultObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(AddAvailabilityActivity.this, "Please check if your intervals are valid.", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
