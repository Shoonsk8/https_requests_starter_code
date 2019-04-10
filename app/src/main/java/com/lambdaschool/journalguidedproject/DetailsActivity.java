package com.lambdaschool.journalguidedproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 50;

    private JournalEntry entry;

    private TextView  dateView;
    private EditText  entryTextView;
    private SeekBar   dayRatingView;
    private ImageView dayImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ActivityLifecycle", getLocalClassName() + " - onCreate");

        setContentView(R.layout.activity_detail);

//        createJournalEntry();
        Intent intent = getIntent();
        entry = (JournalEntry) intent.getSerializableExtra(JournalEntry.TAG);
        if(entry == null) {
            entry = new JournalEntry(JournalEntry.INVALID_ID);
        }

        dateView = findViewById(R.id.journal_entry_date);
        dateView.setText(entry.getDate());

        entryTextView = findViewById(R.id.journal_entry_text);
        entryTextView.setText(entry.getEntryText());
        entryTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String entryString = s.toString();
                entry.setEntryText(entryString);
            }
        });

        dayRatingView = findViewById(R.id.journal_entry_seekbar);
        dayRatingView.setMax(5);
        dayRatingView.setProgress(entry.getDayRating());
        dayRatingView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    entry.setDayRating(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dayImageView = findViewById(R.id.journal_entry_image);
        final Uri imageUri = entry.getImage();
        if(imageUri != null) {
            // TODO: use file I/O to read image from path
//            dayImageView.setImageURI(imageUri);
        }

        findViewById(R.id.add_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            if (data != null) {
                Uri dataUri = data.getData();
                entry.setImage(dataUri);

                dayImageView.setImageURI(dataUri);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(JournalEntry.TAG, entry);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ActivityLifecycle", getLocalClassName() + " - onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ActivityLifecycle", getLocalClassName() + " - onResume");
    }

    // user interacting with app

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ActivityLifecycle", getLocalClassName() + " - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ActivityLifecycle", getLocalClassName() + " - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ActivityLifecycle", getLocalClassName() + " - onDestroy");
    }


}
