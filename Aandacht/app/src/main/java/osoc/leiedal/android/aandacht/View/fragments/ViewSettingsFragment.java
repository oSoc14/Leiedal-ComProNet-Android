package osoc.leiedal.android.aandacht.View.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import osoc.leiedal.android.aandacht.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ViewSettingsFragment extends Fragment {

    /* ============================================================================================
        NESTED INTERFACES
    ============================================================================================ */

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    /* ============================================================================================
        STATIC METHODS
    ============================================================================================ */

    // unnecessary but this structure is according to android rules...
    public static ViewSettingsFragment newInstance() {
        return new ViewSettingsFragment();
    }

    /* ============================================================================================
        CONSTRUCTORS
    ============================================================================================ */

    // constructor is public AND there is a static newInstance method, this is according to android rules...
    public ViewSettingsFragment() {
        // fragment constructors must be empty
    }

    /* ============================================================================================
        MEMBERS
    ============================================================================================ */

    private OnFragmentInteractionListener mListener;

    /* ============================================================================================
        METHODS
    ============================================================================================ */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_settings, container, false);

        CompoundButton notif = (CompoundButton) view.findViewById(R.id.settings_notifications);
        CompoundButton sound = (CompoundButton) view.findViewById(R.id.settings_sound);
        CompoundButton vibrate = (CompoundButton) view.findViewById(R.id.settings_vibrate);


        SharedPreferences pref = getActivity().getSharedPreferences(getResources().getString(R.string.app_pref), 0);
        notif.setChecked(pref.getBoolean(getResources().getString(R.string.settings_option_notif), true));
        sound.setChecked(pref.getBoolean(getResources().getString(R.string.settings_option_sound), true));
        vibrate.setChecked(pref.getBoolean(getResources().getString(R.string.settings_option_vibrate), true));

        notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getActivity().getSharedPreferences(getResources().getString(R.string.app_pref), 0).edit().putBoolean(getResources().getString(R.string.settings_option_notif), isChecked).commit();
            }
        });

        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getActivity().getSharedPreferences(getResources().getString(R.string.app_pref), 0).edit().putBoolean(getResources().getString(R.string.settings_option_sound), isChecked).commit();
            }
        });

        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getActivity().getSharedPreferences(getResources().getString(R.string.app_pref), 0).edit().putBoolean(getResources().getString(R.string.settings_option_vibrate), isChecked).commit();
            }
        });


        return view;
    }

    // UNUSED METHOD
    /**
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
     */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
