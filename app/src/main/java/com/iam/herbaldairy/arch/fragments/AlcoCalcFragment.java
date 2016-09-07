package com.iam.herbaldairy.arch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iam.herbaldairy.util.Calculator;
import com.iam.herbaldairy.util.Editor;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.widget.Header;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

public class AlcoCalcFragment extends Fragment implements Header.HeaderManipulation {

    View view;

    EditText alcSourcePercentET;
    EditText alcResultPercentET;
    EditText spiritVolumeET;

    Button calculateWaterButton;
    Button calculateSpiritButton;

    Text text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alco_calc_fragment, container, false);

        alcResultPercentET = (EditText) view.findViewById(R.id.alcresult);
        alcResultPercentET.addTextChangedListener(Editor.integerTextWatcher(alcResultPercentET, 2));
        alcSourcePercentET = (EditText) view.findViewById(R.id.alcsource);
        alcSourcePercentET.addTextChangedListener(Editor.integerTextWatcher(alcSourcePercentET, 2));
        spiritVolumeET = (EditText) view.findViewById(R.id.spirit);
        calculateWaterButton = (Button) view.findViewById(R.id.calculatewater);
        calculateSpiritButton = (Button) view.findViewById(R.id.calculatespirit);
        text = (Text) view.findViewById(R.id.text);

        calculateWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFieldsCompleted()) {
                    final String svettext = spiritVolumeET.getText().toString();
                    double spiritVolume = svettext.equals("") ? 0 : Double.parseDouble(svettext);
                    final String aspettext = alcSourcePercentET.getText().toString();
                    int sourcePercent = aspettext.equals("") ? 0 : Integer.parseInt(aspettext);
                    final String arpettext = alcResultPercentET.getText().toString();
                    int resultPercent = arpettext.equals("") ? 0 : Integer.parseInt(arpettext);
                    final double result = Calculator.waterVolumeForDiluteSpirit(spiritVolume, sourcePercent, resultPercent);
                    text.setText(
                            "Для разбавления " +
                                    spiritVolume +
                                    "л. " +
                                    sourcePercent +
                                    "% спирта до " +
                                    resultPercent +
                                    "% " +
                                    (result < 0 ? "у" : "до") +
                                    "бавьте " +
                                    String.format("%.3f", Math.abs(result)) +
                                    " л. воды"
                    );
                } else {
                    Toast.makeText(calculateWaterButton.getContext(), "Сначала заполите поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        calculateSpiritButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFieldsCompleted()) {
                    double spiritVolume = Double.parseDouble(spiritVolumeET.getText().toString());
                    int sourcePercent = Integer.parseInt(alcSourcePercentET.getText().toString());
                    int resultPercent = Integer.parseInt(alcResultPercentET.getText().toString());
                    final double result = Calculator.spiritVolumeForSpirit(spiritVolume, sourcePercent, resultPercent);
                    text.setText(
                            sourcePercent > resultPercent ?
                            "Для увеличения концентрации " +
                                    spiritVolume +
                                    "л. " +
                                    sourcePercent +
                                    "% спирта до " +
                                    resultPercent +
                                    "% " +
                                    (result < 0 ? "у" : "до") +
                                    "бавьте " +
                                    String.format("%.3f", Math.abs(result)) +
                                    " л. спирта"
                                    :
                                    "Нельзя разбавить спирт спиртом"
                    );
                } else {
                    Toast.makeText(calculateWaterButton.getContext(), "Сначала заполите поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((Header.FragmentDataSender)getContext()).onFragmentOpen(this);

        return view;
    }

    @Override
    public svg leftIcon() {
        return svg.toggle;
    }

    @Override
    public View.OnClickListener leftIconAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Header.ToggleClicker)getActivity()).onToggleClick();
            }
        };
    }

    @Override
    public svg rightIcon1() {
        return null;
    }

    @Override
    public View.OnClickListener rightIcon1Action() {
        return null;
    }

    @Override
    public svg rightIcon2() {
        return null;
    }

    @Override
    public View.OnClickListener rightIcon2Action() {
        return null;
    }

    @Override
    public String headerTextButton() {
        return null;
    }

    @Override
    public View.OnClickListener textAction() {
        return null;
    }

    @Override
    public String headerTitle() {
        return "CALCULATOR";
    }

    @Override
    public String headerSubtitle() {
        return null;
    }

    private boolean allFieldsCompleted() {
        return
                !alcResultPercentET.getText().toString().equals("") &&
                        !alcSourcePercentET.getText().toString().equals("") &&
                        !spiritVolumeET.getText().toString().equals("");
    }


}
