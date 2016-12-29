package com.malalaoshi.android.malapad.classexercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.malalaoshi.android.core.base.BaseFragment;
import com.malalaoshi.android.core.utils.MiscUtil;
import com.malalaoshi.android.malapad.R;
import com.malalaoshi.android.malapad.classexercises.adapter.QuestionAdapter;
import com.malalaoshi.android.malapad.data.entity.Option;
import com.malalaoshi.android.malapad.data.entity.ChoiceQuestion;
import com.malalaoshi.android.malapad.usercenter.UserManager;
import com.malalaoshi.android.malapad.usercenter.login.LoginActivity;
import com.malalaoshi.comm.views.ScrollListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kang on 16/12/26.
 */

public class ExercisesFragment extends BaseFragment implements ExercisesContract.View, View.OnClickListener {

    @BindView(R.id.sub_status_view)
    View subStatusView;

    @BindView(R.id.tv_user_info)
    TextView tvUserInfo;

    @BindView(R.id.iv_logout)
    ImageView ivLogout;

    @BindView(R.id.rl_questions)
    RelativeLayout rlQuestions;

    @BindView(R.id.listview_questions)
    ScrollListView listviewQuestions;

    @BindView(R.id.tv_questions_title)
    TextView tvQuestionsTitle;

    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @BindString(R.string.user_info)
    String strUserInfo;

    private QuestionAdapter mQuestionAdapter;

    private List<ChoiceQuestion> choiceQuestionList;

    private ExercisesContract.Presenter mPresenter;

    public static ExercisesFragment newInstance() {
        return new ExercisesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exercises,container,false);
        ButterKnife.bind(this,root);
        initDate();
        setEvent();root.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mPresenter.loadQuestionsTask("001");
                onSuccess();
            }
        },3000);
        return root;
    }

    private void initDate() {
        UserManager userManager = UserManager.getInstance();
        tvUserInfo.setText(String.format(strUserInfo,userManager.getSchool(),userManager.getName()));
    }

    private void setEvent() {
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onSuccess() {
        rlQuestions.setVisibility(View.VISIBLE);
        subStatusView.setVisibility(View.GONE);
        tvQuestionsTitle.setText("这是一组英语题");
        choiceQuestionList = new ArrayList<>();
        List<Option> options = new ArrayList<Option>();
        options.add(new Option("001","Both",false));
        options.add(new Option("002","Neither",false));
        options.add(new Option("003","None",false));
        options.add(new Option("004","Either",false));
        choiceQuestionList.add(new ChoiceQuestion("--Which would you like, a cup of tea or a glass of milk?\n" + "--- ______. I think I’ll just have a glass of water.", options));

        options = new ArrayList<Option>();
        options.add(new Option("001","such an exciting",false));
        options.add(new Option("002","so an exciting",false));
        options.add(new Option("003","such an excited",false));
        options.add(new Option("004","so an excited",false));
        choiceQuestionList.add(new ChoiceQuestion("I’ve never seen ______ match before.", options));

        options = new ArrayList<Option>();
        options.add(new Option("001","tell",false));
        options.add(new Option("002","talk",false));
        options.add(new Option("003","speak",false));
        options.add(new Option("004","say",false));
        choiceQuestionList.add(new ChoiceQuestion("We usually _____ hello to each other", options));

        options = new ArrayList<Option>();
        options.add(new Option("001","are, is",false));
        options.add(new Option("002","are, are",false));
        options.add(new Option("003","is, are",false));
        options.add(new Option("004","is, is",false));
        choiceQuestionList.add(new ChoiceQuestion("There _____ a great number of students over there. The number of the students ____ five thousand.", options));

        options = new ArrayList<Option>();
        options.add(new Option("001","are used to take a walk, am used to swim",false));
        options.add(new Option("002","are used to taking a walk, am used to swimming",false));
        options.add(new Option("003","used to take a walk, used to swim",false));
        options.add(new Option("004","used to take a walk, am used to swimming",false));
        choiceQuestionList.add(new ChoiceQuestion("—Can you remember this park? We _____ here.—Sure. But now I ______ in that swimming pool.\n", options));

        options = new ArrayList<Option>();
        options.add(new Option("001","wish",false));
        options.add(new Option("002","to wish",false));
        options.add(new Option("003","hope",false));
        options.add(new Option("004","to hope",false));
        choiceQuestionList.add(new ChoiceQuestion("The Chinese ping-pong players will join in the match. Let's _____ them success.", options));
        mQuestionAdapter = new QuestionAdapter(getContext(), choiceQuestionList);
        listviewQuestions.setAdapter(mQuestionAdapter);
        mQuestionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinished() {

    }

    @Override
    public void setPresenter(ExercisesContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @OnClick(R.id.iv_logout)
    public void onClickLogout(View view){
        //1、清除本地认证信息
        //2、跳转至登录页面
        UserManager.getInstance().logout();
        LoginActivity.launch(getContext());
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        Map<String,Option> mapSelected = mQuestionAdapter.getSelectedOptions();
        if (choiceQuestionList.size()!=mapSelected.size()){
            MiscUtil.toast("题目还没有答完");
        }
        mPresenter.submitAnswerTask(mapSelected);
    }
}