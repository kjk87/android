//package com.pplus.prnumberbiz.apps.common.component;
//
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.pplus.prnumberbiz.PRNumberBizApplication;
//import com.pplus.prnumberbiz.R;
//
///**
// * Created by j2n on 2016. 12. 8..
// */
//
//public class EmojiTextWatcher implements TextWatcher{
//
//    private EditText editText;
//
//    private Toast toast;
//
//    public EmojiTextWatcher(EditText editText){
//
//        this.editText = editText;
//        toast = Toast.makeText(editText.getContext(), PRNumberBizApplication.getContext().getString(R.string.msg_disable_emoticon), Toast.LENGTH_SHORT);
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after){
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count){
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s){
//
//        if(s.length() > 0) {
//
//            String result = EmojiParser.removeAllEmojis(s.toString());
//
//            if(result.length() != s.length()) {
//                //                ToastUtil.showAlert(PPlusApplication.getContext(), PPlusApplication.getContext().getString(R.string.msg_disable_emoticon));
//                toast.show();
//                editText.removeTextChangedListener(this);
//                editText.setText(result);
//                editText.setSelection(editText.getText().length());
//                editText.addTextChangedListener(this);
//            }
//
//        }
//
//    }
//}
