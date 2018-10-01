package com.thedot.mystoryinenglishn.Utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thedot.mystoryinenglishn.R;

/**
 * Created by okitoki on 2017. 9. 18..
 */

public class DownloadAlertDialog extends DialogFragment{
    public interface DialogOkEventListener{
        void onOkEvent();
    }
    public interface DialogcancelEventListener{
        void onCancelEvent();
    }
    private DialogOkEventListener okeventListener;
    private DialogcancelEventListener canceleventListener;

    public static DownloadAlertDialog newInstance(int title, String subject, String filename) {
        DownloadAlertDialog frag = new DownloadAlertDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("subject", subject);
        args.putString("filename", filename);
        frag.setArguments(args);
        return frag;
    }

    public void setOkDialogEventListner(DialogOkEventListener listener){
        okeventListener = listener;
    }
    public void setCanceleventListener(DialogcancelEventListener listener){
        canceleventListener = listener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.alertDailog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.downloaddialog_view,null);
        builder.setView(view);
        TextView subject = (TextView) view.findViewById(R.id.download_unit_subject);
        TextView filename = (TextView) view.findViewById(R.id.download_unit_number);
        subject.setText(getArguments().getString("subject"));
        filename.setText(getArguments().getString("filename"));

        final Button okbtn = (Button) view.findViewById(R.id.dialog_btn_ok);
        Button cancelbtn = (Button) view.findViewById(R.id.dialog_btn_cancel);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okeventListener.onOkEvent();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canceleventListener.onCancelEvent();
            }
        });
        return builder.create();
    }

}