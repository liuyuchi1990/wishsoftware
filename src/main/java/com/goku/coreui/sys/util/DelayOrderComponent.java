package com.goku.coreui.sys.util;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.order.mapper.OrderMapper;
import com.goku.coreui.order.service.OrderService;
import com.goku.coreui.sys.model.OrderMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

@Component
@Lazy(false)
public class DelayOrderComponent{

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    private static DelayQueue<OrderMessage> delayQueue =  new DelayQueue<OrderMessage>();

    @PostConstruct
    public void init() throws Exception {
        /**初始化时加载数据库中需处理超时的订单**/
//        List<OverTimeOrder> orderList = orderMapper.selectOverTimeOrder();
//        for (int i = 0; i < orderList.size(); i++) {
//            OrderMessage orderMessage = new OrderMessage(propertyCarList.get(i).getOrderId(),propertyCarList.get(i).getCreateTime());
//            this.addToOrderDelayQueue(orderMessage);
//        }

        /**启动一个线程，去取延迟消息**/
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                OrderMessage message = null;
                while (!delayQueue.isEmpty()) {
                    try {
                        message = delayQueue.take();
                        //处理超时订单
                        //orderService.closeOverTimeOrder(message.getOrderId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        System.out.println("run+++++++++++++++++");
    }

    /**加入延迟消息队列**/
    private boolean addToOrderDelayQueue(OrderMessage orderMessage){
        return delayQueue.add(orderMessage);
    }


    /**从延迟队列中移除**/
    private void removeToOrderDelayQueue(OrderMessage orderMessage){
        if(CommonUtil.isEmpty(orderMessage)){
            return;
        }
        for (Iterator<OrderMessage> iterator = delayQueue.iterator(); iterator.hasNext();) {
            OrderMessage queue = (OrderMessage) iterator.next();
            if(orderMessage.getOrderId().equals(queue.getOrderId())){
                delayQueue.remove(queue);
            }
        }
    }

    public static void main(String[] args) {
        final DelayQueue<OrderMessage> delayQueue =  new DelayQueue<OrderMessage>();
        long time = System.currentTimeMillis();
        delayQueue.add(new OrderMessage(""+1, time,"3秒后执行"));
        delayQueue.add(new OrderMessage(""+2, time,"4秒后执行"));
        delayQueue.add(new OrderMessage(""+3, time,"8秒后执行"));

        /**启动一个线程，处理延迟消息**/
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                OrderMessage message = null;
                while (!delayQueue.isEmpty()) {
                    try {
                        message = delayQueue.take();
                        System.out.println(new Date()+"  处理延迟消息:  "+ JSON.toJSONString(message));
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });

        System.out.println("run+++++++++++++++++");
    }

}