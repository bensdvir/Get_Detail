package ws.wolfsoft.get_detail;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.HashMap;

import UI_objects.Beanclass;
import UI_objects.ChildAnimationExample;
import UI_objects.ExpandableHeightGridView;
import UI_objects.ExpandableHeightListView;
import UI_objects.GridviewAdapter;
import UI_objects.JayBaseAdapter;
import UI_objects.SliderLayout;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    SliderLayout mDemoSlider;



    LinearLayout linear1,showless,review;
    LinearLayout linear2;

    Typeface fonts1,fonts2;
    private ExpandableHeightGridView gridview;
    private ExpandableHeightListView listview;
    private ArrayList<UI_objects.Bean>Bean;
    private JayBaseAdapter baseAdapter;
    private ArrayList<Beanclass> beanclassArrayList;
    private GridviewAdapter gridviewAdapter;





    private int[] IMAGE = {R.drawable.p1, R.drawable.p2, R.drawable.p3};
    private String[] TITLE = {"Best seller", "Dunt think twise for it", "Good product"};
    private String[] RATING = {"4.5 rating", "5 rating", "4 rating"};
    private String[] BY = {"by Kelly","by Emma","by Erik"};


    private int[] IMAGEgrid = {R.drawable.pik1, R.drawable.pik2, R.drawable.pik3, R.drawable.pik4};
    private String[] TITLeGgrid = {"Min 70% off", "Min 50% off", "Min 45% off",  "Min 60% off"};
    private String[] DIscriptiongrid = {"Wrist Watches", "Belts", "Sunglasses","Perfumes"};
    private String[] Dategrid = {"Explore Now!","Grab Now!","Discover now!", "Great Savings!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        ********LISTVIEW***********


        /*listview = (ExpandableHeightListView)findViewById(R.id.listview);


        Bean = new ArrayList<Bean>();

        for (int i= 0; i< TITLE.length; i++){

            Bean bean = new Bean(IMAGE[i], TITLE[i], RATING[i], BY[i]);
            Bean.add(bean);

        }

        baseAdapter = new JayBaseAdapter(MainActivity.this, Bean) {
        };

        listview.setAdapter(baseAdapter);
        */



//        ********GRIDVIEW***********


        gridview = (ExpandableHeightGridView)findViewById(R.id.gridview);
        beanclassArrayList= new ArrayList<Beanclass>();

        for (int i= 0; i< IMAGEgrid.length; i++) {

            //Beanclass beanclass = new Beanclass("", TITLeGgrid[i], DIscriptiongrid[i], Dategrid[i]);
            Beanclass beanclass = new Beanclass(IMAGEgrid[i], TITLeGgrid[i], DIscriptiongrid[i], Dategrid[i]);

            beanclassArrayList.add(beanclass);

        }
        gridviewAdapter = new GridviewAdapter(MainActivity.this, beanclassArrayList);
        gridview.setExpanded(true);

        gridview.setAdapter(gridviewAdapter);


//        ********lInearlayout()***********

//        review = (LinearLayout)findViewById(R.id.review);
//
//
//        for(int i=0;i<IMAGE.length;i++) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(2,150);
//            // product.setOrientation(LinearLayout.HORIZONTAL);
//            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(150, 150);
//            int colorInt0 = getResources().getColor(R.color.colorblue);
//
//
//
//
//
//            LinearLayout ll1 = new LinearLayout(this);
//            ll1.setOrientation(LinearLayout.HORIZONTAL);
//
//            LinearLayout ll2 = new LinearLayout(this);
//            ll2.setOrientation(LinearLayout.VERTICAL);
//
//
////            View div1 = new View(this);
////            div1.setLayoutParams(params);
////            div1.setBackgroundColor(colorInt0);
////            div1.setPadding(10, 0, 0, 0);
//
//
//
//            ImageView imageView1 = new ImageView(this);
//            // imageView.setImageResource(R.drawable.ac);
//            imageView1.setImageResource(IMAGE[i]);
//            imageView1.setLayoutParams(params1);
//            imageView1.setPadding(0, 0, 0, 0);
//
//
//            TextView valuename1 = new TextView(this);
//            valuename1.setText(TITLE[i]);
//            valuename1.setId(R.id.title);
//            valuename1.setTextSize(16);
//            valuename1.setPadding(16, 10, 0, 10);
//            valuename1.setTextColor(colorInt0);
//
//            // valuename.setLayoutParams(params);
//
//            TextView valuecoment1 = new TextView(this);
//            valuecoment1.setText(RATING[i]);
//            valuename1.setId(R.id.title);
//            valuecoment1.setTextSize(11);
//            valuecoment1.setPadding(16, 0, 0, 0);
//            valuecoment1.setLineSpacing(5, 1);
//
//
//            TextView valuecoment2 = new TextView(this);
//            valuecoment2.setText(BY[i]);
//            valuename1.setId(R.id.title);
//            valuecoment2.setTextSize(11);
//            valuecoment2.setPadding(16, 10, 0, 10);
//            valuecoment2.setLineSpacing(5, 1);
//
//
//            ll1.addView(imageView1);
//
//            ll2.addView(valuename1);
//            ll2.addView(valuecoment1);
//            ll2.addView(valuecoment2);
////            ll1.addView(div1);
//
//
//            ll1.addView(ll2);
//
//            review.addView(ll1);
//
//        }




//
//        fonts1 =  Typeface.createFromAsset(MainActivity.this.getAssets(),
//                "fonts/Lato-Light.ttf");
//
//
//
//        fonts2 =  Typeface.createFromAsset(MainActivity.this.getAssets(),
//                "fonts/Lato-Regular.ttf");
//
//
//        TextView t5 =(TextView)findViewById(R.id.title);
//        t5.setTypeface(fonts1);












//                ***********viewmore**********

        linear1 = (LinearLayout)findViewById(R.id.linear1);
        showless = (LinearLayout)findViewById(R.id.showless);

        linear2 = (LinearLayout)findViewById(R.id.linear2);

        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear2.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.GONE);

            }
        });

        showless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);


            }
        });

//         ********Slider*********

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.iohone1);
        file_maps.put("2",R.drawable.iphone2);
        file_maps.put("3",R.drawable.iphone3);
        

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
