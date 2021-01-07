package com.namespace.translatetext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
    }
    public void translateText(View view){
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.FRENCH)
                        .build();

        final Translator englishFrenchTranslator =
                Translation.getClient(options);

        String text = String.valueOf(editText.getText());
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();



        englishFrenchTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        (OnSuccessListener) v -> {
                            englishFrenchTranslator.translate(text)
                                    .addOnSuccessListener(
                                            (OnSuccessListener) translatedText -> {
                                                textView.setText("");
                                                textView.append((String) translatedText);

                                                Log.i("TAG", "Translation is "+(String) translatedText);
                                            })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Error.
                                                    Log.e("Error","Translation faliled "+e);
                                                }
                                            });
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                Log.e("Error","Model could n’t be downloaded "+e);

                            }
                        });



    }
}
