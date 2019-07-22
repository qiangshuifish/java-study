
package com.putin.elasticjob.jobs;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JavaDataflowJob implements DataflowJob<Foo> {
    
    @Override
    public List<Foo> fetchData(final ShardingContext shardingContext) {
        System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
                shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "DATAFLOW FETCH"));
        List<Foo> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(new Foo(i,i/2+3+"",i/2 == 0?Foo.Status.COMPLETED:Foo.Status.TODO));
        }

        return list;
    }
    
    @Override
    public void processData(final ShardingContext shardingContext, final List<Foo> data) {
        System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
                shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "DATAFLOW PROCESS"));
        for (Foo each : data) {
            System.out.println(each.toString());
        }
    }
}
