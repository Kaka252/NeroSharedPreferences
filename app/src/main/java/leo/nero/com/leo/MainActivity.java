package leo.nero.com.leo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import leo.nero.com.leo.config.Constants;
import leo.nero.com.leo.sp.Nero;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etText = (EditText) findViewById(R.id.et_text);
        etText.setHint("请输入");

        findViewById(R.id.tv_save).setOnClickListener(this);
        findViewById(R.id.tv_clear).setOnClickListener(this);

        String s = Nero.getString(Constants.DATA_STRING, "");
        if (!TextUtils.isEmpty(s)) {
            etText.setText(s);
            etText.setSelection(s.length());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                String text = etText.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(getApplicationContext(), "请填写内容", Toast.LENGTH_SHORT).show();
                } else {
                    Nero.putString(Constants.DATA_STRING, text);
                    Toast.makeText(getApplicationContext(), "已保存", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_clear:
                etText.getText().clear();
                Nero.putString(Constants.DATA_STRING, "");
                Toast.makeText(getApplicationContext(), "已清除", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }
}
