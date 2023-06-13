package com.example.project;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

class MemoItem {
    public String category;
    public String memo;
    public String regDate;

    public MemoItem(String category, String memo) {

        // 날짜를 표시하기 위한 포멧터를 생성. 년-월-일 시:분:초를 표시하는 포맷을 작성한다.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        // 현재 날짜정보를 가진 Date객체를 생성한다.
        Date date = new Date();

        this.category = category;
        this.memo = memo;

        // 생성한 포맷터를 통해 현재 날짜 객체를 지정해서 새로운 포맷에 맞는 날짜정보를 regDate에 저장한다.
        this.regDate = formatter.format(date);

    }
}

class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.ViewHolder> {
    private Context context;
    private int resource;
    private ArrayList<MemoItem> itemList;

    // MemoListAdapter 생성자
    public MemoListAdapter(Context context, int resource, ArrayList<MemoItem> itemList) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;
    }

    // 어댑터의 itemList에 인자로 넘어온 아이템리스트를 추가하고,
    // notifyDataSetChanged() 메소드를 호출해서 추가된 아이템을 리사이클러뷰에 반영
    public void addItem(MemoItem item) {
        this.itemList.add(0, item);
        notifyDataSetChanged();
    }

    // 어댑터의 itemList로 넘어온 아이템 리스트를 추가하고
    // notifyDataSetChanged() 메소드를 호출해서 추가된 아이템을 리사이클러뷰에 반영함
    // notifyDataSetChanged() 메소드 호출 안하면 추가된 아이템이 사용자에게 보여지지 않게 됨
    public void addItemList(ArrayList<MemoItem> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return this.itemList.size();
    }

    // 뷰홀더 생성자
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(v);
    }

    // 데이터(아이템)과 뷰홀더를 매칭시키는 메소드
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MemoItem item = itemList.get(position);

        holder.categoryText.setText(item.category);
        holder.memoText.setText(item.memo);
        holder.dateText.setText(item.regDate);

        //아이템 클릭 시 토스트로 내용을 보여준다. 물론 클릭 시 다른 작업을 처리하도록 하는 것도 가능하다.
        holder.itemView.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, item.memo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 뷰 홀더 클래스를 정의하는 코드
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryText;
        TextView memoText;
        TextView dateText;

        public ViewHolder(View itemView) {
            super(itemView);

            categoryText = itemView.findViewById(R.id.category);
            memoText = itemView.findViewById(R.id.memo);
            dateText = itemView.findViewById(R.id.regdate);
        }
    }

}



public class MemoActivity  extends AppCompatActivity implements View.OnClickListener {

    // 아래는 이미 작성된 메모 리스트를 보여주는 데에 필요한 멤버들
    Context context;
    RecyclerView memoList;
    MemoListAdapter memoListAdapter; // 리사이클러뷰의 어댑터
    LinearLayoutManager layoutManager; //리사이클러뷰의 레이아웃 결정

    // 아래는 메모 작성 시 필요한 멤버들
    Spinner categorySpinner;
    EditText memoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_layout);

        context = this;
        setView();
    }

    // 레이아웃에서 뷰 찾고 버튼 리스너 설정
    private void setView() {
        categorySpinner = findViewById(R.id.category);
        memoEdit = findViewById(R.id.memo);

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(this);
        memoList = findViewById(R.id.recyclerView);

        setRecyclerView();
        setMemoListItem();
    }

    // 리사이클러뷰 설정: 레이아웃 매니저, 어댑터 설정
    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        memoList.setLayoutManager(layoutManager);

        memoListAdapter = new MemoListAdapter(context, R.layout.memo_item, new ArrayList<MemoItem>());
        memoList.setAdapter(memoListAdapter);
    }

    // 리사이클러뷰에 아이템 지정, 설정할 더미 아이템은 getMemoList에서 생성
    private void setMemoListItem () {
        String data = loadDataFromFile();
        if(data != null){
            ArrayList<MemoItem> list = getMemoDummyList(data);
            memoListAdapter.addItemList(list);
        }
    }

    // 파일에서 읽은 데이터를 MemoItem 리스트로 변환하는 메서드

    private ArrayList<MemoItem> getMemoDummyList(String data) {
        ArrayList<MemoItem> list = new ArrayList<>();
        String[] lines = data.split(System.getProperty("line.separator"));

        for(String line : lines){
            String[] items = line.split(",");
            if(items.length == 3){
                String category = items[0];
                String memo = items[1];
                MemoItem item = new MemoItem(category, memo);
                list.add(item);
            }
        }

        // sample items
        MemoItem item1 = new MemoItem("일상", "오늘 점심은 OOO과 함께할 것");
        MemoItem item2 = new MemoItem("회사", "오후 2시에 팀 회의가 있으니 관련 서류 준비");

        list.add(item1);
        list.add(item2);

        return list;
    }

    // 등록 버튼 클릭하면, 메모 등록 메서드 실행
    @Override
    public void onClick(View v) {
        registerMemo();
    }


    // 메모 등록 메서드: 아이템 추가, 뷰 초기화, 키패드 숨기기
    private void registerMemo () {
        String category = (String) categorySpinner.getSelectedItem();
        String memo = memoEdit.getText().toString();

        if(TextUtils.isEmpty(memo)) {
            Toast.makeText(context, R.string.msg_memo_input, Toast.LENGTH_SHORT) .show();
            return;
        }

        saveDataToFile(memo);
        addMemoItem(category, memo);

        categorySpinner.setSelection(0);
        memoEdit.setText("");

        hideKeyboard();
    }

    // 키패드 숨기기 메서드
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // 아이템을 어댑터에 추가하는 매서드
    private void addMemoItem(String category, String memo) {
        MemoItem item = new MemoItem(category, memo);

        memoListAdapter.addItem(item);
    }

    private void saveDataToFile(String data) {
        File file = new File(getFilesDir(), "hello.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(data.getBytes());
            // 저장 완료 메시지 등의 처리
            Log.d("저장:", "저장 완료!!");
        } catch (IOException e) {
            e.printStackTrace();
            // 저장 실패 처리
        }finally{
            try{
                if(fos != null){
                    fos.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private String loadDataFromFile() {
        File file = new File(getFilesDir(), "hello.txt");
        if(!file.exists()){
            Log.d("로드:", "파일이 존재하지 않습니다.");
            return null;
        }
        
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            Log.d("로드:", "로딩 완료");

            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            // 데이터 불러오기 실패 처리
        }finally{
            try{
                if(fis != null){
                    fis.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        return null;
    }
}
