package com.fortex.webtraderwrapper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fortex.common.prefs.Session;
import com.fortex.webtraderwrapper.data.WebTraderApiConstant;

public class MainActivity extends AppCompatActivity {

	private EditText editText; //the second level domain
	private Button buttonGo; //go to the HTML5 webtrader

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(secondLevelDomainNeeded()) {
			initInputView();
		}
	}

	private void initInputView() {
		if(editText == null) {
			editText = findViewById(R.id.et_domain);
			editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(buttonGo != null) {
						buttonGo.setEnabled(!TextUtils.isEmpty(s));
					}
				}

				@Override
				public void afterTextChanged(Editable s) { }
			});
		}

		if(buttonGo == null) {
			buttonGo = findViewById(R.id.btn_go);
			buttonGo.setOnClickListener(v -> {
				if(editText != null) {
					String secondLevelDomain = editText.getText().toString();
					if(TextUtils.isEmpty(secondLevelDomain)) {
						Toast.makeText(MainActivity.this, "Please input the second level domain!", Toast.LENGTH_LONG).show();
						return;
					}

					Session.putSessionString(WebTraderApiConstant.STRING_WEBTRADERDOMAIN_URL_KEY, secondLevelDomain);
					//launch the HTML5 webtrader
					launchWebtrader(secondLevelDomain);
				}
			});
		}
	}

	private boolean secondLevelDomainNeeded() {
		String secondLevelDomain = Session.getSessionString(WebTraderApiConstant.STRING_WEBTRADERDOMAIN_URL_KEY, null);
		return launchWebtrader(secondLevelDomain);
	}

	/**
	 * launch the HTML5 webtrader
	 *
	 * @param secondLevelDomain
	 */
	private boolean launchWebtrader(@NonNull String secondLevelDomain) {
		String h5TraderDomain = String.format(WebTraderApiConstant.STRING_WEBTRADERDOMAIN_URL_FMT, secondLevelDomain);
		if(!TextUtils.isEmpty(secondLevelDomain)) {
			//launch the HTML5 webtrader by brapper
			//TODO :
			finish();
			return false;
		}

		return true;
	}
}