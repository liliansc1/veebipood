package ee.lilian.veebipood.controller;

import ee.lilian.veebipood.dto.OrderRowDto;
import ee.lilian.veebipood.dto.ParcelMachine;
import ee.lilian.veebipood.dto.PaymentUrl;
import ee.lilian.veebipood.entity.Order;
import ee.lilian.veebipood.repository.OrderRepository;
import ee.lilian.veebipood.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private OrderRepository orderRepository;
    private OrderService orderService;
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("parcelmachines")
    public List<ParcelMachine> getParcelMachines(@RequestParam String country) {
        String url = "https://www.omniva.ee/locations.json";
        ParcelMachine[] response = restTemplate.exchange(url, HttpMethod.GET, null, ParcelMachine[].class).getBody();
        return Arrays.stream(response)
                .filter(e -> e.getA0_name().equals(country.toUpperCase()))
                .toList();
    }

    @GetMapping("orders")
    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

    @DeleteMapping("orders/{id}")
    public List<Order> deleteOrder(@PathVariable Long id){
        orderRepository.deleteById(id); //kustutan
        return orderRepository.findAll();//uuenenud seis
    }

    @PostMapping("orders")
    public PaymentUrl addOrder(@RequestParam Long personId,
                                @RequestParam(required = false) String parcelMachine,
                                @RequestBody List<OrderRowDto> orderRows){
        Order order =  orderService.saveOrder(personId, parcelMachine, orderRows);//siin salvestab
        return orderService.makePayment(order.getId(), order.getTotal());
        //return orderRepository.findAll();//siin on kogu list tagastud
    }

    //@PostMapping("pay")
    //public PaymentUrl makePayment(@RequestParam Long orderId, double sum) {
    //    return orderService.makePayment(orderId, sum);
    //}

}
