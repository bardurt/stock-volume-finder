package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.interactor.implementation.data.base.RangeInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Node;
import com.zygne.stockalyze.domain.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RangeInteractorImpl implements RangeInteractor {

    private Callback callback;
    private List<LiquidityZone> data;
    private int currentPrice;

    public RangeInteractorImpl(Callback callback, List<LiquidityZone> data, int currentPrice) {
        this.callback = callback;
        this.data = data;
        this.currentPrice = currentPrice;
    }

    @Override
    public void execute() {

        List<LiquidityZone> range = new ArrayList<>();

        if(currentPrice == 0){
            callback.onRangeGenerated(range);
            return;
        }


        int originIndex = -1;

        int index = 0;
        for(LiquidityZone e : data){
            if(e.price >= currentPrice){
               originIndex = index;
            } else {
                break;
            }
            index++;
        }

        if(originIndex == -1){
            callback.onRangeGenerated(range);
            return;
        }

        int count = 0;
        for(int i = originIndex; i > 0; i--){
            range.add(data.get(i));
            count++;
            if(count > 10){
                break;
            }

        }

        count = 0;
        for(int i = originIndex +1; i < data.size(); i++){
            range.add(data.get(i));
            count++;
            if(count > 10){
                break;
            }

        }

        range.sort(new LiquidityZone.PriceComparator());
        Collections.reverse(range);

        callback.onRangeGenerated(range);
    }
}
