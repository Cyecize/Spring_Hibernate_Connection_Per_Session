package com.cyecize.multidb.areas.demo.controllers;

import com.cyecize.multidb.areas.demo.entities.Car;
import com.cyecize.multidb.areas.demo.entities.Order;
import com.cyecize.multidb.areas.demo.entities.User;
import com.cyecize.multidb.areas.demo.services.OrderService;
import com.cyecize.multidb.areas.demo.services.UserService;
import com.cyecize.multidb.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@PreAuthorize("isFullyAuthenticated()")
@RequestMapping("/orders")
public class OrdersController extends BaseController {

    private final OrderService orderService;

    private final UserService userService;

    @Autowired
    public OrdersController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/order/{carId}")
    public ModelAndView placeOrderAction(@PathVariable("carId") Car car, Principal principal) {
        if (car == null) return redirect("/cars/browse");

        this.orderService.placeOrder(car, this.userService.findByEmailOrUsername(principal.getName()));

        return redirect("/orders/browse?infoMsg=Order Was Placed!");
    }

    @GetMapping("/browse")
    public ModelAndView browseOrdersAction(Principal principal) {
        return view("orders/browse-orders", "orders", this.orderService.findByUser(this.userService.findByEmailOrUsername(principal.getName())));
    }

    @GetMapping("/decline/{orderId}")
    public ModelAndView declineOrderAction(@PathVariable("orderId") Order order, Principal principal) {
        User user = this.userService.findByEmailOrUsername(principal.getName());

        if (order == null || !order.getCustomer().getId().equals(user.getId())) return redirect("/orders/browse");

        this.orderService.removeOrder(order);

        return redirect("/orders/browse");
    }

    @GetMapping("/invalidate/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView invalidateOrderAction(@PathVariable("orderId") Order order) {
        this.orderService.removeOrder(order);
        return redirect("/orders/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView allRolesAction() {
        return view("orders/browse-orders", "orders", this.orderService.findAll());
    }
}
