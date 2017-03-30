package timur.imangadjiev.yandextest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import timur.imangadjiev.yandextest.R;
import timur.imangadjiev.yandextest.fragments.translate;
import timur.imangadjiev.yandextest.items.Def;
import timur.imangadjiev.yandextest.items.Mean;
import timur.imangadjiev.yandextest.items.Tr;

public class dictAdapter extends BaseAdapter {

    private List<Def> objects = new ArrayList<>();
    private LayoutInflater lInflater;
    private Context ctx;

    public dictAdapter(Context ctx, List<Def> objects){
        this.objects = objects;
        this.ctx = ctx;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Def getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_dict, parent, false);
        }

        LinearLayout llMain = (LinearLayout) view.findViewById(R.id.llMain);

        LinearLayout llMean = new LinearLayout(ctx);
        llMean.setOrientation(LinearLayout.VERTICAL);
        llMean.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        llMain.addView(llMean);

        if (objects.get(position).getTr() != null) {
            if (objects.get(position).getTr().size() > 0) {
                LinearLayout.LayoutParams lp24 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp24.setMargins(0,0,24,0);
                LinearLayout.LayoutParams lp10 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp10.setMargins(0,0,10,0);
                LinearLayout.LayoutParams lp10left = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp10left.setMargins(10,0,0,0);

                LinearLayout llFirstPos = new LinearLayout(ctx);
                llFirstPos.setOrientation(LinearLayout.VERTICAL);
                llFirstPos.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                llMean.addView(llFirstPos);

                LinearLayout llSecond = new LinearLayout(ctx);
                llSecond.setVisibility(View.GONE);
                llSecond.setOrientation(LinearLayout.HORIZONTAL);
                llSecond.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                llFirstPos.addView(llSecond);

                TextView tvThisText = new TextView(ctx);
                tvThisText.setTextColor(0xff000000);
                tvThisText.setTextSize(16);
                tvThisText.setLayoutParams(lp24);
                tvThisText.setText(objects.get(position).getText());
                llSecond.addView(tvThisText);

                if (objects.get(position).getGen() != null) {
                    llSecond.setVisibility(View.VISIBLE);
                    TextView tvGen = new TextView(ctx);
                    tvGen.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tvGen.setTextColor(0xffff0000);
                    tvGen.setTextSize(16);
                    tvGen.setLayoutParams(lp24);
                    tvGen.setVisibility(View.VISIBLE);
                    tvGen.setText(objects.get(position).getGen());
                    llSecond.addView(tvGen);
                }

                if (objects.get(position).getPos() != null) {
                    llSecond.setVisibility(View.VISIBLE);
                    TextView tvPos = new TextView(ctx);
                    tvPos.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tvPos.setTextColor(0xffa37d92);
                    tvPos.setTextSize(16);
                    tvPos.setVisibility(View.VISIBLE);
                    tvPos.setText(objects.get(position).getPos());
                    llSecond.addView(tvPos);
                }

                if (objects.get(position).getAnm() != null) {
                    TextView tvAnm = new TextView(ctx);
                    tvAnm.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tvAnm.setTextColor(0xff000000);
                    tvAnm.setVisibility(View.VISIBLE);
                    tvAnm.setTextSize(16);
                    tvAnm.setText(objects.get(position).getAnm());
                    llFirstPos.addView(tvAnm);
                }
                int pos = 0;
                for (Tr tr : objects.get(position).getTr()) {
                    LinearLayout llSin = new LinearLayout(ctx);
                    llSin.setOrientation(LinearLayout.HORIZONTAL);
                    llSin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    llMean.addView(llSin);
                    pos++;

                    TextView tvNumber = new TextView(ctx);
                    tvNumber.setTextColor(0xff9a9a9a);
                    tvNumber.setLayoutParams(lp10);
                    tvNumber.setText(String.valueOf(pos));
                    llSin.addView(tvNumber);

                    TextView tvText = new TextView(ctx);
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setTextColor(0xff4b7893);
                    tvText.setText(tr.getText());
                    tvText.setTag(tr.getText());
                    tvText.setOnClickListener(onClickListener());
                    llSin.addView(tvText);

                    if (tr.getSyn() != null) {
                        if (tr.getSyn().size() > 0) {
                            for (int j=0; j < tr.getSyn().size(); j++){
                                TextView tvSyn = new TextView(ctx);
                                tvSyn.setVisibility(View.VISIBLE);
                                tvSyn.setTextColor(0xff4b7893);
                                tvSyn.setOnClickListener(onClickListener());
                                tvSyn.setTag(tr.getSyn().get(j).getText());
                                if (j == 0) {
                                    tvSyn.setText(", " + tr.getSyn().get(j).getText() + ", ");
                                } else {
                                    if ((j+1) > tr.getSyn().size()){
                                        tvSyn.setText(tr.getSyn().get(j).getText());
                                    } else {
                                        tvSyn.setText(tr.getSyn().get(j).getText() + ", ");
                                    }
                                }
                                llSin.addView(tvSyn);
                            }
                        }
                    }

                    if (tr.getMean() != null) {
                        if (tr.getMean().size() > 0) {
                            String forMean = "(";
                            TextView tvMean = new TextView(ctx);
                            tvMean.setVisibility(View.VISIBLE);
                            tvMean.setTextColor(0xff8d634d);
                            tvMean.setLayoutParams(lp10left);
                            for (Mean m: tr.getMean()){
                                forMean += m.getText() + ", ";
                            }
                            forMean = forMean.substring(0, forMean.length() - 2);
                            forMean += ")";
                            tvMean.setText(forMean);
                            llMean.addView(tvMean);
                        }
                    }
                }
            }
        }
        return view;
    }

    View.OnClickListener onClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate.setText(String.valueOf(v.getTag()));
            }
        };
    }
}
