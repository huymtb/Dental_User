package com.prostage.l_pha.dental_user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.activity.MainActivity;
import com.prostage.l_pha.dental_user.model.server_model.admin_model.AdminModel;
import com.prostage.l_pha.dental_user.model.server_model.admin_model.WorkingSetModel;
import com.prostage.l_pha.dental_user.utils.UtilsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private TextView txtAddress, txtTel, txtCloseDay, txtTechnique;

    private TextView txtMondayMorning, txtMondayAfternoon;
    private TextView txtTuesdayMorning, txtTuesdayAfternoon;
    private TextView txtWednesdayMorning, txtWednesdayAfternoon;
    private TextView txtThursdayMorning, txtThursdayAfternoon;
    private TextView txtFridayMorning, txtFridayAfternoon;
    private TextView txtSaturdayMorning, txtSaturdayAfternoon;
    private TextView txtSundayMorning, txtSundayAfternoon;
    private TextView txtHolidayMorning, txtHolidayAfternoon;

    private AdminModel adminModel;
    private List<WorkingSetModel> workingSetModels;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View infoView = inflater.inflate(R.layout.fragment_info, container, false);

        txtAddress = (TextView) infoView.findViewById(R.id.textViewAddress);
        txtTel = (TextView) infoView.findViewById(R.id.textViewTel);
        txtCloseDay = (TextView) infoView.findViewById(R.id.textViewCloseDay);
        txtTechnique = (TextView) infoView.findViewById(R.id.textViewTechnique);

        txtMondayMorning = (TextView) infoView.findViewById(R.id.textViewMondayMorning);
        txtMondayAfternoon = (TextView) infoView.findViewById(R.id.textViewMondayAfternoon);

        txtTuesdayMorning = (TextView) infoView.findViewById(R.id.textViewTuesdayMorning);
        txtTuesdayAfternoon = (TextView) infoView.findViewById(R.id.textViewTuesdayAfternoon);

        txtWednesdayMorning = (TextView) infoView.findViewById(R.id.textViewWednesdayMorning);
        txtWednesdayAfternoon = (TextView) infoView.findViewById(R.id.textViewWednesdayAfternoon);

        txtThursdayMorning = (TextView) infoView.findViewById(R.id.textViewThursdayMorning);
        txtThursdayAfternoon = (TextView) infoView.findViewById(R.id.textViewThursdayArternoon);

        txtFridayMorning = (TextView) infoView.findViewById(R.id.textViewFridayMorning);
        txtFridayAfternoon = (TextView) infoView.findViewById(R.id.textViewFridayAfternoon);

        txtSaturdayMorning = (TextView) infoView.findViewById(R.id.textViewSatturdayMorning);
        txtSaturdayAfternoon = (TextView) infoView.findViewById(R.id.textViewSatturdayAfternoon);

        txtSundayMorning = (TextView) infoView.findViewById(R.id.textViewSundayMorning);
        txtSundayAfternoon = (TextView) infoView.findViewById(R.id.textViewSundayAfternoon);

        txtHolidayMorning = (TextView) infoView.findViewById(R.id.textViewHolidayMorning);
        txtHolidayAfternoon = (TextView) infoView.findViewById(R.id.textViewHolidayAfternoon);

        return infoView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adminModel = new AdminModel();
        workingSetModels = new ArrayList<>();

        adminModel = ((MainActivity) getActivity()).getAdminModel();
        workingSetModels = ((MainActivity) getActivity()).getListWorkingSetModel();

        txtAddress.setText(adminModel.getAddress() != null ? adminModel.getAddress() : "");
        txtTel.setText(adminModel.getTel() != null ? adminModel.getTel() : "");
        txtCloseDay.setText(adminModel.getCloseDays() != null ? adminModel.getCloseDays() : "");
        txtTechnique.setText(adminModel.getTechnique() != null ? adminModel.getTechnique() : "");

        if (workingSetModels.size() > 0) {
            setValueToTextView(txtMondayMorning, txtMondayAfternoon, workingSetModels.get(0));
            setValueToTextView(txtTuesdayMorning, txtTuesdayAfternoon, workingSetModels.get(1));
            setValueToTextView(txtWednesdayMorning, txtWednesdayAfternoon, workingSetModels.get(2));
            setValueToTextView(txtThursdayMorning, txtThursdayAfternoon, workingSetModels.get(3));
            setValueToTextView(txtFridayMorning, txtFridayAfternoon, workingSetModels.get(4));
            setValueToTextView(txtSaturdayMorning, txtSaturdayAfternoon, workingSetModels.get(5));
            setValueToTextView(txtSundayMorning, txtSundayAfternoon, workingSetModels.get(6));
            setValueToTextView(txtHolidayMorning, txtHolidayAfternoon, workingSetModels.get(7));
        }
    }

    private void setValueToTextView(TextView morningText, TextView afternoonText, WorkingSetModel workingSet) {
        morningText.setText("x");
        afternoonText.setText("x");

        if (workingSet.getFirstShiftFromHour() + workingSet.getFirstShiftFromMin() + workingSet.getFirstShiftToHour() + workingSet.getFirstShiftToMin() > 0) {
            morningText.setText(UtilsHelper.formatTime(workingSet.getFirstShiftFromHour()) + ":" + UtilsHelper.formatTime(workingSet.getFirstShiftFromMin())
                    + "~" + UtilsHelper.formatTime(workingSet.getFirstShiftToHour()) + ":" + UtilsHelper.formatTime(workingSet.getFirstShiftToMin()));
        }

        if (workingSet.getSecondShiftFromHour() + workingSet.getSecondShiftFromMin() + workingSet.getSecondShiftToHour() + workingSet.getSecondShiftToMin() > 0) {
            afternoonText.setText(UtilsHelper.formatTime(workingSet.getSecondShiftFromHour()) + ":" + UtilsHelper.formatTime(workingSet.getSecondShiftFromMin())
                    + "~" + UtilsHelper.formatTime(workingSet.getSecondShiftToHour()) + ":" + UtilsHelper.formatTime(workingSet.getSecondShiftToMin()));
        }
    }

}
