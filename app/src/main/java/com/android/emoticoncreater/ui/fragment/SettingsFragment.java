package com.android.emoticoncreater.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.ui.activity.SettingsActivity;
import com.android.emoticoncreater.utils.PrefUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 设置
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (SettingsActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    private void recreateMain() {
        mActivity.onRecreate();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        findPreference(Constants.THEME).setOnPreferenceClickListener(mPreferenceClick);
        findPreference(Constants.LANGUAGE).setOnPreferenceClickListener(mPreferenceClick);
    }

    private void showThemeChooser() {
        final List<String> valueList = Arrays.asList(getResources().getStringArray(R.array.theme_list));
        String theme = PrefUtils.getTheme();
        int selectIndex = valueList.indexOf(theme);
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.choose_theme)
                .setSingleChoiceItems(R.array.theme_list, selectIndex, (dialog1, which) -> {
                    dialog1.dismiss();
                    PrefUtils.set(Constants.THEME, valueList.get(which));
                    recreateMain();
                })
                .show();
    }

    private void showLanguageList() {
        final List<String> valueList
                = Arrays.asList(getResources().getStringArray(R.array.language_id_array));
        String language = PrefUtils.getLanguage();
        int index = valueList.indexOf(language);

        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.language)
                .setSingleChoiceItems(R.array.language_array, index, (dialog, which) -> {
                    dialog.dismiss();
                    PrefUtils.set(Constants.LANGUAGE, valueList.get(which));
                    recreateMain();
                })
                .show();
    }

    private Preference.OnPreferenceClickListener mPreferenceClick = preference -> {
        switch (preference.getKey()) {
            case Constants.THEME:
                showThemeChooser();
                return true;
            case Constants.LANGUAGE:
                showLanguageList();
                return true;
        }
        return false;
    };
}
