package com.goku.coreui.sys.util;

import com.goku.coreui.order.mapper.OrderMapper;
import com.goku.coreui.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;

@Component
@Lazy(false)
public class DelayOrderComponent{
//
//    @Autowired
//    private OrderMapper orderMapper;
//
//    @Autowired
//    private OrderService orderService;
//
//    private static DelayQueue<OrderMessage> delayQueue =  new DelayQueue<OrderMessage>();
//
//    @PostConstruct
//    public void init() throws Exception {
//        /**初始化时加载数据库中需处理超时的订单**/
//        List<OverTimeOrder> orderList = orderMapper.selectOverTimeOrder();
//        for (int i = 0; i < orderList.size(); i++) {
//            OrderMessage orderMessage = new OrderMessage(propertyCarList.get(i).getOrderId(),propertyCarList.get(i).getCreateTime());
//            this.addToOrderDelayQueue(orderMessage);
//        }
//
//        /**启动一个线程，去取延迟消息**/
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                OrderMessage message = null;
//                while (true) {
//                    try {
//                        message = delayQueue.take();
//                        //处理超时订单
//                        orderService.closeOverTimeOrder(message.getOrderId());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
//
//    /**加入延迟消息队列**/
//    private boolean addToOrderDelayQueue(OrderMessage orderMessage){
//        return delayQueue.add(orderMessage);
//    }
//
//
//    /**从延迟队列中移除**/
//    private void removeToOrderDelayQueue(OrderMessage orderMessage){
//        if(Tools.isEmpty(orderMessage)){
//            return;
//        }
//        for (Iterator<OrderMessage> iterator = delayQueue.iterator(); iterator.hasNext();) {
//            OrderMessage queue = (OrderMessage) iterator.next();
//            if(orderMessage.getOrderId().equals(queue.getOrderId())){
//                delayQueue.remove(queue);
//            }
//        }
//    }

}