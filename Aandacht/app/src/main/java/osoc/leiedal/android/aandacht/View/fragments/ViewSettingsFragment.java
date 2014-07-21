package osoc.leiedal.android.aandacht.View.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.View.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ViewSettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static ViewSettingsFragment newInstance() {
        ViewSettingsFragment fragment = new ViewSettingsFragment();
        return fragment;
    }

    public ViewSettingsFragment() {
        // Required empty public constructor
    }

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

        SharedPreferences pref = getActivity().getSharedPreferences(getResources().getString(R.string.app_pref),0);
        notif.setChecked(pref.getBoolean(getResources().getString(R.string.settings_option_notif),true));
        sound.setChecked(pref.getBoolean(getResources().getString(R.string.settings_option_sound),true));

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

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
