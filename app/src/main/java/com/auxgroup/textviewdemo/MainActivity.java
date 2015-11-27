package com.auxgroup.textviewdemo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    private TextView tv_smile, tv_second,tv_src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //带图标文本
        tv_smile = (TextView) findViewById(R.id.tv_smile);
        Drawable[] compoundDrawables = tv_smile.getCompoundDrawables();
        //  compoundDrawables[1].setBounds(0,0,200,200);

//链接测试
        tv_second = (TextView) findViewById(R.id.tv_second);
        String s1 = "<font color='blue'><b>百度一下，你就知道~：</b></font><br>";
        s1 += "<a href = 'http://www.baidu.com'>百度</a>";
        tv_second.setText(Html.fromHtml(s1));
        tv_second.setMovementMethod(LinkMovementMethod.getInstance());

//        测试src标签，插入图片  反射
tv_src= (TextView) findViewById(R.id.tv_src);
        String s2 = "图片：<img src = 'smile2'/><br>";
        tv_src.setText(Html.fromHtml(s2, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable draw = null;

                    Field field = null;

                try {
                    field = R.drawable.class.getField(source);
                    int resourceId = 0;

                    resourceId = Integer.parseInt(field.get(null).toString());
                    draw = getResources().getDrawable(resourceId);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());


                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


                return draw;
            }
        },null));

        //span

        TextView t1 = (TextView) findViewById(R.id.txtOne);
        TextView t2 = (TextView) findViewById(R.id.txtTwo);

        SpannableString span = new SpannableString("红色打电话斜体删除线绿色下划线图片:.");
        //1.设置背景色,setSpan时需要指定的flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
        span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //2.用超链接标记文本
        span.setSpan(new URLSpan("tel:4155551212"), 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //3.用样式标记文本（斜体）
        span.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //4.用删除线标记文本
        span.setSpan(new StrikethroughSpan(), 7, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //5.用下划线标记文本
        span.setSpan(new UnderlineSpan(), 10, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //6.用颜色标记
        span.setSpan(new ForegroundColorSpan(Color.GREEN), 10, 13,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //7.//获取Drawable资源
        Drawable d = getResources().getDrawable(R.drawable.smile2);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //8.创建ImageSpan,然后用ImageSpan来替换文本
        ImageSpan imgspan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        span.setSpan(imgspan, 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        t1.setText(span);


//        实现部分可点击的TextView
        TextView tv_part= (TextView) findViewById(R.id.tv_part);
            StringBuilder sb=new StringBuilder();
        for (int i = 0; i <20 ; i++) {
            sb.append("好友："+i+",");
        }
        String likeUsers=sb.substring(0,sb.lastIndexOf(",")).toString();
        tv_part.setMovementMethod(LinkMovementMethod.getInstance());
        tv_part.setText(addClickPart(likeUsers), TextView.BufferType.SPANNABLE);

            

    }

    private SpannableStringBuilder addClickPart(String likeUsers) {
        ImageSpan imgspan=new ImageSpan(MainActivity.this,R.drawable.smile2);
        SpannableString spanStr=new SpannableString("p.");
        spanStr.setSpan(imgspan,0,1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(likeUsers);
        String[] likeUser=likeUsers.split(",");
        if (likeUser.length> 0) {
            for (int i = 0; i <likeUser.length ; i++) {
                final String name=likeUser[i];
                final int start=likeUsers.indexOf(name)+spanStr.length();
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(MainActivity.this,name,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                },start,start+name.length(),0);
            }
        }
        return ssb.append("等" +likeUser.length+ "个人觉得很赞");
    }
}
